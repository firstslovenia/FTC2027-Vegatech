package org.firstinspires.ftc.teamcode.generic;

public enum BallType {
    TeamColored,
    Pollen,
    None;

    public BallType other() {
        switch (this) {
            case Pollen:
                return TeamColored;
            case TeamColored:
                return Pollen;
            default:
                return None;
        }
    }
}
