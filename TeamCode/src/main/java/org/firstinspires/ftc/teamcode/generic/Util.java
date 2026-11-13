package org.firstinspires.ftc.teamcode.generic;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Util {
    /// Converts a pedro pathing pose to a pose2d
    public static Pose2D to_pose2d(Pose pedro_pos) {
        return new Pose2D(DistanceUnit.INCH, pedro_pos.getX(), pedro_pos.getY(), AngleUnit.RADIANS, pedro_pos.getHeading());
    }

    /// Converts a pedro pathing pose to a pose2d
    public static Pose to_pose(Pose2D pedro_pos) {
        return new Pose(pedro_pos.getX(DistanceUnit.INCH), pedro_pos.getY(DistanceUnit.INCH), pedro_pos.getHeading(AngleUnit.RADIANS));
    }

    /// Returns if two positions are roughly the same
    public static boolean pose2d_rougly_eq(Pose2D a, Pose2D b) {
        return Math.abs(a.getX(DistanceUnit.METER) - b.getX(DistanceUnit.METER)) < 0.2 && Math.abs(a.getY(DistanceUnit.METER) - b.getY(DistanceUnit.METER)) < 0.2 && Math.abs(a.getHeading(AngleUnit.DEGREES) - b.getHeading(AngleUnit.DEGREES)) < 10;
    }

    // Returns if two positions are roughly the same
    public static boolean pose_rougly_eq(Pose a, Pose b) {
        return pose2d_rougly_eq(to_pose2d(a), to_pose2d(b));
    }}
