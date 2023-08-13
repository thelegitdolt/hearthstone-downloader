import core.Card;
import core.CardList;
import util.NullStringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


     public static void main(String[] args) throws IOException {
          CardList.initializeWith("CORE_EX1_603", "EX1_603", "VAN_EX1_603", "CS2_073");

          System.out.println(CardList.get().size());
          List<Card> withName = new ArrayList<>();

          CardList.forEach((card) -> {
               if (card.getImagePath().isEmpty())
                    return;

               if ((withName.size() > 0 && !NullStringUtil.equals(card.getName(),  withName.get(0).getName())) ) {
//                    if (withName.size() > 1 && withName.stream().anyMatch(Card::isCollectible))
//                         System.out.println(withName.get(0).getName());

                    Card.decideDelete(withName).forEach(file -> System.out.println(file.getName()));
                    withName.clear();
               }


               withName.add(card);
          });
     }
}