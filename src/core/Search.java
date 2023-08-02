package core;

import util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class Search {
    public static void search(Predicate<Card> criteria) {
        moveFiles(Card.cardList.stream().filter(criteria).toList());
    }

    private static void moveFiles(List<Card> toMove) {
        new File(FileUtil.SEARCH_FOLDER_FILEPATH).mkdirs();
        clearFolder();

        for (Card card : toMove) {
            BufferedImage img = null;
            try {
                if (card.getImagePath().isPresent())
                    img = ImageIO.read(card.getImagePath().get());
                else
                    continue;
            }
            catch (IOException ignored) {}


            try {
                assert img != null;
                ImageIO.write(img, "png", new File(FileUtil.SEARCH_FOLDER_FILEPATH + "/" + card.getName() + " " + card.getId() + ".png"));
            }
            catch (IOException ignored) {}
        }
    }

    public static void clearFolder() {
        FileUtil.mapDir(FileUtil.SEARCH_FOLDER_FILEPATH, File::delete, false);
    }
}
