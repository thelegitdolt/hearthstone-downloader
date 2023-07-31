package util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BinaryBoolList {

    public static int compute(List<Boolean> list) {
        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i))
                result+= Math.pow(2, i);
        }
        return result;
    }

    public static int compute(boolean[] array)  {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i])
                result+= Math.pow(2, i);
        }
        return result;
    }

    public static List<Boolean> of(int num) {
        int i = 0;
        while (Math.pow(2, i) < num) {
            i++;
        }
        return makeHelper(num, i, new ArrayList<>());
    }

    private static List<Boolean> makeHelper(int num, int bound, List<Boolean> list) {
        if (bound >= 0) {
            if (num >= Math.pow(2, bound)) {
                 list.add(true);
                makeHelper(num - (int) Math.pow(2, bound), bound - 1, list);
            }
            else {
                list.add(false);
                makeHelper(num, bound - 1, list);
            }
        }
        else {
            Collections.reverse(list);
        }
        return list;
    }

    public static List<Boolean> operate(List<Boolean> num, Function<Integer, Integer> op) {
        return of(op.apply(compute(num)));
    }
}
