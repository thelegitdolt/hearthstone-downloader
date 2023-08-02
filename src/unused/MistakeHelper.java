package unused;

import util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MistakeHelper {

    public static String trimTailNumbersPNGFile(String str) {

        StringBuilder build = new StringBuilder(str);
        build.delete(str.length() - 4, str.length());
        List<Integer> numbers = new ArrayList<>();

        while (Character.isDigit(build.charAt(build.length() - 1)) || build.charAt(build.length() - 1) == ' ') {
            int index = build.length() - 1;
            List<Character> latestNumber = new ArrayList<>();
            while (index >= 0 && Character.isDigit(build.charAt(index))) {
                latestNumber.add(build.charAt(index));
                index--;
            }

            int number = Util.getNumberFromIntList(
                    latestNumber.stream().map(Character::getNumericValue).toList());

            numbers.add(number);

            build = new StringBuilder(build.substring(0, build.length() - 1 - String.valueOf(number).length()));
        }

        if (numbers.size() == 0)
            return str;

        Collections.reverse(numbers);

        for (int i = 0; i < numbers.lastIndexOf(2); i++) {
            build.append(" ").append(numbers.get(0).toString());

            numbers.remove(0);
        }

        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i + 1) - numbers.get(i) != 1)
                return str;
        }

        return build.append(" ").append(numbers.get(numbers.size() - 1)).append(".png").toString();
    }


}
