import core.Card;
import core.Search;
import util.FileUtil;
import util.NullStringUtil;
import values.CardSet;
import values.CardType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {


     public static void main(String[] args) throws IOException {
          Card.initializeSortedCardList();


          List<Card> withName = new ArrayList<>();

          FileUtil.mapDir(FileUtil.CARD_FOLDER_FILEPATH, (File file) -> {

               Card card = Card.lookup(file).orElse(null);
               if (card == null)
                    return;


               if (withName.size() > 0 && card.getName().equals( withName.get(0).getName())) {
//                    Card.decideDelete(withName).forEach(File::delete);

                    if (withName.size() > 1 && withName.stream().anyMatch(Card::isCollectible))
                         System.out.println(card);

                    withName.clear();
               }

               withName.add(card);
          }, false);

     }
}