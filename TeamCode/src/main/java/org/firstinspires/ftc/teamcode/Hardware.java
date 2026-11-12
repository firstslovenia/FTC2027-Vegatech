package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Hardware {

	private OpMode callingOpMode = null;

	//  Motor configuration:
	//  ╔════════════════════╗
	//  ║ \     front     /  ║
	//  ║ \  left  right  /  ║
	//  ║ \               /  ║
	//  ║                    ║
	//  ║ /     back      \  ║
	//  ║ /  left  right  \  ║
	//  ║ /               \  ║
	//  ╚════════════════════╝

	public static DcMotor frontLeftMotor = null;
	public static DcMotor frontRightMotor = null;
	public static DcMotor backLeftMotor = null;
	public static DcMotor backRightMotor = null;

    public static DcMotor leftEncoder = null;
    public static DcMotor rightEncoder = null;
    public static DcMotor sidewaysEncoder = null;

	public static DcMotor shooterMotor = null;

    public static DcMotor intakeMotor = null;
    public static DcMotor transferMotor = null;
    public static DcMotor turretMotor = null;
	public static Servo cameraAngleServo = null;

	public static IMU imu = null;
	public static Servo rgbLed = null;
    public static RevColorSensorV3 colorSensor = null;

	public static WebcamName webcam = null;
    public static double ODOMETRY_EPSILON = 0.0001;

	public Hardware (OpMode opmode) {
		callingOpMode = opmode;
	}

	public void init()    {
		frontLeftMotor = callingOpMode.hardwareMap.get(DcMotor.class, "frontLeftMotor");
		frontRightMotor = callingOpMode.hardwareMap.get(DcMotor.class, "frontRightMotor");
		backLeftMotor = callingOpMode.hardwareMap.get(DcMotor.class, "backLeftMotor");
		backRightMotor = callingOpMode.hardwareMap.get(DcMotor.class, "backRightMotor");

		frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

		// Make the positive direction on all motors +y
		// (This is assuming the motors spin clockwise and their directions aren't flipped by gears)
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        leftEncoder =  backRightMotor;
        rightEncoder = backLeftMotor;
        sidewaysEncoder = frontRightMotor;

        cameraAngleServo = callingOpMode.hardwareMap.get(Servo.class, "cameraAngleServo");

        intakeMotor = callingOpMode.hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterMotor = callingOpMode.hardwareMap.get(DcMotor.class, "shooterMotor");
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        turretMotor = callingOpMode.hardwareMap.get(DcMotor.class, "turretMotor");
        turretMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		imu = callingOpMode.hardwareMap.get(IMU.class, "imu");
		imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.DOWN, RevHubOrientationOnRobot.UsbFacingDirection.LEFT)));

		rgbLed = callingOpMode.hardwareMap.get(Servo.class, "rgbLed");

        // Rely primarily on odometry, use the camera to set the initial position; autonomous gets us reliable positioning anyway
        // To target stuff use calculated gobilda pos
		//webcam = callingOpMode.hardwareMap.get(WebcamName.class, "webcam");

        colorSensor = callingOpMode.hardwareMap.get(RevColorSensorV3.class, "colorSensor");
	}
}
