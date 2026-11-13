package org.firstinspires.ftc.teamcode.opmodes.production.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.NVPosition;
import org.firstinspires.ftc.teamcode.ShooterPlusPlus;
import org.firstinspires.ftc.teamcode.ShooterPositioning;
import org.firstinspires.ftc.teamcode.generic.LedIndicator;
import org.firstinspires.ftc.teamcode.TargetInformation;
import org.firstinspires.ftc.teamcode.Webcam;
import org.firstinspires.ftc.teamcode.generic.SlidingWindow;
import org.firstinspires.ftc.teamcode.generic.Team;
import org.firstinspires.ftc.teamcode.generic.Util;
import org.firstinspires.ftc.teamcode.generic.Vector2D;

@Disabled
@TeleOp(name = "!! DO NOT USE !! Main (Unknown Team)", group = "Production")
public class Main extends LinearOpMode {

    // Higher number is facing more down
    // -30 deg ish
    public static final double HIGHER_CAMERA_POSITION = 0.44;
    // -11 deg ish
    public static final double LOWER_CAMERA_POSITION = 0.52;

    public static final long LONG_LOOP_DELAY_MS = 100;

	Hardware hardware;
	Drivetrain drivetrain;
	LedIndicator ledIndicator;
	ShooterPlusPlus shooter;
    Webcam webcam;
	Follower pedroFollower;

    Pose2D starting_pos = new Pose2D(DistanceUnit.METER, 1.0, 1.0, AngleUnit.RADIANS, Math.PI / 2.0);

    Team team = null;

    boolean manual_spindexer = false;
    boolean intake_enabled = false;

    /// If the camera servo is currently in the high position
    boolean camera_servo_high = false;

    /// The TargetInformation of the target we're currently rotating towards
    TargetInformation target_to_rotate_to = null;
    ShooterPositioning positioning = null;

    /// The wanted heading of the target for target_to_rotate_to
    double wanted_heading_for_target = Double.NaN;
    long last_long_loop_ms = 0;
    long aligned_via_camera = 0;

    SlidingWindow<Long> last_loops_took = new SlidingWindow(50);
    long last_loop_time = System.currentTimeMillis();

    double led_position_to_set = LedIndicator.OFF_POSITION;

    /// Overriden to set what's in the spindexer
    public void spindexer_override() {

    }

