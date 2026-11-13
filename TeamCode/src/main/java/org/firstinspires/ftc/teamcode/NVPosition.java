package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/// Saves the robot's position inbetween opmodes
public class NVPosition extends NVRAM {

    {
        filename = "saved_pos.txt";
    }

    public NVPosition(Pose p) {
       saved_at = System.currentTimeMillis();
       pos = p;
    }

    public Pose pos;
    public long saved_at;

    @Override
    public void save() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("s_at", saved_at);
        map.put("x", pos.getX());
        map.put("y", pos.getY());
        map.put("h", pos.getHeading());

        JSONObject j = new JSONObject();

        save_json(j);
    }

    public static NVPosition load() {
        try {
            JSONObject j = load_json();

            if (j == null) {
                return null;
            }

            long s_at = j.getLong("s_at");
            double x = j.getDouble("x");
            double y = j.getDouble("y");
            double h = j.getDouble("h");

            NVPosition pos = new NVPosition(new Pose(x, y, h));
            pos.saved_at = s_at;
            return pos;
        } catch (Exception e) {
            return null;
        }
    }

    public static void clear() {
        delete_string();
    }
}
