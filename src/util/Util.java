package util;

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
    public static void mapDir(String path, Lambdas.FileConsumer action) {
        mapDir(new File(path), action);
    }

    public static void mapPixels(BufferedImage img, Lambdas.TriConsumer<BufferedImage, Integer, Integer> action) {
        for (int x = 0; x < img.getWidth(); x ++) {
            for (int y = 0; y < img.getHeight(); y++) {
                action.apply(img, x, y);
            }
        }
    }

    /**
     * A method that applies an action to all non-directory files in a folder.
     * @param directory - directory, as a File
     * @param action - the action to undertake
     */
    public static void mapDir(File directory, Lambdas.FileConsumer action) {
        if (!directory.isDirectory())
            return;

        Set<File> files = Stream.of(Objects.requireNonNull(directory.listFiles()))
                .filter(file -> !file.isDirectory()).collect(Collectors.toSet());

        System.out.println("Queued " + files.size() + " files to process");

        int i = 0;
        for (File file : files) {
            try {
                action.apply(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            i++;
            if (i % 500 == 0)
                System.out.println(i);
        }
    }

    public static boolean nullableStrEqual(String a, String b) {
        if (a == b)
            return true;
        else return a.equals(b);
    }

    public static boolean nullableStrContains(String a, CharSequence b) {
        if (a == null)
            return false;
        else return a.contains(b);
    }


    public static int nullableStrIndexOf(String a, String b) {
        if (a == null)
            return -1;
        else
            return a.indexOf(b);
    }


    public static void cropIgnoreException(File file) {
        try {
            cropExtraneous(file);
        }
        catch (Exception ignored) {}
    }


    /**
     * Takes a file and trims it so there is no extraneous transparent outer layer.
     * @param file to trim, must be an image
     * @throws IOException if that file is not an image or does not exist
     */
    public static void cropExtraneous(File file) throws IOException {

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
        try {
            file.delete();
            ImageIO.write(newImg, "png", file);
        }
        catch (IOException e) {
            System.out.println(file.getPath());
            throw new IOException("");
        }

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

    public static boolean sameImage(String img1, String img2) throws IOException {
        return sameImage(new File(img1), new File(img2));
    }

    public static boolean sameImage(File img1, File img2) throws IOException {
        return sameImage(ImageIO.read(img1), ImageIO.read(img2));
    }

    public static boolean sameImage(BufferedImage img1, BufferedImage img2) throws IOException {
        BufferedImage artApprox = img1.getSubimage(img1.getWidth() / 4, img1.getHeight() / 8, img1.getWidth() / 2, img1.getHeight() * 3 / 8);

        BufferedImage newRef = img2.getSubimage(img2.getWidth() / 4, img2.getHeight() / 8, img2.getWidth() / 2, img2.getHeight() * 3 / 8);

        ImageIO.write(artApprox, "png", new File("/Users/drew/Desktop/test.png"));
        ImageIO.write(newRef, "png", new File("/Users/drew/Desktop/test2.png"));

        List<Integer> colors = new ArrayList<>();


        mapPixels(artApprox, (img, x, y) -> {
            int pixel = img.getRGB(x, y);
            if (!colors.contains(pixel)) {
                colors.add(pixel);
            }
        });

        List<None> counter = None.list();

        int toReach = (newRef.getHeight() * newRef.getWidth()) / 10;

        mapPixels(newRef, (img, x, y) -> {
            int pixel = img.getRGB(x, y);

            if (colors.contains(pixel)) {
                counter.add(None.make());
            }
        });

        return counter.size() >= toReach;
    }




    private static BufferedImage getArtImageApprox(BufferedImage img) {
        int x, y, w, h;
        x = img.getWidth() / 3;
        y = img.getHeight() / 5;
        w = img.getWidth() / 3;
        h = (int) (img.getHeight() / 6.5);

        return img.getSubimage(x, y, w, h);
    }

    public static final int HERO_POWER_WIDTH = 440;
    public static final int HERO_POWER_HEIGHT = 656;

    public static boolean isHeroPowerDimensions(BufferedImage card) {
        return card.getWidth() == HERO_POWER_WIDTH && card.getHeight() == HERO_POWER_HEIGHT;
    }


}


