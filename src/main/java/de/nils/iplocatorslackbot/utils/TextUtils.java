package de.nils.iplocatorslackbot.utils;

import java.util.List;

public class TextUtils {
    public static String na(String text) {
        return (text == null || text.isEmpty()) ? "n/a" : text;
    }

    public static String na(int num) {
        return (num == 0) ? "n/a" : String.valueOf(num);
    }

    public static String coords(double lat, double lon) {
        return (lat == 0.0 && lon == 0.0) ? "n/a" : lat + ", " + lon;
    }

    public static String hostnames(List<String> list) {
        return (list == null || list.isEmpty())
                ? "n/a"
                : String.join("\n", list);
    }
}
