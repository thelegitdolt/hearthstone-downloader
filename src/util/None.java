package util;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that holds no information
 * Intended to be used in lists, as they can be changed even if they are final, so information can be transfered through lambdas
 * This is an incredibly stupid way to go about it
 * @author Dolt
 */
public class None {
    /**
     * Use None.make() because new None() is stupid
     */
    private None() {}

    /**
     * Makes a new None object
     */
    public static None make() {
        return new None();
    }

    /**
     * Makes a list of Nones. "None.list()" is slightly shorter than "new ArrayList<>()</>"
     */
    public static List<None> list() {
        return new ArrayList<>();
    }

    /**
     * Adds one None to the list
     */
    public static void add(List<None> list) {
        list.add(make());
    }

    /**
     * Adds a num amonut of Nones to list
     */
    public static void add(List<None> list, int num) {
        for (int i = 0; i < num; i++) {
            add(list);
        }
    }
}
