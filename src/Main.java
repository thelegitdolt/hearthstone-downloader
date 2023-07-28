import util.Card;
import util.PredsUtil;

import java.io.IOException;

public class Main {



     public static void main(String[] args) throws IOException {
          Card.initializeCardList();

          for (Card card : Card.cardList.stream().filter(PredsUtil.IS_PUZZLE).toList()) {
               System.out.println(card);
          }

     }














}
