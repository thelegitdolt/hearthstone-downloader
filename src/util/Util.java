package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {
    public static String idFromFile(String str) {
        return str.substring(str.lastIndexOf(" ") + 1, str.length() - 4);
    }


    public static String idFromFile(File file) {
        return idFromFile(file.getName());
    }



    /**
     * turns a String into a list of characters.
     */
    private static List<Character> toCharList(String str) {
        List<Character> charList = new ArrayList<>();
        for (char car : str.toCharArray()) {
            charList.add(car);
        }
        return charList;
    }

    /**
     * Takes a list of single-digit numbers and displays them as a number.
     */
    public static int getNumberFromIntList(List<Integer> list) {

        List<Integer> tmpList = new ArrayList<>(list);
        Collections.reverse(tmpList);

        int number = 0;
        for (int i = 0; i < tmpList.size(); i++) {
            number += list.get(i) * Math.pow(10, i);
        }
        return number;
    }






    private static BufferedImage getArtImageApprox(BufferedImage img) {
        int x, y, w, h;
        x = img.getWidth() / 3;
        y = img.getHeight() / 5;
        w = img.getWidth() / 3;
        h = (int) (img.getHeight() / 6.5);

        return img.getSubimage(x, y, w, h);
    }

}


