package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.generic.Vector2D;

@TeleOp(name = "Dumb Shooter Test", group = "Testing")
public class DumbShooterTest extends LinearOpMode {

    Hardware hardware;
    @Override
    public void runOpMode() {

        hardware = new Hardware(this);
        hardware.init();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.aWasPressed()) {
                if (hardware.shooterMotor.getPower() == 0.0) {
                    hardware.shooterMotor.setPower(1.0);
                } else {
                    hardware.shooterMotor.setPower(0.0);
                }
            }

            if (gamepad1.bWasPressed()) {
                if (hardware.transferMotor.getPower() == 0.0) {
                    hardware.transferMotor.setPower(1.0);
                } else {
                    hardware.transferMotor.setPower(0.0);
                }
            }

            if (gamepad1.xWasPressed()) {
                if (hardware.intakeMotor.getPower() == 0.0) {
                    hardware.intakeMotor.setPower(1.0);
                } else {
                    hardware.intakeMotor.setPower(0.0);
                }
            }

            telemetry.update();
        }
    }
}
