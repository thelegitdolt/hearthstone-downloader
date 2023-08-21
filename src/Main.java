import core.Card;
import core.CardList;
import core.Search;
import util.FileUtil;
import util.NullStringUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Random;

public class Main {
     public static void main(String[] args) throws IOException {
          CardList.initialize();

          List<Card> duelsHeroList = Search.filterCards(c -> NullStringUtil.contains(c.getId(), "PVPDR_Hero"));




     }
}