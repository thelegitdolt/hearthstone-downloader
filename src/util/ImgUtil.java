package util;

import datafixers.Lambdas;
import datafixers.None;
import datafixers.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImgUtil {
    public static void mapPixels(BufferedImage img, Lambdas.TriConsumer<BufferedImage, Integer, Integer> action) {
        for (int x = 0; x < img.getWidth(); x ++) {
            for (int y = 0; y < img.getHeight(); y++) {
                action.apply(img, x, y);
            }
        }
    }


    /**
     * Takes an image and returns a list of all possible kernels of size kernelSize on that image
     *
     * @param img the image
     * @param kernelSize the size of the kernel. 2 means 2x2, etc.
     * @param stopAfterMaxKernel how many kernels to place into list before it stops.
     * @param startX X cord of the location to start producing kernels at
     * @param startY y cord of the location to start producing kernels at
     * @return the list yk
     */
    public static List<int[][]> getImageKernelsOf(BufferedImage img, int kernelSize, int stopAfterMaxKernel, int startX, int startY) {
        List<int[][]> kernels = new ArrayList<>();

        if (kernelSize > img.getWidth() || kernelSize > img.getHeight())
            throw new IllegalArgumentException("Kernelsize cannot be bigger than the dimension of the image");
        else if (startX > img.getWidth())
            throw new IllegalArgumentException("StartX is outside the image");
        else if (startY > img.getHeight())
            throw new IllegalArgumentException("StartY is outside the image");

        int kernelAmount = 0;
        for (int y = 0; y < img.getHeight() - 1 - kernelSize; y++) {
            for (int x = 0; x < img.getWidth() - 1 - kernelSize; x++) {
                if (y < startY)
                    continue;
                else if (y == startY && x < startX)
                    continue;
                else if (isTranparentPixel(img, x, y))
                    continue;

                kernelAmount++;
                int[][] singleKernel = new int[kernelSize][kernelSize];

                for (int kernelX = 0; kernelX < kernelSize; kernelX++){
                    for (int kernelY = 0; kernelY < kernelSize; kernelY++) {
                        singleKernel[kernelX][kernelY] = img.getRGB(kernelX + x, kernelY + y);
                    }
                }
                kernels.add(singleKernel);
                if (kernelAmount >= stopAfterMaxKernel)
                    return kernels;
            }
        }

        return kernels;
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
     */
    public static Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> getPixelLimit(BufferedImage img) {
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


    public static boolean sameImage(String img1, String img2) throws IOException {
        return sameImage(new File(img1), new File(img2));
    }

    public static boolean sameImage(File img1, File img2) throws IOException {
        return sameImage(ImageIO.read(img1), ImageIO.read(img2));
    }

    public static boolean sameImage(BufferedImage img1, BufferedImage img2) throws IOException {
        BufferedImage artApprox = img1.getSubimage(img1.getWidth() / 4, img1.getHeight() / 8, img1.getWidth() / 2, img1.getHeight() * 3 / 8);

        BufferedImage newRef = img2.getSubimage(img2.getWidth() / 4, img2.getHeight() / 8, img2.getWidth() / 2, img2.getHeight() * 3 / 8);


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



    public static int[] getRGBs(int rawRGB) {
        Color color = new Color(rawRGB);
        return new int[]{
          color.getRed(), color.getBlue(), color.getGreen()
        };
    }

    public static void printAsRGB(int rawRGB) {
        System.out.println(Arrays.toString(getRGBs(rawRGB)));
    }

    public static int twoColorDist(int pix1, int pix2) {
        Color color1 = new Color(pix1);
        Color color2 = new Color(pix2);

        int red1 = color1.getRed();
        int blue1 = color1.getBlue();
        int green1 = color1.getGreen();

        int red2 = color2.getRed();
        int blue2 = color2.getBlue();
        int green2 = color2.getGreen();

        return Math.abs(red1 + blue1 + green1 - red2 - blue2 - green2);
    }





    public static void replaceImage(BufferedImage img, String path) throws IOException {
        File file = new File(path);

        if (file.exists())
            file.delete();

        ImageIO.write(img, "png", file);
    }

}
