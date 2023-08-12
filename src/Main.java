import core.Card;
import util.NullStringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


     public static void main(String[] args) throws IOException {
          Card.initializeSortedCardList();


          List<Card> withName = new ArrayList<>();

          Card.cardList.forEach((card) -> {
               if (card.getImagePath().isEmpty())
                    return;

               if (withName.size() > 0 && !NullStringUtil.equals(card.getName(),  withName.get(0).getName())) {
//                    if (withName.size() > 1 && withName.stream().anyMatch(Card::isCollectible))
//                         System.out.println(withName.get(0).getName());

                    Card.decideDelete(withName).forEach(File::delete);
                    withName.clear();
               }


               withName.add(card);
          });
     }
}