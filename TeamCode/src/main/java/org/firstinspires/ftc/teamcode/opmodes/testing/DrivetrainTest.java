package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.NVPosition;
import org.firstinspires.ftc.teamcode.generic.Util;
import org.firstinspires.ftc.teamcode.generic.Vector2D;

@TeleOp(name = "Drivetrain Test", group = "Testing")
public class DrivetrainTest extends LinearOpMode {

	Hardware hardware;
	Drivetrain drivetrain;

	@Override
	public void runOpMode() {

		hardware = new Hardware(this);
		hardware.init();

        drivetrain = new Drivetrain(this, hardware);
        drivetrain.fieldCentricTranslation = true;
        drivetrain.fieldCentricRotation = true;
        drivetrain.setPos(new Pose2D(DistanceUnit.METER, 0.0, 0.0, AngleUnit.RADIANS,0.0));
        drivetrain.keepHeading = true;

        Pose2D starting_pos = null;

        // Load saved non-volatile position
        NVPosition pos = NVPosition.load();

        if (pos != null) {
            starting_pos = Util.to_pose2d(pos.pos);
        }

		waitForStart();

        if (starting_pos != null) {
            drivetrain.setPos(starting_pos);
            drivetrain.resetStartingDirection();
        }

		while (opModeIsActive()) {

			Vector2D translation_vector = new Vector2D(gamepad1.left_stick_x, -gamepad1.left_stick_y);
			Vector2D rotation_vector = new Vector2D(gamepad1.right_stick_x, -gamepad1.right_stick_y);

			if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right) {
				translation_vector = new Vector2D(0.0, 0.0);

				if (gamepad1.dpad_up) { translation_vector.y += 0.65; }
				if (gamepad1.dpad_down) { translation_vector.y -= 0.65; }
				if (gamepad1.dpad_right) { translation_vector.x += 0.65; }
				if (gamepad1.dpad_left) { translation_vector.x -= 0.65; }
			}

			if (gamepad1.a) {
				drivetrain.fieldCentricRotation = true;
			}

			if (gamepad1.b) {
				drivetrain.fieldCentricRotation = false;
			}

			if (gamepad1.x) {
				drivetrain.fieldCentricTranslation = true;
			}

			if (gamepad1.y) {
				drivetrain.fieldCentricTranslation = false;
			}

			if (gamepad1.guideWasPressed()) {
				drivetrain.resetStartingDirection();
			}

			drivetrain.update(translation_vector, rotation_vector);

            if (starting_pos != null) {
                telemetry.addLine("starting ni null");
            }
			telemetry.update();
		}
	}
}
