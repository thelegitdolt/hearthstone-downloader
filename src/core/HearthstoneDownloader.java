package core;

import util.FileUtil;
import util.PredsUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The meat and potatoes of the program.
 * Methods to extract cards to download from jsons, and then saving all these cards into a computer.
 * @author Dolt
 */
public class HearthstoneDownloader {
    private static final String SOURCE_API_URL = "https://art.hearthstonejson.com/v1/render/latest/enUS/512x/";

    public static void defaultRun() throws IOException {
        downloadAll(processAllCards(FileUtil.READTXT_FILEPATH, PredsUtil.SHOULD_INSTALL));
    }

    public static List<Card> processAllCards(String path, Predicate<Card> pred) throws FileNotFoundException {
        Scanner file = new Scanner(new File(path));
        List<Card> cards = new ArrayList<>();

        Card.Builder newCard = Card.Builder.template();
        while (file.hasNextLine()) {
            String nextLine = file.nextLine();
            if (nextLine.equals("[") || nextLine.equals("]"))
                continue;
            if ((nextLine.charAt(0) != '\t')) {
                Card card = newCard.build();
                if (!card.equals(Card.NULL_CARD) && pred.test(card)) {
                    cards.add(card);
                }
                newCard = Card.Builder.template();
                continue;
            }

            newCard.fillFromMap(nextLine);
        }

        return cards;
    }

    public static void downloadAll(List<Card> cards) throws IOException {
        downloadAll(cards, PredsUtil.ALWAYS_TRUE);
    }

    public static void downloadAll(List<Card> cards, Predicate<Card> cardPred) throws IOException {
        downloadAll(cards, cardPred, Card::toString);
    }

    public static void downloadAll(List<Card> cards, Function<Card, String> nameFunc) throws IOException {
        downloadAll(cards, PredsUtil.ALWAYS_TRUE, nameFunc);
    }

    public static void downloadAll(List<Card> cards, Predicate<Card> cardPred, Function<Card, String> nameFunc) throws IOException {
        new File(FileUtil.CARD_FOLDER_FILEPATH).mkdirs();
        List<Card> queue = cards.stream().filter(cardPred).toList();

        int queueSize = queue.size();
        System.out.println("Card download started! Queueing up  " + queueSize + " cards for download.");
        System.out.println("Progress:");

        int error = 0;
        int percentage = 0;
        for (Card card : queue) {
            if (queue.indexOf(card) % (queueSize / 100) == 0 ) {
                System.out.println(percentage + "%");
                percentage++;
            }

            if (card.fileExists())
                continue;

            URL url = new URL(SOURCE_API_URL + card.getId() + ".png" );
            try {
                try (InputStream in = url.openStream()) {
                    String path = FileUtil.CARD_FOLDER_FILEPATH + "/" + nameFunc.apply(card) + ".png";
                    Files.copy(in, Paths.get(path));
                }
            }
            catch (FileNotFoundException e) {error++;}
        }

        System.out.println("Card download finished!");
        if (error > 0)
            System.out.println("Did not download " + error + " cards as their information could not be retrieved.");
    }

}
