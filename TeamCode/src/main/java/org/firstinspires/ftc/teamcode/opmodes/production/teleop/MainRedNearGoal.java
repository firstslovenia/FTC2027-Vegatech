package org.firstinspires.ftc.teamcode.opmodes.production.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ShooterPositioning;
import org.firstinspires.ftc.teamcode.generic.Team;

@TeleOp(name = "Main | Red | Near Goal", group = "Production")
public class MainRedNearGoal extends Main {
    public MainRedNearGoal() {
        super();

        team = Team.Red;
    }
}
