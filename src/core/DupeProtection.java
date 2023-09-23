package core;

import util.NullStringUtil;
import util.PredsUtil;
import util.Util;
import values.CardType;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DupeProtection {
    @RequiresInitializedCardList
    public static void listDupes() {
        Set<Card> withName = new HashSet<>();

        CardSet.forEach((card) -> {
            if (card.getImagePath().isEmpty()) {
                return;
            }

            Card c = withName.iterator().next();
            if ((withName.size() > 0 && !NullStringUtil.equals(card.getName(),  c.getName())) ) {
                if (withName.size() > 1 && withName.stream().anyMatch(Card::isCollectible))
                    System.out.println(c.getName());
            }

            withName.add(card);
        });
    }

    @RequiresInitializedCardList
    public static void deleteDupes() {
        Set<Card> withName = new HashSet<>();

        CardSet.forEach((card) -> {
            if (card.getImagePath().isEmpty()) {
                return;
            }

            if ((withName.size() > 0 && !NullStringUtil.equals(card.getName(),  withName.iterator().next().getName())) ) {
//                    if (withName.size() > 1 && withName.stream().anyMatch(Card::isCollectible))
//                         System.out.println(withName.get(0).getName());

                decideDelete(withName).forEach(File::delete);
                withName.clear();
            }

            withName.add(card);
        });
    }

    private static List<File> decideDelete(Set<Card> cards) {
        Set<Card> toReturn = new HashSet<>();

        // divide all cards into a number of subsets
        // each subset should have identical names, texts, costs, attacks, healths, card type, and race/spell school
        Set<Set<Card>> subsets = new HashSet<>();

        boolean hasHome = false;

        for (Card card : cards) {
            for (Set<Card> subset : subsets) {
                if (subset.iterator().next().isMostlyEqual(card)) {
                    subset.add(card);
                    hasHome = true;
                }
            }
            if (hasHome) {
                hasHome = false;
                continue;
            }

            Set<Card> newSubSet = new HashSet<>();
            newSubSet.add(card);
            subsets.add(newSubSet);
        }


        // Drop all subsets with only uncollectible cards; I will handle these separately
        Set<Set<Card>> uncollectibles = subsets.stream().filter((list) -> list.stream().noneMatch(Card::isCollectible)).collect(Collectors.toSet());
        decideDeleteUncollectibles(uncollectibles, toReturn);

        subsets.removeIf((list) -> list.stream().noneMatch(Card::isCollectible));



        // from each subset, if there are collectible cards inside, filter out any cards that are not collectible
        for (Set<Card> subset : subsets) {
            if (Util.notAllElementsMatch(subset, Card::isCollectible))
                Util.removeIfThenApply(subset, PredsUtil.not(Card::isCollectible), toReturn::add);
        }

        // if one of the minions is in the core set, get rid of it.
        for (Set<Card> subset : subsets) {
            if (Util.notAllElementsMatch(subset, (a) -> a.getSet() == values.CardSet.CORE))
                Util.removeIfThenApply(subset, c -> c.getSet() == values.CardSet.CORE, toReturn::add);
        }

        return toReturn.stream().map(card -> card.getImagePath().orElse(null)).filter(Objects::nonNull).toList();
    }


    private static void decideDeleteUncollectibles(Set<Set<Card>> subsets, Set<Card> toReturn) {
        if (NullStringUtil.equals(subsets.iterator().next().iterator().next().getName(), "The Coin"))
            return;

        // first of all, if any these subsets are not Duels-only yet has a Duels counterpart, cut those.
        for (Set<Card> subset : subsets) {
            Card c = subset.iterator().next();
            if ((c.isDuelsPassive() || c.isDuelsTreasure()) && subsets.size() > 1) {
                toReturn.addAll(subset);

                subset.clear();
            }
        }

        // Clear any tavern brawl hero powers; they don't really do a lot
        for (Set<Card> subset : subsets) {
            Card c = subset.iterator().next();
            if (subsets.size() > 1 && c.getType() == CardType.HERO_POWER && c.getId().endsWith("_TB")) {
                toReturn.addAll(subset);

                subset.clear();
            }
        }


    }
}
