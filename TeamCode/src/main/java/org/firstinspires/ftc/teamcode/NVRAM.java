package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Objects;

/// Provides non-volatile storage of data between opmodes
public class NVRAM {
    static String filename;

    static Character control = '\u0002';
    static Character delim = '\u0001';

    static String empty_str = "<empty>";
    static String null_str = "<null>";

    static void save_string(String s) {
        File myFileName = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(myFileName, s);
    }

    static String load_string() {
        File myFileName = AppUtil.getInstance().getSettingsFile(filename);
        return ReadWriteFile.readFile(myFileName);
    }

    static void write_line(StringBuffer b, String pfx, Object val) {
        b.append(delim);
        b.append(pfx);
        b.append(control);
        b.append(val.toString());
    }

    /// Returns {consumed, key, val}
    static String[] read_line(String b) {
        if (!b.startsWith(delim.toString())) {
            return new String[] {};
        }
        String rest = b.substring(1);

        int end_key = 0;
        int start_val = 0;
        int end_val = 0;

        for (int i = 0; i < rest.length(); i++) {
            if (rest.charAt(i) == control) {
               end_key = i;
               start_val = i + 1;
            }

            else if (rest.charAt(i) == delim) {
                end_val = i;
            }
        }

        String key = rest.substring(1, end_key);
        String value = rest.substring(start_val, end_val);
        String consumed = rest.substring(0, end_val);

        return new String[] {consumed, key, value};
    }

    static void write_string(StringBuffer b, String s) {
        if (s == null) {
            s = null_str;
        }
        if (s.equals("")) {
            s = empty_str;
        }

        write_line(b, "s", s);
    }

    static void write_int(StringBuffer b, int i) {
        write_line(b, "i", i);
    }

    static void write_long(StringBuffer b, long l) {
        write_line(b, "l", l);
    }

    static void write_float(StringBuffer b, float f) {
        write_line(b, "f", f);
    }

    static void write_double(StringBuffer b, double d) {
        write_line(b, "d", d);
    }

    /// Returns (key, val)
    static String[] read_val(StringBuffer b) {
        String[] ret = read_line(b.toString());

        if (ret == null || ret.length != 3) {
            return null;
        }

        // Remove consumed
        b.replace(0, ret[0].length(), "");

        return new String[] {ret[1], ret[2]};
    }

    static String read_string(StringBuffer b) {
        String[] ret = read_val(b);

        if (ret == null || ret.length != 2) {
            return null;
        }

        if (!ret[0].equals("s")) {
            return null;
        }

        if (ret[1].equals(empty_str)) {
            return "";
        }

        if (ret[1].equals(null_str)) {
            return null;
        }

        return ret[1];
    }

    static Integer read_int(StringBuffer b) {
        String[] ret = read_val(b);

        if (ret == null || ret.length != 2) {
            return null;
        }

        if (!ret[0].equals("i")) {
            return null;
        }

        try {
            return Integer.valueOf(ret[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Long read_long(StringBuffer b) {
        String[] ret = read_val(b);

        if (ret == null || ret.length != 2) {
            return null;
        }

        if (!ret[0].equals("i")) {
            return null;
        }

        try {
            return Long.valueOf(ret[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Float read_float(StringBuffer b) {
        String[] ret = read_val(b);

        if (ret == null || ret.length != 2) {
            return null;
        }

        if (!ret[0].equals("i")) {
            return null;
        }

        try {
            return Float.valueOf(ret[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Double read_double(StringBuffer b) {
        String[] ret = read_val(b);

        if (ret == null || ret.length != 2) {
            return null;
        }

        if (!ret[0].equals("i")) {
            return null;
        }

        try {
            return Double.valueOf(ret[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void save() {};
}
