package util;

import java.util.function.Predicate;

public class PredsUtil {
    public static final Predicate<Card> IS_MERCENARY = Card::isMercenaries;
    public static final Predicate<Card> IS_LOOT_CARD = Card::isSingleLoot;
    public static final Predicate<Card> IS_PUZZLE = Card::isPuzzleLab;

    public static <T> Predicate<T> invert(Predicate<T> pred) {
        return (T t) -> !pred.test(t);
    }

    public static <T> Predicate<T> and(Predicate<T> a, Predicate<T> b) {
        return (T t) -> a.test(t) && b.test(t);
    }

    public static <T> Predicate<T> or(Predicate<T> a, Predicate<T> b){
        return (T t) -> a.test(t) || b.test(t);
    }
}
