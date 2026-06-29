package de.nils.iplocatorslackbot.utils;

import java.util.Iterator;
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
        if (list == null || list.isEmpty()) {
            return "n/a";
        }

        StringBuilder sb = new StringBuilder();
        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            String hostname = it.next();
            sb.append("• ").append(hostname);
            if (it.hasNext()) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
