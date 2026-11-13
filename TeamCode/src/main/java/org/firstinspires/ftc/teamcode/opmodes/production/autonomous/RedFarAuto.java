package org.firstinspires.ftc.teamcode.opmodes.production.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;

@Autonomous(name = "Red | Far", group = "Production")
public class RedFarAuto extends GenericJustMoveAuto {
    {
        startPose = Constants.RED_AUTO_FAR_START;
        endPose = Constants.RED_AUTO_FAR_END;
    }
}