	@Override
	public void runOpMode() {

		hardware = new Hardware(this);
		hardware.init();

        // Load saved non-volatile position
        NVPosition pos = NVPosition.load();

        if (pos != null) {
            long now = System.currentTimeMillis();

            long since_save_ms = now - pos.saved_at;
            long one_hour_ms = 60 * 60 * 1000;

            if (since_save_ms >= 0 && since_save_ms <= one_hour_ms) {
                starting_pos = Util.to_pose2d(pos.pos);
            }
        }

		drivetrain = new Drivetrain(this, hardware);
		drivetrain.fieldCentricTranslation = true;
		drivetrain.fieldCentricRotation = true;
		drivetrain.keepHeading = true;

		ledIndicator = new LedIndicator(this, hardware);
		led_position_to_set = LedIndicator.OFF_POSITION;

		webcam = new Webcam(this, hardware);

		waitForStart();

        drivetrain.resetStartingDirection();
        drivetrain.setPos(starting_pos);

        hardware.cameraAngleServo.setPosition(LOWER_CAMERA_POSITION);

		while (opModeIsActive()) {

            long now = System.currentTimeMillis();

            boolean do_long_loop = (now - last_long_loop_ms) >= LONG_LOOP_DELAY_MS;

            led_position_to_set = LedIndicator.OFF_POSITION;

			Vector2D translation_vector = new Vector2D(gamepad1.left_stick_x, -gamepad1.left_stick_y);
			Vector2D rotation_vector = new Vector2D(gamepad1.right_stick_x, -gamepad1.right_stick_y);

            if (gamepad1.dpad_up) { drivetrain.wanted_heading = Math.PI / 2.0; }
            if (gamepad1.dpad_down) { drivetrain.wanted_heading = -Math.PI / 2.0; }
            if (gamepad1.dpad_right) { drivetrain.wanted_heading = 0; }
            if (gamepad1.dpad_left) { drivetrain.wanted_heading = Math.PI; }

            // Enable / disable intake
            if (gamepad1.xWasPressed()) {
                intake_enabled = !intake_enabled;

                if (intake_enabled) {
                    hardware.intakeMotor.setPower(0.6);
                } else {
                    hardware.intakeMotor.setPower(0.0);
                }
            }

            if (gamepad1.bWasPressed()) {
                if (intake_enabled && hardware.intakeMotor.getPower() > 0.5) {
                    hardware.intakeMotor.setPower(-0.6);
                } else {
                    intake_enabled = !intake_enabled;

                    if (intake_enabled) {
                        hardware.intakeMotor.setPower(-0.6);
                    } else {
                        hardware.intakeMotor.setPower(0.0);
                    }
                }
            }

            telemetry.addData("Intake power", hardware.intakeMotor.getPower());

			// Reset robot to -90 degrees
			if (gamepad1.a) {
                drivetrain.wanted_heading = -Math.PI / 2.0;
			}

            // Set robot to goal's angle
            if (team == null) {
                if (gamepad1.left_trigger > 0.1) {
                    drivetrain.wanted_heading = Math.toRadians(90.0 - 36.0);
                } else if (gamepad1.right_trigger > 0.1) {
                    drivetrain.wanted_heading = Math.toRadians(90.0 + 36.0);
                }
            } else {
                if (gamepad1.left_trigger > 0.1 || gamepad1.right_trigger > 0.1) {
                    if (team == Team.Blue) {
                        drivetrain.wanted_heading = Math.toRadians(90.0 - 36.0);
                    } else if (team == Team.Red) {
                        drivetrain.wanted_heading = Math.toRadians(90.0 + 36.0);
                    }
                }
            }

            // Rotate towards the goal
            if (gamepad1.y || gamepad1.right_bumper) {

                boolean recalculate_from_odometry = true;

                if (target_to_rotate_to != null) {
                    if (now - aligned_via_camera < 5000) {
                        recalculate_from_odometry = false;
                    }
                }

                if (recalculate_from_odometry) {
                    positioning = new ShooterPositioning();
                    target_to_rotate_to = positioning.compute_target_information(drivetrain.last_robot_position, team);
                    aligned_via_camera = 0;
                    wanted_heading_for_target = drivetrain.getCurrentHeading() + target_to_rotate_to.angle_distance_rads;
                    drivetrain.wanted_heading = wanted_heading_for_target;
                }
            }

            if (gamepad1.guideWasPressed()) {
                drivetrain.resetStartingDirection();
            }

            // User 2
            // Enable / disable the shooter
			if (gamepad2.rightStickButtonWasPressed()) {
				if (shooter.flywheel_enabled) {
					shooter.disable_flywheel();
				} else {
                    shooter.run();
                }
			}

            // Camera angle
            double camera_servo_position = hardware.cameraAngleServo.getPosition();
            if (gamepad2.leftStickButtonWasPressed()) {

                camera_servo_high = !camera_servo_high;

                if (camera_servo_high) {
                    camera_servo_position = HIGHER_CAMERA_POSITION;
                } else {
                    camera_servo_position = LOWER_CAMERA_POSITION;
                }

                hardware.cameraAngleServo.setPosition(camera_servo_position);
            }

            shooter.update();

            if (shooter.flywheel_enabled) {
                telemetry.addLine("Shooter ON");
            } else {
                telemetry.addLine("Shooter OFF");
            }

            double shooter_wanted_rpm = shooter.wanted_flywheel_rpm;
            double shooter_real_rpm = shooter.last_rpm_measurements.average().orElse(0.0);
            boolean shooter_correct_speed = Math.abs(shooter_wanted_rpm - shooter_real_rpm) <= 200.0;

            telemetry.addData("Shooter @ correct speed", shooter_correct_speed);
            telemetry.addLine("(" + shooter_wanted_rpm + " vs " + shooter_real_rpm + ")");

			drivetrain.update(translation_vector, rotation_vector);

            if (do_long_loop) {
                webcam.update();

                if (webcam.target_position != null && !webcam.target_position.is_old() && gamepad1.left_bumper) {
                    target_to_rotate_to = webcam.target_position;
                    wanted_heading_for_target = drivetrain.getCurrentHeading() + target_to_rotate_to.angle_distance_rads;
                    drivetrain.wanted_heading = wanted_heading_for_target;

                    webcam.target_position = null;
                    aligned_via_camera = now;

                    // TODO: potentially update odometry here
                }

                last_long_loop_ms = now;
            }

            // Update shooter for the current position
            if (target_to_rotate_to != null) {
                if (webcam.target_position != null && now - webcam.target_position.time_ms < 5000) {
                    shooter.update_for_target(webcam.target_position);
                } else {
                    shooter.update_for_target(target_to_rotate_to);
                }
            }

            if (shooter.flywheel_enabled) {
                if (Double.isNaN(shooter.shooting_distance_m)) {
                    led_position_to_set = LedIndicator.BLUE_POSITION;
                } else {
                    led_position_to_set = LedIndicator.INDIGO_POSITION;
                }
            }

            if (now - aligned_via_camera < 1000) {
                telemetry.addLine("Apriltag detected!");

                if (gamepad1.left_bumper) {
                    led_position_to_set = LedIndicator.GREEN_POSITION;
                }
            } else {
                telemetry.addLine("Apriltag NOT detected!");
            }

            if (target_to_rotate_to != null) {
                telemetry.addData("Target distance  [m]", target_to_rotate_to.distance_m);
                telemetry.addData("Target age      [ms]", now - target_to_rotate_to.time_ms);
            } else {
                telemetry.addLine("Target: null");
            }

            if (drivetrain.set_new_pos_at != 0 || drivetrain.started_compass_recalibration_ms != 0) {
                led_position_to_set = LedIndicator.ORANGE_POSITION;
            }

            hardware.rgbLed.setPosition(led_position_to_set);

            telemetry.update();
		}
	}
}
