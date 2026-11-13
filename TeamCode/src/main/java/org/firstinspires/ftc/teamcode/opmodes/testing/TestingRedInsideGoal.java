package org.firstinspires.ftc.teamcode.opmodes.testing;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.generic.Team;
import org.firstinspires.ftc.teamcode.generic.Util;
import org.firstinspires.ftc.teamcode.opmodes.production.teleop.Main;

@TeleOp(name = "Testing Red inside goal", group = "Testing")
public class TestingRedInsideGoal extends Main {
    {
        team = Team.Red;
        starting_pos = Util.to_pose2d(Constants.LOWER_TARGET_CENTER_RED);
    }
}
