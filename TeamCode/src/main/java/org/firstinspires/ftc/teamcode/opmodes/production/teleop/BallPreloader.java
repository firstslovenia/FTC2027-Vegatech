package org.firstinspires.ftc.teamcode.opmodes.production.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.generic.LedIndicator;

@TeleOp(name = "Ball Preloading", group = "Production")
public class BallPreloader extends LinearOpMode {

    Hardware hardware;
    LedIndicator ledIndicator;

    boolean intake_enabled = false;

    @Override
    public void runOpMode() throws InterruptedException {

        hardware = new Hardware(this);
        hardware.init();

        ledIndicator = new LedIndicator(this, hardware);
        ledIndicator.setPosition(LedIndicator.OFF_POSITION);

        waitForStart();

        while (opModeIsActive()) {

            // Enable / disable intake
            if (gamepad1.xWasPressed()) {
                intake_enabled = !intake_enabled;

                if (intake_enabled) {
                    hardware.intakeMotor.setPower(1.0);
                } else {
                    hardware.intakeMotor.setPower(0.0);
                }
            }

            // User 2

            // Do spindexer intake
            if (gamepad1.yWasPressed()) {
            }

            // Get ready for match position
            if (gamepad1.aWasPressed()) {
            }


            telemetry.update();
        }
    }
}