import core.Card;
import core.CardList;
import util.FileUtil;

import java.io.File;
import java.io.IOException;

public class Main {
     public static void main(String[] args) throws IOException {
          CardList.initialize();
          FileUtil.mapDirConditioned(FileUtil.CARD_FOLDER_FILEPATH, File::delete,
                  (file) -> Card.lookup(file).orElse(Card.NULL_CARD).getId().endsWith("_G"), true);



     }
}