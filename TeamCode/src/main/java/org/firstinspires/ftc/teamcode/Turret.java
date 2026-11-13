package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.generic.AngleUtil;

public class Turret {

    OpMode callingOpMode;
    Hardware hardware;

    /// How many encoder counts mean one revolution on the output axle
    /// 3:1, 4:1, 1:5
    public static int TICKS_PER_REVOLUTION = (int) (28.0 * (3.0 * 4.0 / 5.0));

    /// How many encoder ticks (here we calculate it from degrees) we allow the PID controller to miss
    public static int PID_TOLERANCE = (TICKS_PER_REVOLUTION * 6) / 360;
    public static double TOLERANCE_RADS = 5.0 * Math.PI / 180.0;

    public static int TOLERANCE_TICKS = (int) Math.ceil(TOLERANCE_RADS/(Math.PI * 2.0) * TICKS_PER_REVOLUTION);

    public static double STARTING_ANGLE = Math.PI / 2.0;

    /// Pid coefficient when we are normally operating the turret
    public static double NORMAL_PID_COEFFICIENT = 8.0;
    public static double MOTOR_POWER = 0.5;

    /// What time in millis the motor arrived at the correct position
    public Long stopped_being_busy_ms = null;

    /// Whether or not we were busy in the last loop, used to set stopped_being_busy_ms
    boolean last_was_busy = false;

    public Turret(OpMode callingOpMode, Hardware hardware) {
        this.callingOpMode = callingOpMode;
        this.hardware = hardware;
    }

    public void init() {

        // Anti-tweaking / stuck mechanism
        if (callingOpMode.hardwareMap.voltageSensor.iterator().next().getVoltage() >= 12.7) {
            MOTOR_POWER *= 0.8;
        }

        DcMotorEx ex = (DcMotorEx) hardware.turretMotor;
        ex.setTargetPositionTolerance(PID_TOLERANCE);
        ex.setPositionPIDFCoefficients(NORMAL_PID_COEFFICIENT);

        hardware.turretMotor.setPower(MOTOR_POWER);
        hardware.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.turretMotor.setTargetPosition(0);
    }

    public void set_normal_tolerance() {
        DcMotorEx ex = (DcMotorEx) hardware.turretMotor;
        ex.setTargetPositionTolerance(PID_TOLERANCE);
        ex.setPositionPIDFCoefficients(NORMAL_PID_COEFFICIENT);
    }

    public void update() {

        long now_ms = System.currentTimeMillis();
        boolean is_busy = is_motor_busy();

        if (stopped_being_busy_ms == null && !is_busy && last_was_busy) {
            stopped_being_busy_ms = now_ms;
        }

        if (is_busy) {
            stopped_being_busy_ms = null;
        }

        last_was_busy = is_busy;
    }

    /// Tells the turret to move to the set angle
    public void move_to_angle(double angle_rads) {
        int current_pos = hardware.turretMotor.getCurrentPosition();

        int cw_pos = nearest_cw_pos_at_angle(current_pos, angle_rads);
        int ccw_pos = nearest_cw_pos_at_angle(current_pos, angle_rads);

        double cw_twisting = Math.abs(loops(cw_pos));
        double ccw_twisting = Math.abs(loops(cw_pos));

        if (cw_twisting < ccw_twisting) {
            move_to_pos(cw_pos);
        } else {
            move_to_pos(ccw_pos);
        }
    }

    /// Tells the turret to move to the set angle clockwise
    public void move_to_angle_cw(double angle_rads) {

        if (Math.abs(target_angle() - angle_rads) > TOLERANCE_RADS) {
            int encoder_pos = hardware.turretMotor.getCurrentPosition();
            hardware.turretMotor.setTargetPosition(nearest_cw_pos_at_angle(encoder_pos, angle_rads));
            stopped_being_busy_ms = null;
        }

        hardware.turretMotor.setPower(MOTOR_POWER);
        hardware.turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /// Tells the turret to move to the set angle counterclockwise
    public void move_to_angle_ccw(double angle_rads) {

        if (Math.abs(target_angle() - angle_rads) > TOLERANCE_RADS) {
            int encoder_pos = hardware.turretMotor.getCurrentPosition();
            hardware.turretMotor.setTargetPosition(nearest_ccw_pos_at_angle(encoder_pos, angle_rads));
            stopped_being_busy_ms = null;
        }

        hardware.turretMotor.setPower(MOTOR_POWER);
        hardware.turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /// Tells the turret to move to the set position
    public void move_to_pos(int pos) {

        int current_pos = hardware.turretMotor.getCurrentPosition();

        int pos_diff = Math.abs(current_pos - pos);

        if (pos_diff < TOLERANCE_TICKS) {
            // already there :D
            return;
        }

        // SAFETY: don't do sheiße
        if (pos_diff > TICKS_PER_REVOLUTION) {
            return;
        }

        hardware.turretMotor.setTargetPosition(pos);
        stopped_being_busy_ms = null;

        hardware.turretMotor.setPower(MOTOR_POWER);
        hardware.turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public double current_angle() {
        return calculate_current_angle(hardware.turretMotor.getCurrentPosition());
    }

    public double target_angle() {
        return calculate_current_angle(hardware.turretMotor.getTargetPosition());
    }

    public boolean is_motor_busy() {
        return hardware.turretMotor.isBusy() || Math.abs(hardware.turretMotor.getCurrentPosition() - hardware.turretMotor.getTargetPosition()) > PID_TOLERANCE;
    }

    /// Returns how many loops a given position has accumulated (as an integer, floored)
    public static int int_loops(int encoder_position) {
        return encoder_position / TICKS_PER_REVOLUTION;
    }

    /// Returns how many loops a given position has accumulated (as a decimal value)
    public static double loops(int encoder_position) {
        return (double) encoder_position / TICKS_PER_REVOLUTION;
    }

    /// Returns how far (via ticks) we are into the current loop
    public static int in_loop_ticks(int encoder_position) {
        int moved_since_zero = encoder_position;
        return Math.floorMod(moved_since_zero, TICKS_PER_REVOLUTION);
    }

    /// Computes the nearest position to the given position which puts the turret into the wanted angle, going clockwise
    public static int nearest_cw_pos_at_angle(int encoder_position, double angle_rads) {

        double current_angle_rads = calculate_current_angle(encoder_position);
        double angle_diff = AngleUtil.ensure_negative_diff(current_angle_rads, angle_rads);

        int angle_diff_ticks = (int) ((angle_diff / Math.PI / 2.0) * TICKS_PER_REVOLUTION);
        return encoder_position + angle_diff_ticks;
    }

    /// Computes the nearest position to the given position which puts the turret into the wanted angle, going counterclockwise
    public static int nearest_ccw_pos_at_angle(int encoder_position, double angle_rads) {

        double current_angle_rads = calculate_current_angle(encoder_position);
        double angle_diff = AngleUtil.ensure_positive_diff(current_angle_rads, angle_rads);

        int angle_diff_ticks = (int) ((angle_diff / Math.PI / 2.0) * TICKS_PER_REVOLUTION);
        return encoder_position + angle_diff_ticks;
    }

    /// Calculate the current angle of the turret
    ///
    /// Angle 0 means that grey parts and orange parts on the turret
    /// and walls are lined up, and the connecting line on the turret is
    /// dead center in the shooter pusher servo hole
    ///
    /// Always returns positive angles
    public static double calculate_current_angle(int encoder_pos) {
        return (double) in_loop_ticks(encoder_pos) / TICKS_PER_REVOLUTION * Math.PI * 2.0;
    }
}