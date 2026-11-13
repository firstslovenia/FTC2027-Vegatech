package org.firstinspires.ftc.teamcode.opmodes.production.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.NVPosition;
import org.firstinspires.ftc.teamcode.ShooterPlusPlus;
import org.firstinspires.ftc.teamcode.ShooterPositioning;
import org.firstinspires.ftc.teamcode.TargetInformation;
import org.firstinspires.ftc.teamcode.Webcam;
import org.firstinspires.ftc.teamcode.opmodes.production.teleop.Main;

@Disabled
@Autonomous(name = "NO TOUCHIE!", group = "Examples")
public class GenericJustMoveAuto extends OpMode {

    private Follower follower;
    private Timer opmodeTimer, pathTimer;

    private int pathState;

    // Must be set by derived classes!!!!
    public Pose startPose;

    // if endpose is set, we'll move there; if distance is set, we'll just move forward for that distance.
    public Pose endPose = null;
    public Double distance_cm = null;

    private PathChain move;


    Hardware hardware;

    public void buildPaths() {

        Pose true_end_pose = null;

        if (endPose != null) {
            true_end_pose = endPose;
        } else if (distance_cm != null) {
            double dx = Math.cos(startPose.getHeading())*(distance_cm/2.54);
            double dy = Math.sin(startPose.getHeading())*(distance_cm/2.54);

            true_end_pose = new Pose(startPose.getX() + dx, startPose.getY() + dy, startPose.getHeading());
        }

        move = follower.pathBuilder()
                .addPath(new BezierLine(startPose, true_end_pose))
                .setConstantHeadingInterpolation(startPose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(move, true);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy() || opmodeTimer.getElapsedTime() >= 20000) {
                    setPathState(2);
                }
                break;

            case 2:
                follower.breakFollowing();
                NVPosition pos = new NVPosition(follower.getPose());
                pos.save();
                break;
        }
    }

    /**
     * These change the states of the paths and actions. It will also reset the timers of the individual switches
     **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        opmodeTimer = new Timer();
        pathTimer = new Timer();

        hardware = new Hardware(this);
        hardware.init();
    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop() {
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
        NVPosition pos = new NVPosition(follower.getPose());
        pos.save();
    }
}