package org.firstinspires.ftc.teamcode.opmodes.production.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.generic.Util;

@Autonomous(name = "Blue | Near", group = "Production")
public class BlueNearAuto extends GenericJustMoveAuto {
    {
        startPose = Util.mirror_x(Util.mirror_y(Constants.RED_AUTO_NEAR_START));
        endPose = Util.mirror_x(Util.mirror_y(Constants.RED_AUTO_NEAR_END));
    }
}
