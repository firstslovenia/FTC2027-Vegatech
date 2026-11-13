package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Turret;
import org.firstinspires.ftc.teamcode.generic.Vector2D;

@TeleOp(name = "Turret Tracking Test", group = "Testing")
public class TurretTrackingTest extends LinearOpMode {

    Hardware hardware;
    Drivetrain drivetrain;
    Turret turret;

    @Override
    public void runOpMode() {

        hardware = new Hardware(this);
        hardware.init();

        drivetrain = new Drivetrain(this, hardware);
        drivetrain.fieldCentricTranslation = true;
        drivetrain.fieldCentricRotation = true;
        drivetrain.setPos(new Pose2D(DistanceUnit.METER, 0.0, 0.0, AngleUnit.RADIANS,0.0));
        drivetrain.resetStartingDirection();
        drivetrain.keepHeading = true;

        turret = new Turret(this, drivetrain, hardware);

        waitForStart();

        while (opModeIsActive()) {

            Vector2D translation_vector = new Vector2D(gamepad1.left_stick_x, -gamepad1.left_stick_y);
            Vector2D rotation_vector = new Vector2D(gamepad1.right_stick_x, -gamepad1.right_stick_y);

            if (gamepad1.aWasPressed()) {
                if (turret.target_pos != null) {
                    turret.target_pos = null;
                } else {
                    turret.target_pos = new Pose2D(DistanceUnit.METER, 0.0, 0.0, AngleUnit.RADIANS, 0.0);
                }
            }

            if (gamepad1.guideWasPressed()) {
                drivetrain.resetStartingDirection();
            }

            drivetrain.update(translation_vector, rotation_vector);
            turret.update();

            if (turret.target_pos != null) {
                double drive_angle_rads = drivetrain.odometry.getPose().getHeading() - Math.PI / 2;

                telemetry.addData("target x [m]", turret.target_pos.getX(DistanceUnit.METER));
                telemetry.addData("target y [m]", turret.target_pos.getY(DistanceUnit.METER));
                telemetry.addData("distance [m]", turret.target_pos_info.distance_m);
                telemetry.addData("ideal  abs [°]", Math.toDegrees(turret.target_pos_info.ideal_angle_to_target));
                telemetry.addData("turret abs [°]", Math.toDegrees(turret.current_angle() + drive_angle_rads));
                telemetry.addData("turret loc [°]", Math.toDegrees(turret.current_angle()));
                telemetry.addData("drive [°]", Math.toDegrees(drive_angle_rads));
                telemetry.addData("turret abs target [°]", Math.toDegrees(turret.target_angle() + drive_angle_rads));
                telemetry.addData("turret loc target [°]", Math.toDegrees(turret.target_angle()));
            }

            telemetry.update();
        }
    }
}
