package core;

import util.NullStringUtil;
import util.PredsUtil;
import util.Util;
import values.CardSet;
import values.CardType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DupeProtection {
    @RequiresInitializedCardList
    public static void listDupes() {
        List<Card> withName = new ArrayList<>();

        CardList.forEach((card) -> {
            if (card.getImagePath().isEmpty()) {
                return;
            }

            if ((withName.size() > 0 && !NullStringUtil.equals(card.getName(),  withName.get(0).getName())) ) {
                if (withName.size() > 1 && withName.stream().anyMatch(Card::isCollectible))
                    System.out.println(withName.get(0).getName());
            }

            withName.add(card);
        });
    }

    @RequiresInitializedCardList
    public static void deleteDupes() {
        List<Card> withName = new ArrayList<>();

        CardList.forEach((card) -> {
            if (card.getImagePath().isEmpty()) {
                return;
            }

            if ((withName.size() > 0 && !NullStringUtil.equals(card.getName(),  withName.get(0).getName())) ) {
//                    if (withName.size() > 1 && withName.stream().anyMatch(Card::isCollectible))
//                         System.out.println(withName.get(0).getName());

                decideDelete(withName).forEach(File::delete);
                withName.clear();
            }

            withName.add(card);
        });
    }

    private static List<File> decideDelete(List<Card> cards) {
        List<Card> toReturn = new ArrayList<>();

        // divide all cards into a number of subsets
        // each subset should have identical names, texts, costs, attacks, healths, card type, and race/spell school
        List<List<Card>> subsets = new ArrayList<>();

        boolean hasHome = false;

        for (Card card : cards) {
            for (List<Card> subset : subsets) {
                if (subset.get(0).isMostlyEqual(card)) {
                    subset.add(card);
                    hasHome = true;
                }
            }
            if (hasHome) {
                hasHome = false;
                continue;
            }

            List<Card> newSubSet = new ArrayList<>();
            newSubSet.add(card);
            subsets.add(newSubSet);
        }


        // Drop all subsets with only uncollectible cards; I will handle these separately
        List<List<Card>> uncollectibles = subsets.stream().filter((list) -> list.stream().noneMatch(Card::isCollectible)).toList();
        decideDeleteUncollectibles(uncollectibles, toReturn);

        subsets.removeIf((list) -> list.stream().noneMatch(Card::isCollectible));



        // from each subset, if there are collectible cards inside, filter out any cards that are not collectible
        for (List<Card> subset : subsets) {
            if (Util.notAllElementsMatch(subset, Card::isCollectible))
                Util.removeIfThenApply(subset, PredsUtil.not(Card::isCollectible), toReturn::add);
        }

        // if one of the minions is in the core set, get rid of it.
        for (List<Card> subset : subsets) {
            if (Util.notAllElementsMatch(subset, (a) -> a.getSet() == CardSet.CORE))
                Util.removeIfThenApply(subset, c -> c.getSet() == CardSet.CORE, toReturn::add);
        }

        return toReturn.stream().map(card -> card.getImagePath().orElse(null)).filter(Objects::nonNull).toList();
    }


    private static void decideDeleteUncollectibles(List<List<Card>> subsets, List<Card> toReturn) {
        if (NullStringUtil.equals(subsets.get(0).get(0).getName(), "The Coin"))
            return;

        // first of all, if any these subsets are not Duels-only yet has a Duels counterpart, cut those.
        for (List<Card> subset : subsets) {
            if ((subset.get(0).isDuelsPassive() || subset.get(0).isDuelsTreasure()) && subsets.size() > 1) {
                toReturn.addAll(subset);

                subset.clear();
            }
        }

        // Clear any tavern brawl hero powers; they don't really do a lot
        for (List<Card> subset : subsets) {
            if (subsets.size() > 1 && subset.get(0).getType() == CardType.HERO_POWER && subset.get(0).getId().endsWith("_TB")) {
                toReturn.addAll(subset);

                subset.clear();
            }
        }


    }
}
