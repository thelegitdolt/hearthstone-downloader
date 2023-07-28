package util;

import values.CardSet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    public static final String READTXT_FILEPATH = "/Users/drew/Desktop/Hearthstone.txt";
    public static final String CARD_FOLDER_FILEPATH = "/Users/drew/Desktop/Hearthstone Cards";


    public static final CardSet[] MERCENARY_SETS = new CardSet[]{
            CardSet.LETTUCE,
    };

    public static String trimChar(String str, int num) {
        return new StringBuffer(str).deleteCharAt(str.length() - num).toString();
    }



    public static Pair<String, String> extractLine(String entry) {
        entry = entry.substring(2, entry.length() - 1);
        String word = entry.substring(0, entry.indexOf("\""));
        String value = entry.substring(word.length() + 3);

        if (entry.lastIndexOf("\"") == entry.length() - 1) {
            value = Util.trimChar(value);
            value = value.substring(1);
        }
        else if (value.charAt(0) == '\"') {
            value = value.substring(1);
        }

        return Pair.of(word, value);
    }

    public static String trimChar(String str) {
        return trimChar(str, 1);
    }

    /**
     * A method that applies an action to all non-directory files in a folder.
     * @param path - the path of the file, as a String
     * @param action - the action to undertake
     */
    public static void mapToDirectory(String path, Lambdas.FileConsumer action) {
        mapToDirectory(new File(path), action);
    }

    /**
     * A method that applies an action to all non-directory files in a folder.
     * @param directory - directory, as a File
     * @param action - the action to undertake
     */
    public static void mapToDirectory(File directory, Lambdas.FileConsumer action) {
        if (!directory.isDirectory())
            return;

        Set<File> files = Stream.of(Objects.requireNonNull(directory.listFiles()))
                .filter(file -> !file.isDirectory()).collect(Collectors.toSet());

        for (File file : files) {
            try {
                action.apply(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Takes a file and trims it so there is no extraneous transparent outer layer.
     * @param file to trim, must be an image
     * @throws IOException if that file is not an image or does not exist
     */
    public static void trimTransparentOutside(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int x, y, w, h;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> rect = getPixelLimit(image);



        x = rect.getFirst().getFirst();
        y = rect.getSecond().getFirst();
        w = rect.getFirst().getSecond() - rect.getFirst().getFirst();
        h = rect.getSecond().getSecond() - rect.getSecond().getFirst();

        if (x < 0 || y < 0)
            return;
        if (x >= image.getWidth() || y >= image.getHeight())
            return;

        BufferedImage newImg = image.getSubimage(x, y, w, h);

        file.delete();
        ImageIO.write(newImg, "png", file);
    }

    /**
     * Finds the dimension of an image, trimming away all the empty space.
     * @param img the image to find the generates on
     * @return a Pair<Pair<First vertical slice of image with a nontransparent pixel, last vertical slice>, Pair<First vertical slice, Last vertical slice>>
     * @throws IOException if img is null
     */
    public static Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> getPixelLimit(BufferedImage img) throws IOException {
        Pair<Integer, Integer> xLimit = new Pair<>(null, null);
        Pair<Integer, Integer> yLimit = new Pair<>(null, null);
        boolean a = false;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (a)
                    break;
                if (!isTranparentPixel(img, x, y)) {
                    xLimit.setFirst(x - 1);
                    a = true;
                }
            }
        }
        boolean b = false;
        for (int x = img.getWidth() - 1; x >= 0; x--) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (b)
                    break;
                if (!isTranparentPixel(img, x, y)) {
                    xLimit.setSecond(x + 1);
                    b = true;
                }
            }
        }

        boolean c = false;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (c)
                    break;
                if (!isTranparentPixel(img, x, y)) {
                    yLimit.setFirst(y - 1);
                    c = true;
                }
            }
        }

        boolean d = false;
        for (int y = img.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (d)
                    break;
                if (!isTranparentPixel(img, x, y)) {
                    yLimit.setSecond(y + 1);
                    d = true;
                }
            }
        }

        return Pair.of(xLimit, yLimit);
    }

    /**
     * Returns whether the (x,y) pixel in img is transparent
     * @param img image
     * @param x x cord of pixel to check
     * @param y y cord of pixel to check
     * @return whether that pixel is transparent
     */
    public static boolean isTranparentPixel(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y) >> 24) == 0x00;
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


}


