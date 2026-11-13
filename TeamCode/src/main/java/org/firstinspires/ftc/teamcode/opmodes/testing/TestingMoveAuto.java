package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.production.autonomous.GenericJustMoveAuto;

@Autonomous(name = "Testing Auto", group = "Examples")
public class TestingMoveAuto extends GenericJustMoveAuto {
    public TestingMoveAuto() {
        this.startPose = new Pose(0.0, 0.0, Math.PI / 2.0);
        this.distance_cm = 50.0;
    }
}
