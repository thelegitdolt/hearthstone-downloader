package util;

import datafixers.Pair;

public class HSStringUtil {

    public static String trimChar(String str, int num) {
        return new StringBuffer(str).deleteCharAt(str.length() - num).toString();
    }

    public static String trimChar(String str) {
        return trimChar(str, 1);
    }


    public static Pair<String, String> extractLine(String entry) {
        entry = entry.substring(2, entry.length() - 1);
        String word = entry.substring(0, entry.indexOf("\""));
        String value = entry.substring(word.length() + 3);

        if (entry.lastIndexOf("\"") == entry.length() - 1) {
            value = HSStringUtil.trimChar(value);
            value = value.substring(1);
        } else if (value.charAt(0) == '\"') {
            value = value.substring(1);
        }

        return Pair.of(word, value);
    }
}
