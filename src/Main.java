import core.Card;
import core.Search;
import util.NullStringUtil;
import values.CardType;

import java.io.IOException;

public class Main {


     public static void main(String[] args) throws IOException {

          Card.initializeCardList();
          Search.search( (card) ->
                  !card.hasText() && card.getType() == CardType.HERO);

     }
}