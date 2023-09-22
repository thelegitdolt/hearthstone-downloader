package util;

import core.Card;

import java.util.function.Predicate;

/**
 * A class with some methods for predicates, which are the main way to determine whether a card will be downloaded or not
 * @author Dolt
 */
public class PredsUtil {
    public static final Predicate<Card> ALWAYS_TRUE = a -> true;
    public static final Predicate<Card> IS_MERCENARY = Card::isMercenaries;
    public static final Predicate<Card> IS_LOOT_CARD = Card::isLootCard;
    public static final Predicate<Card> IS_PUZZLE = Card::isPuzzleLab;
    public static final Predicate<Card> IS_ENCHANTMENT = Card::isEnchantment;
    public static final Predicate<Card> IS_BG_GOLDEN = idContains("BaconUps");


    public static final Predicate<Card> SHOULD_INSTALL = noneTrue(IS_MERCENARY, IS_LOOT_CARD, IS_PUZZLE, IS_ENCHANTMENT, IS_BG_GOLDEN);
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


    public static Predicate<Card> idContains(String chars) {
        return (card) -> NullStringUtil.contains(chars, card.getId());
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
     * @param preds as many predicates are you want
     * @return a new predicate that is true as long as none of preds is true
     */
    @SafeVarargs
    public static <T> Predicate<T> noneTrue(Predicate<T>... preds) {
        return (T t) -> {
            for (Predicate<T> pred : preds) {
                if (pred.test(t))
                    return false;
            }
            return true;
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
