package org.firstinspires.ftc.teamcode.opmodes.production.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware;

@TeleOp(name = "Reset Odometry", group = "Production")
public class ResetOdometry extends LinearOpMode {
    Hardware hardware;

    long started = System.currentTimeMillis();

    @Override
    public void runOpMode() {

        hardware = new Hardware(this);
        hardware.init();

        waitForStart();

        // nared magic da clearaš pos
    }
}
