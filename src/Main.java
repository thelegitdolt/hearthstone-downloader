import core.Card;
import util.FileUtil;
import values.CardSet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {


     public static void main(String[] args) throws IOException {
          Card.initializeCardList();

          List<File> files = Arrays.stream(Objects.requireNonNull(new File(FileUtil.CARD_FOLDER_FILEPATH).listFiles()))
                  .filter(img -> Card.lookup(img).orElse(Card.NULL_CARD).getSet() == CardSet.LETTUCE)
                  .toList();
     }
}