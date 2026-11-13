package org.firstinspires.ftc.teamcode.opmodes.production.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Constants;

@Autonomous(name = "Red | Near", group = "Production")
public class RedNearAuto extends GenericJustMoveAuto {
    {
        startPose = Constants.RED_AUTO_NEAR_START;
        endPose = Constants.RED_AUTO_NEAR_END;
    }
}
