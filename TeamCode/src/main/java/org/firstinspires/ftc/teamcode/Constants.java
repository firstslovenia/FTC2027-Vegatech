package org.firstinspires.ftc.teamcode;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.generic.Util;

public class Constants {
	public static FollowerConstants followerConstants = new FollowerConstants()
            // Ish, update me please!
            .mass(14.6)
            .forwardZeroPowerAcceleration(-36.940776023051384)
            .lateralZeroPowerAcceleration(-80.24074184452753);

	public static MecanumConstants driveConstants = new MecanumConstants()
		.maxPower(1)
		.rightFrontMotorName("frontRightMotor")
		.rightRearMotorName("backRightMotor")
		.leftFrontMotorName("frontLeftMotor")
		.leftRearMotorName("backLeftMotor")
        // Note: the defaults here will mess up everything you've already configured! Pedro is too smart
		.rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
		.rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
        .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
        .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
        .xVelocity(52.085921878882594)
        .yVelocity(40.12158909623925);

    public static ThreeWheelIMUConstants localizerConstants = new ThreeWheelIMUConstants()
            .forwardTicksToInches(8.888423665179236E-4)
            .strafeTicksToInches(8.888423665179236E-4)
            .turnTicksToInches(8.888423665179236E-4)
            .leftPodY(6.5)
            .rightPodY(-6.5)
            .strafePodX(-8.0)
            .leftEncoder_HardwareMapName("backRightMotor")
            .rightEncoder_HardwareMapName("backLeftMotor")
            .strafeEncoder_HardwareMapName("frontRightMotor")
            // Apparently ok?
            //.leftEncoderDirection(Encoder.FORWARD)
            .rightEncoderDirection(Encoder.REVERSE)
            .strafeEncoderDirection(Encoder.REVERSE)
            .IMU_HardwareMapName("imu")
            .IMU_Orientation(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.DOWN, RevHubOrientationOnRobot.UsbFacingDirection.LEFT));

	public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

	public static Follower createFollower(HardwareMap hardwareMap) {
		return new FollowerBuilder(followerConstants, hardwareMap)
			.pathConstraints(pathConstraints)
			.mecanumDrivetrain(driveConstants)
			.threeWheelIMULocalizer(localizerConstants)
			.build();
	}

    public static Pose LOWER_TARGET_LEFT_RED = new Pose(50, 54);
    public static Pose LOWER_TARGET_CENTER_RED = new Pose(57.75, 54);
    public static Pose LOWER_TARGET_RIGHT_RED = new Pose(65.5, 54);

    public static Pose UPPER_TARGET_LEFT_RED = Util.mirror_y(LOWER_TARGET_LEFT_RED);
    public static Pose UPPER_TARGET_CENTER_RED = Util.mirror_y(LOWER_TARGET_CENTER_RED);
    public static Pose UPPER_TARGET_RIGHT_RED = Util.mirror_y(LOWER_TARGET_RIGHT_RED);

    public static Pose LOWER_TARGET_LEFT_BLUE = Util.mirror_x(LOWER_TARGET_LEFT_RED);
    public static Pose LOWER_TARGET_CENTER_BLUE = Util.mirror_x(LOWER_TARGET_CENTER_RED);
    public static Pose LOWER_TARGET_RIGHT_BLUE = Util.mirror_x(LOWER_TARGET_RIGHT_RED);
    public static Pose UPPER_TARGET_LEFT_BLUE = Util.mirror_y(LOWER_TARGET_LEFT_BLUE);
    public static Pose UPPER_TARGET_CENTER_BLUE = Util.mirror_y(LOWER_TARGET_CENTER_BLUE);
    public static Pose UPPER_TARGET_RIGHT_BLUE = Util.mirror_y(LOWER_TARGET_RIGHT_BLUE);

    public static Pose get_target_for_pos(Pose robot) {
        if (robot.getX() >= Util.CORNER_POSE.getX()/2.0) {
            // Blue
            if (robot.getY() >= Util.CORNER_POSE.getY()/2.0) {
                // Upper
                if (robot.getX() >= UPPER_TARGET_CENTER_BLUE.getX()) {
                    // On the right, point left
                    return UPPER_TARGET_LEFT_BLUE;
                } else {
                    // On the left, point right
                    return UPPER_TARGET_RIGHT_BLUE;
                }
            } else {
                // Lower
                if (robot.getX() >= LOWER_TARGET_CENTER_BLUE.getX()) {
                    return LOWER_TARGET_LEFT_BLUE;
                } else {
                    return LOWER_TARGET_RIGHT_BLUE;
                }
            }
        } else {
            // red
            if (robot.getY() >= Util.CORNER_POSE.getY()/2.0) {
                // Upper
                if (robot.getX() >= UPPER_TARGET_CENTER_RED.getX()) {
                    // On the right, point left
                    return UPPER_TARGET_LEFT_RED;
                } else {
                    // On the left, point right
                    return UPPER_TARGET_RIGHT_RED;
                }
            } else {
                // Lower
                if (robot.getX() >= LOWER_TARGET_CENTER_RED.getX()) {
                    // On the right, point left
                    return LOWER_TARGET_LEFT_RED;
                } else {
                    // On the left, point right
                    return LOWER_TARGET_RIGHT_RED;
                }
            }
        }
    }
}
