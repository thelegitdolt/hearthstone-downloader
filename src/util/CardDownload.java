package util;

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
import java.util.function.Predicate;

public class CardDownload {
    public static final String TXT_TO_READ = "/Users/drew/Desktop/Hearthstone.txt";
    public static final String CARD_FOLDER_PATHNAME = "/Users/drew/Desktop/Hearthstone Cards";
    public static final String SOURCE_API_URL = "https://art.hearthstonejson.com/v1/render/latest/enUS/512x/";
    public static List<Card> processAllCards(String path, Predicate<Card> pred) throws FileNotFoundException {
        Scanner file = new Scanner(new File(path));
        List<Card> cards = new ArrayList<>();

        Card.Builder newCard = Card.Builder.template();
        while (file.hasNextLine()) {
            String nextLine = file.nextLine();
            if ((nextLine.contains("}") || nextLine.contains("{"))) {
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
        downloadAllConditioned(cards, PredsUtil.ALWAYS_TRUE);
    }

    public static void downloadAllConditioned(List<Card> cards, Predicate<Card> cardPred) throws IOException {
        List<Card> queue = cards.stream().filter(cardPred).toList();

        int queueSize = queue.size();
        System.out.println("Card download started! Queueing up  " + queueSize + " cards for download.");

        int error = 0;
        int percentage = 0;
        for (Card card : queue) {
            URL url = new URL(SOURCE_API_URL + card.getId() + ".png" );

            try {
                try (InputStream in = url.openStream()) {
                    String path = Util.CARD_FOLDER_FILEPATH + "/" + card.getName() + " " + card.getId();

                    Files.copy(in, Paths.get(path));
                }
            }
            catch (FileNotFoundException e) {
                error++;
            }

            if (queue.indexOf(card) % (queueSize / 100) == 0 ) {
                percentage++;
                System.out.println(percentage + "%");
            }
        }

        System.out.println("Card download finished!");
        if (error > 0)
            System.out.println("Did not download " + error + " cards as their information could not be retrieved.");
    }
}
