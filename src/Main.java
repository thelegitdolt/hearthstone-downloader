import util.Card;
import util.CardDownload;

import java.io.IOException;
import java.util.function.Predicate;

import static util.PredsUtil.*;

public class Main {



     public static void main(String[] args) throws IOException {
          Card.initializeCardList();

          Predicate<Card> condition = not(allTrue(IS_MERCENARY, IS_LOOT_CARD, IS_PUZZLE));

          CardDownload.downloadAllConditioned(Card.cardList, condition);
     }














}
