package org.firstinspires.ftc.teamcode.opmodes.production.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.generic.Team;
import org.firstinspires.ftc.teamcode.generic.Util;

@TeleOp(name = "Main | Blue (NEAR)", group = "Production")
public class MainBlueNear extends Main {
    public MainBlueNear() {
        super();

        team = Team.Blue;
        starting_pos = Util.to_pose2d(Util.mirror_x(Util.mirror_y(Constants.RED_AUTO_NEAR_END)));
    }
}
