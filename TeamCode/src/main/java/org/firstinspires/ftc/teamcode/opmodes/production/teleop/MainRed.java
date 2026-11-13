package org.firstinspires.ftc.teamcode.opmodes.production.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.ShooterPositioning;
import org.firstinspires.ftc.teamcode.generic.Team;
import org.firstinspires.ftc.teamcode.generic.Util;

@TeleOp(name = "Main | Red (FAR)", group = "Production")
public class MainRed extends Main {
    public MainRed() {
        super();

        team = Team.Red;
        starting_pos = Util.to_pose2d(Constants.RED_AUTO_FAR_END);
    }
}
