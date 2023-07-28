package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class CardDownload {
    public static final String TXT_TO_READ = "/Users/drew/Desktop/Hearthstone.txt";
    public static final String CARD_FOLDER_PATHNAME = "/Users/drew/Desktop/Hearthstone.txt";
    public static List<Card> processAllCards(String path) throws FileNotFoundException {

        Scanner file = new Scanner(new File(path));
        List<Card> cards = new ArrayList<>();

        Card.Builder newCard = Card.Builder.template();
        while (file.hasNextLine()) {
            String nextLine = file.nextLine();
            if ((nextLine.contains("}") || nextLine.contains("{"))) {
                Card card = newCard.build();
                if (!card.equals(Card.NULL_CARD)) {
                    cards.add(card);
                }
                newCard = Card.Builder.template();
                continue;
            }

            newCard.fillFromMap(nextLine);
        }

        return cards;
    }

    public static void downloadAllInList(List<Card> cards, Predicate<Card> pred) throws IOException {
        String cardURLStarter = "https://art.hearthstonejson.com/v1/render/latest/enUS/512x/";


        File file = new File(Util.READTXT_FILEPATH);
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);

        for (Card card : cards.stream().filter(pred).toList()) {
            URL url = new URL(cardURLStarter + card.getId() + ".png" );

            StringBuilder path = new StringBuilder(Util.CARD_FOLDER_FILEPATH + "/" + card.getName());
            try {
                try (InputStream in = url.openStream()) {
                    Integer i = 2;
                    StringBuilder newPath = path;
                    while (new File(newPath + ".png").exists()) {
                        newPath = path;
                        newPath.append(" ").append(i);
                        i++;
                    }
                    path = newPath;
                    path.append(".png");
                    Files.copy(in, Paths.get(path.toString()));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
