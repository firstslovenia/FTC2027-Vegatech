package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Objects;

/// Provides non-volatile storage of data between opmodes
public class NVRAM {
    static String filename;

    static void save_string(String s) {
        try {
            File myFileName = AppUtil.getInstance().getSettingsFile(filename);
            ReadWriteFile.writeFile(myFileName, s);
        } catch (Exception e) {}
    }

    static String load_string() {
        try {
            File myFileName = AppUtil.getInstance().getSettingsFile(filename);
            return ReadWriteFile.readFile(myFileName);
        } catch (Exception e) {
            return "";
        }
    }

    static void delete_string() {
        try {
            File myFileName = AppUtil.getInstance().getSettingsFile(filename);

            if (myFileName != null) {
                myFileName.delete();
            }
        } catch (Exception e) {}
    }

    static void save_json(JSONObject json) {
        save_string(json.toString());
    }

    static JSONObject load_json() {
       try {
           String str = load_string();
           return new JSONObject(str);
       } catch (Exception e) {
           return null;
       }
    }

    public void save() {};
}
