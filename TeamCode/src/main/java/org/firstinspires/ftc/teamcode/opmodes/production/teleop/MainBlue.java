package org.firstinspires.ftc.teamcode.opmodes.production.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.ShooterPositioning;
import org.firstinspires.ftc.teamcode.generic.Team;
import org.firstinspires.ftc.teamcode.generic.Util;
import org.firstinspires.ftc.teamcode.opmodes.production.autonomous.BlueNearAuto;

@TeleOp(name = "Main | Blue (FAR)", group = "Production")
public class MainBlue extends Main {
    public MainBlue() {
        super();

        team = Team.Blue;
        starting_pos = Util.to_pose2d(Util.mirror_x(Util.mirror_y(Constants.RED_AUTO_FAR_END)));
    }
}
