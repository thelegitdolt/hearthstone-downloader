package core;

import util.FileUtil;
import util.PredsUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * List of all the cards!
 * Must be initialized in some way for the majority of features in the mod to work.
 * Also contains helpful features from the ArrayList class so you don't have to do CardList.list().stream() all the time.
 * @author Dolt
 */
public class CardList {
    private final static List<Card> cardList = new ArrayList<>();

    /**
     * Gets the card list for you.
     * In you want to do your own thing that the following things won't work on.
     */
    @RequiresInitializedCardList
    public static List<Card> list() {
        return cardList;
    }

    /**
     * does shorthand for CardList.list().indexOf().
     */
    @RequiresInitializedCardList
    public static int indexOf(Card card) {
        return cardList.indexOf(card);
    }

    /**
     * does shorthand for CardList.list().lastIndexOf().
     */
    @RequiresInitializedCardList
    public static int lastIndexOf(Card card) {
        return cardList.lastIndexOf(card);
    }

    /**
     * does shorthand for CardList.list().forEach().
     */
    @RequiresInitializedCardList
    public static void forEach(Consumer<Card> action) {
        cardList.forEach(action);
    }

    /**
     * does shorthand for CardList.list().stream().
     */
    @RequiresInitializedCardList
    public static Stream<Card> stream() {
        return cardList.stream();
    }

    /**
     * does shorthand for CardList.list().get().
     */
    @RequiresInitializedCardList
    public static Card get(int value) {
        return cardList.get(value);
    }

    /**
     * does shorthand for CardList.list().size().
     */
    @RequiresInitializedCardList
    public static int size() {
        return cardList.size();
    }


    /**
     * Initializes cardList to have every single hearthstone card in the game.
     * You MUST run this to allow any method annotated @RequiresInitializedCardList to work properly.
     * @param preds Any cards that do not meet this Predicate will not be included.
     * @throws FileNotFoundException if your FileUtil.CARD_FOLDER_FILEPATH or FileUtil.READTXT_FILEPATH is wrong.
     */
    @CardListInitializer
    public static void initializeFiltered(Predicate<Card> preds) throws FileNotFoundException {
        HearthstoneDownloader.processAllCards(FileUtil.READTXT_FILEPATH, preds, cardList);
    }


    /**
     * Initializes cardList with only the cards in ids.
     * You MUST run this to allow any method annotated @RequiresInitializedCardList to work properly.
     * @param sorted If the list returned to be sorted alphabetically
     * @param ids the ids of cards that will be contained in your cardlist.
     * @throws FileNotFoundException if your FileUtil.CARD_FOLDER_FILEPATH or FileUtil.READTXT_FILEPATH is wrong.
     */
    @CardListInitializer
    public static void initializeWith(boolean sorted, String... ids) throws FileNotFoundException {
        Predicate<Card> pred = (c) -> Arrays.asList(ids).contains(c.getId());
        if (sorted)
            initializeSorted(pred);
        else
            initializeFiltered(pred);
    }

    /**
     * an initializeWith with the @param sorted automatically set to true.
     * @param ids the ids of cards that will be contained in your cardlist.
     * @throws FileNotFoundException if your FileUtil.CARD_FOLDER_FILEPATH or FileUtil.READTXT_FILEPATH is wrong.
     */
    @CardListInitializer
    public static void initializeWith(String... ids) throws FileNotFoundException {
        initializeWith(true, ids);
    }

    /**
     * Initializes cardlist, sortign them alphabetically
     * @param preds optionally, only cards that makes this predicate true will be included in the cardlist.
     * @throws FileNotFoundException if your FileUtil.CARD_FOLDER_FILEPATH or FileUtil.READTXT_FILEPATH is wrong.
     */
    @CardListInitializer
    public static void initializeSorted(Predicate<Card> preds) throws FileNotFoundException {
        HearthstoneDownloader.processAllCards(FileUtil.READTXT_FILEPATH, preds, cardList);
        stream().sorted((first, second) -> String.CASE_INSENSITIVE_ORDER.compare(first.getName(), second.getName()))
                .toList();
    }

    /**
     * Initializes a sorted cardlist containing every card, no predicates.
     * @throws FileNotFoundException if your FileUtil.CARD_FOLDER_FILEPATH or FileUtil.READTXT_FILEPATH is wrong.
     */
    @CardListInitializer
    public static void initializeSorted() throws FileNotFoundException {
        initializeSorted(PredsUtil.ALWAYS_TRUE);
    }

    /**
     * initializeCardList that will contain all cards by default with no filtering.
     * Done by calling Card.initializeCardList(preds) with a predicate that is always true.
     */
    @CardListInitializer
    public static void initialize() throws FileNotFoundException {
        initializeSorted(PredsUtil.ALWAYS_TRUE);
    }
}
