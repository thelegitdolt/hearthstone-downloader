package util;

import java.util.ArrayList;
import java.util.List;

public class None {
    public None() {}

    public static None make() {
        return new None();
    }

    public static List<None> list() {
        return new ArrayList<>();
    }
}
