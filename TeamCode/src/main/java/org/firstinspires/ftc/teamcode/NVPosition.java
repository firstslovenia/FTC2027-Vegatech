package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

/// Saves the robot's position inbetween opmodes
public class NVPosition extends NVRAM {

    public NVPosition(Pose p) {
       filename = "saved_pos.txt";
       saved_at = System.currentTimeMillis();
       pos = p;
    }

    public Pose pos;
    public long saved_at;

    @Override
    public void save() {
        StringBuffer str = new StringBuffer();
        write_long(str, saved_at);
        write_double(str, pos.getX());
        write_double(str, pos.getY());
        write_double(str, pos.getHeading());
        saved_at = System.currentTimeMillis();
        save_string(str.toString());
    }

    public static NVPosition load() {
        String s = load_string();

        if (s == null || s.equals("")) {
            return null;
        }

        StringBuffer b = new StringBuffer(s);

        Long s_at = read_long(b);
        Double x = read_double(b);
        Double y = read_double(b);
        Double h = read_double(b);

        if (s_at == null || x == null || y == null || h == null) {
            return null;
        }

        NVPosition pos = new NVPosition(new Pose(x, y, h));
        pos.saved_at = s_at;
        return pos;
    }
}
