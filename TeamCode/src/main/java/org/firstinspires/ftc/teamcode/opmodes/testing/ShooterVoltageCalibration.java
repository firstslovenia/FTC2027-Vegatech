package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.ShooterPlusPlus;

@TeleOp(name = "Shooter++ Voltage Calibration", group = "Testing")
public class ShooterVoltageCalibration extends LinearOpMode {

    Hardware hardware;
    ShooterPlusPlus shooter;

    @Override
    public void runOpMode() {

        telemetry.setMsTransmissionInterval(100);

        hardware = new Hardware(this);
        hardware.init();

        shooter = new ShooterPlusPlus(this, hardware);
        shooter.automatically_shoot = false;

        waitForStart();

        shooter.run();
        shooter.wanted_flywheel_rpm = 3000000;

        while (opModeIsActive()) {
            shooter.update();

            telemetry.addData("RPMs", shooter.last_rpm_measurements.average().orElse(0.0));
            telemetry.addData("Power", hardware.shooterMotor.getPower());

            telemetry.update();
        }
    }
}
