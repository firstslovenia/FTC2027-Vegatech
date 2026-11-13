package org.firstinspires.ftc.teamcode.opmodes.production.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.NVPosition;
import org.firstinspires.ftc.teamcode.ShooterPlusPlus;
import org.firstinspires.ftc.teamcode.ShooterPositioning;
import org.firstinspires.ftc.teamcode.Turret;
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
    Turret turret;
	LedIndicator ledIndicator;
	ShooterPlusPlus shooter;
	Follower pedroFollower;

    public Pose2D starting_pos = new Pose2D(DistanceUnit.METER, 1.0, 1.0, AngleUnit.RADIANS, Math.PI / 2.0);

    public Team team = null;

    boolean intake_enabled = false;
    boolean transfer_enabled = false;

    double led_position_to_set = LedIndicator.OFF_POSITION;

	@Override
	public void runOpMode() {

		hardware = new Hardware(this);
		hardware.init();

        // Load saved non-volatile position
        NVPosition pos = NVPosition.load();

        if (pos != null) {
            starting_pos = Util.to_pose2d(pos.pos);
        }

		drivetrain = new Drivetrain(this, hardware);
		drivetrain.fieldCentricTranslation = true;
		drivetrain.fieldCentricRotation = true;
		drivetrain.keepHeading = true;

        turret = new Turret(this, drivetrain, hardware);
        turret.init();

        shooter = new ShooterPlusPlus(this, hardware);

		ledIndicator = new LedIndicator(this, hardware);
		led_position_to_set = LedIndicator.OFF_POSITION;

		waitForStart();

        hardware.plowServo.setPosition(1.0);

        drivetrain.setPos(starting_pos);
        drivetrain.resetStartingDirection();

		while (opModeIsActive()) {

            long now = System.currentTimeMillis();

            led_position_to_set = LedIndicator.OFF_POSITION;

            turret.target_pos = Util.to_pose2d(Constants.get_target_for_pos(drivetrain.odometry.getPose(), team));

			Vector2D translation_vector = new Vector2D(gamepad1.left_stick_x, -gamepad1.left_stick_y);
			Vector2D rotation_vector = new Vector2D(gamepad1.right_stick_x, -gamepad1.right_stick_y);

            if (gamepad1.dpad_up) { drivetrain.wanted_heading = Math.PI / 2.0; }
            if (gamepad1.dpad_down) { drivetrain.wanted_heading = -Math.PI / 2.0; }
            if (gamepad1.dpad_right) { drivetrain.wanted_heading = 0; }
            if (gamepad1.dpad_left) { drivetrain.wanted_heading = Math.PI; }

            // Enable / disable intake
            if (Math.abs(gamepad1.left_trigger) > 0.2) {
                hardware.intakeMotor.setPower(0.5);
            } else {
                hardware.intakeMotor.setPower(0.0);
            }

            if (gamepad1.b) {
                hardware.intakeMotor.setPower(-0.5);
            }

            telemetry.addData("Intake power", hardware.intakeMotor.getPower());

			// Reset robot to -90 degrees
			if (gamepad1.a) {
                drivetrain.wanted_heading = -Math.PI / 2.0;
			}

            if (gamepad1.guideWasPressed()) {
                drivetrain.resetStartingDirection();
            }

            // Shoot when holding RT
            if (Math.abs(gamepad1.right_trigger) > 0.2) {
                hardware.transferMotor.setPower(1.0);
                hardware.intakeMotor.setPower(0.75);
            } else {
                hardware.transferMotor.setPower(0.0);
            }

            // Enable / disable the shooter
			if (gamepad1.xWasPressed()) {
				if (shooter.flywheel_enabled) {
					shooter.disable_flywheel();
				} else {
                    shooter.run();
                }
			}

            shooter.update();
            turret.update();

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

            // Update shooter for the current position
            if (turret.target_pos != null) {
                shooter.update_for_target(turret.target_pos_info);
            }

            if (shooter.flywheel_enabled) {
                if (Double.isNaN(shooter.shooting_distance_m)) {
                    led_position_to_set = LedIndicator.BLUE_POSITION;
                } else {
                    led_position_to_set = LedIndicator.INDIGO_POSITION;
                }
            }

            if (turret.target_pos_info != null) {
                telemetry.addData("Target distance  [m]", turret.target_pos_info);
                telemetry.addData("Target age      [ms]", now - turret.target_pos_info.time_ms);
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
