package util;

import datafixers.Lambdas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
     * Guess what this method does!!!!
     */
    public static double square(double dubs) {
        return dubs * dubs;
    }

    public static <V> Set<Map.Entry<Integer, V>> enumerate(List<V> list) {
        Map<Integer, V> enumeratedThing = new HashMap<>();
        for (int i = 0; i < list.size() - 1; i++) {
            enumeratedThing.put(i, list.get(i));
        }

        return enumeratedThing.entrySet();
    }


    public static <E> void removeIfThenApply(List<E> list, Predicate<? super E> criteria, Consumer<E> action) {
        for (E element : list.stream().filter(criteria).toList()) {
            action.accept(element);
        }

        list.removeIf(criteria);
    }

     public static <K, V> Map<K, V> mapButKeepFirst(List<K> list, Function<? super K, V> mapper) {
        Map<K, V> map = new HashMap<>();
        for (K element : list)
            map.put(element, mapper.apply(element));

        return map;
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


    public static boolean deepFitCriteria(int[][] a1, int[][] a2, Lambdas.BiPredicate<Integer, Integer> criteria) {
        if (a1 == a2)
            return true;
        if (a1 == null || a2==null)
            return false;
        int length = a1.length;
        if (a2.length != length)
            return false;


        for (int i = 0; i < length; i++) {
            int[] e1 = a1[i];
            int[] e2 = a2[i];

            if (e1 == null)
                return false;

            int len = e1.length;

            if (len != e2.length)
                return false;

            for (int j = 0; j < len; j++)
                if (!criteria.test(e1[j], e2[j])) return false;
        }
        return true;
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


