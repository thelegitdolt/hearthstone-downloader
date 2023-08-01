package util;

/**
 * A class for various String methods that allows them to be nullable
 * @author Dolt
 */
public class NullStringUtil {
    /**
     * Implimentation of String.equals().
     * @return false if only one of a or b is null, true if they are both null, otherwise delegates to String.equals()
     */
    public static boolean equals(String a, String b) {
        if (a == b)
            return true;
        else return a.equals(b);
    }

    /**
     * Implimentation of String.contains()
     * @return false if a is null, otherwise delegates to String.contains()
     */
    public static boolean contains(String a, CharSequence b) {
        if (a == null)
            return false;
        else return a.contains(b);
    }

    /**
     * Implementation of String.indexOf()
     * @return -1 if a is null, otherwise delegates to String.indexOf()
     */
    public static int indexOf(String a, String b) {
        if (a == null)
            return -1;
        else
            return a.indexOf(b);
    }
}
