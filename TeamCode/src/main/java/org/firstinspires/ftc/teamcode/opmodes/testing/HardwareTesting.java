package org.firstinspires.ftc.teamcode.opmodes.testing;

import android.util.Pair;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.ShooterPlusPlus;
import org.firstinspires.ftc.teamcode.Turret;
import org.firstinspires.ftc.teamcode.generic.LedIndicator;

@TeleOp(name = "Hardware Testing", group = "Testing")
public class HardwareTesting extends LinearOpMode {

    Hardware hardware;

    Turret turret;
    HardwareComponent component = HardwareComponent.frontLeftMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        hardware = new Hardware(this);
        hardware.init();

        turret = new Turret(this, hardware);
        turret.init();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.aWasPressed()) {
                component = component.next();
            }
            if (gamepad1.bWasPressed()) {
                component = component.previous();
            }

            telemetry.addData("Component", component);

            double power = -gamepad1.right_stick_y;

            switch (component) {
                case frontLeftMotor:
                    hardware.frontLeftMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.frontLeftMotor.getCurrentPosition());
                    break;
                case frontRightMotor:
                    hardware.frontRightMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.frontRightMotor.getCurrentPosition());
                    break;
                case backLeftMotor:
                    hardware.backLeftMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.backLeftMotor.getCurrentPosition());
                    break;
                case backRightMotor:
                    hardware.backRightMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.backRightMotor.getCurrentPosition());
                    break;
                case shooterMotor:
                    hardware.shooterMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.shooterMotor.getCurrentPosition());
                    break;
                case intakeMotor:
                    hardware.intakeMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.intakeMotor.getCurrentPosition());
                    break;
                case transferMotor:
                    hardware.transferMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.transferMotor.getCurrentPosition());
                    break;
                case turretMotor:
                    hardware.turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    hardware.turretMotor.setPower(power);
                    telemetry.addData("Power", power);
                    telemetry.addData("Ticks", hardware.turretMotor.getCurrentPosition());
                    break;
                case turret:
                    android.util.Pair<Double, Double> mag_phi = Drivetrain.getMagnitudeAndPhiFor(gamepad1.right_stick_x, -gamepad1.right_stick_y);

                    double magnitude = mag_phi.first;
                    double phi = mag_phi.second;

                    if (magnitude >= 0.5) {
                        turret.move_to_angle(phi);
                    }

                   turret.update();
                case cameraAngleServo:

                    double servo_pos = hardware.cameraAngleServo.getPosition();

                    telemetry.addData("servo position", servo_pos);

                    if (gamepad1.xWasPressed()) {
                        servo_pos = servo_pos + 0.1;
                    } else if (gamepad1.yWasPressed()) {
                        servo_pos = servo_pos - 0.1;
                    }

                    servo_pos = Math.min(Math.max(0.0, servo_pos), 1.0);

                    hardware.cameraAngleServo.setPosition(servo_pos);


                case rgbLed:
                    double color = LedIndicator.RED_POSITION + (LedIndicator.VIOLET_POSITION - LedIndicator.RED_POSITION) * ((double)(System.currentTimeMillis() % 10000) / 10000.0);

                    hardware.rgbLed.setPosition(color);
                    telemetry.addData("Position", color);
                    break;
                case colorSensor:
                    NormalizedRGBA output = hardware.colorSensor.getNormalizedColors();
                    double distance = hardware.colorSensor.getDistance(DistanceUnit.CM);

                    telemetry.addData("Distance [cm]", distance);
                    telemetry.addData("R", output.red);
                    telemetry.addData("G", output.green);
                    telemetry.addData("B", output.blue);
                    telemetry.addData("A", output.alpha);
            }

            telemetry.update();
        }
    }
}
