package org.firstinspires.ftc.teamcode.opmodes.production.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.NVPosition;

@TeleOp(name = "Reset Odometry", group = "Production")
public class ResetOdometry extends LinearOpMode {
    Hardware hardware;

    long started = System.currentTimeMillis();

    @Override
    public void runOpMode() {

        hardware = new Hardware(this);
        hardware.init();

        waitForStart();

        hardware.leftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.rightEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.sidewaysEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        NVPosition.clear();
    }
}
