package core;

import util.FileUtil;
import util.ImgUtil;
import util.PredsUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static final String SOURCE_API_URL = "https://static.firestoneapp.com/cards/enUS/512/id_here.png";
    private static final String SOURCE_API_URL_BG = "https://static.firestoneapp.com/cards/bgs/enUS/512/id_here.png";

    public static void defaultRun() throws IOException {
        List<Card> cards = new ArrayList<>();
        processAllCards(FileUtil.READTXT_FILEPATH, PredsUtil.not(Card::shouldOmitFromInstall), cards);
        downloadAll(cards, Card::toString);
    }

    public static void processAllCards(String path, Predicate<Card> pred, List<Card> cards) throws FileNotFoundException {
        Scanner file = new Scanner(new File(path));

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
    }

    public static void downloadAll(List<Card> queue, Function<Card, String> nameFunc) throws IOException {
        new File(FileUtil.CARD_FOLDER_FILEPATH).mkdirs();

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

            URL bgUrl = new URL(SOURCE_API_URL_BG.replace("id_here", card.getId()));
            try {
                try (InputStream in = bgUrl.openStream()) {
                    String path = FileUtil.CARD_FOLDER_FILEPATH + "/" + nameFunc.apply(card) + ".png";
                    Files.copy(in, Path.of(path));

                    ImgUtil.cropIgnoreException(new File(path));
                    continue;
                }
            }
            catch (FileNotFoundException ignored) {}


            URL url = new URL(SOURCE_API_URL.replace("id_here", card.getId()));
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
