package org.firstinspires.ftc.teamcode.opmodes.production.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.generic.Util;

@Autonomous(name = "Blue | Far", group = "Production")
public class BlueFarAuto extends GenericJustMoveAuto {
    {
        startPose = Util.mirror_x(Util.mirror_y(Constants.RED_AUTO_FAR_START));
        endPose = Util.mirror_x(Util.mirror_y(Constants.RED_AUTO_FAR_END));
    }
}
