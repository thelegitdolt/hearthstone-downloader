package core;

import util.FileUtil;
import util.PredsUtil;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * List of all the cards!
 */
public class CardList {
    private static List<Card> cardList = new ArrayList<>();

    @RequiresInitializedCardList
    public static List<Card> get() {
        return cardList;
    }

    @RequiresInitializedCardList
    public static int indexOf(Card card) {
        return cardList.indexOf(card);
    }

    @RequiresInitializedCardList
    public static int lastIndexOf(Card card) {
        return cardList.lastIndexOf(card);
    }

    @RequiresInitializedCardList
    public static void forEach(Consumer<Card> action) {
        cardList.forEach(action);
    }

    @RequiresInitializedCardList
    public static Stream<Card> stream() {
        return cardList.stream();
    }

    @RequiresInitializedCardList
    public static Card get(int value) {
        return cardList.get(value);
    }

    @RequiresInitializedCardList
    public static int size() {
        return cardList.size();
    }


    /**
     * Initializes cardList to have every single hearthstone card in the game.
     * You MUST run this to do most logic on this card.
     * @param preds Any cards that do not meet this Predicate will not be included.
     * @throws FileNotFoundException if your FileUtil.CARD_FOLDER_FILEPATH or FileUtil.READTXT_FILEPATH is wrong.
     */
    public static void initialize(Predicate<Card> preds) throws FileNotFoundException {
        cardList = HearthstoneDownloader.processAllCards(FileUtil.READTXT_FILEPATH, preds);
    }


    public static void initializeWith(boolean sorted, String... ids) throws FileNotFoundException {
        Predicate<Card> pred = (c) -> Arrays.asList(ids).contains(c.getId());
        if (sorted)
            initializeSorted(pred);
        else
            initialize(pred);
    }

    public static void initializeWith(String... ids) throws FileNotFoundException {
        initializeWith(true, ids);
    }


    public static void initializeSorted(Predicate<Card> preds) throws FileNotFoundException {
        cardList = HearthstoneDownloader.processAllCards(FileUtil.READTXT_FILEPATH, preds).stream()
                .sorted((first, second) -> String.CASE_INSENSITIVE_ORDER.compare(first.getName(), second.getName()))
                .toList();
    }



    public static void initializeSorted() throws FileNotFoundException {
        initializeSorted(PredsUtil.ALWAYS_TRUE);
    }

    /**
     * initializeCardList that will contain all cards by default with no filtering.
     * Done by calling Card.initializeCardList(preds) with a predicate that is always true.
     */
    public static void initialize() throws FileNotFoundException {
        initialize(PredsUtil.ALWAYS_TRUE);
    }


}
