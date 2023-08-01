package util;

import java.util.function.Predicate;

/**
 * A class with some methods for predicates, which are the main way to determine whether a card will be downloaded or not
 * @author Dolt
 */
public class PredsUtil {
    public static final Predicate<Card> ALWAYS_TRUE = (card) -> true;
    public static final Predicate<Card> IS_MERCENARY = Card::isMercenaries;
    public static final Predicate<Card> IS_LOOT_CARD = Card::isLootCard;
    public static final Predicate<Card> IS_PUZZLE = Card::isPuzzleLab;
    public static final Predicate<Card> IS_ENCHANTMENT = Card::isEnchantment;

    /**
     * Inverts the predicate
     * @return is delegated to Predicates.not(), is placed here for easier access
     */
    public static <T> Predicate<T> not(Predicate<T> pred) {
        return Predicate.not(pred);
    }

    /**
     * Combines a and b predicates
     * @return A predicate that only returns true if both a and b are true
     */
    public static <T> Predicate<T> and(Predicate<T> a, Predicate<T> b) {
        return (T t) -> a.test(t) && b.test(t);
    }

    /**
     * @return a Predicate<Card></Card> returns true iff the card is a specific name
     */
    public static Predicate<Card> isName(String name) {
        return (card) -> NullStringUtil.equals(name, card.getName());
    }

    /**
     * @param preds as many predicates as you want
     * @return a new predicate that is true only if all of preds returns true
     */
    @SafeVarargs
    public static <T> Predicate<T> allTrue(Predicate<T>... preds) {
        return (T t) -> {
            for (Predicate<T> pred : preds) {
                if (!pred.test(t))
                    return false;
            }
            return true;
        };
    }

    /**
     * @param preds as many predicates are you want
     * @return a new predicate that is true as long as one of preds is true
     */
    @SafeVarargs
    public static <T> Predicate<T> oneTrue(Predicate<T>... preds) {
        return (T t) -> {
            for (Predicate<T> pred : preds) {
                if (pred.test(t))
                    return true;
            }
            return false;
        };
    }


    /**
     * Combines a and b predicates
     * @return A predicate that returns true if either a or b is true
     */
    public static <T> Predicate<T> or(Predicate<T> a, Predicate<T> b){
        return (T t) -> a.test(t) || b.test(t);
    }
}
