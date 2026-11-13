package org.firstinspires.ftc.teamcode.opmodes.production.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.generic.Team;
import org.firstinspires.ftc.teamcode.generic.Util;

@TeleOp(name = "Main | Red (NEAR)", group = "Production")
public class MainRedNear extends Main {
    public MainRedNear() {
        super();

        team = Team.Blue;
        starting_pos = Util.to_pose2d(Constants.RED_AUTO_NEAR_END);
    }
}
