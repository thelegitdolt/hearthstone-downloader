package core;

import util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Search {
    public static void search(Predicate<Card> criteria) {
        moveFiles(Card.cardList.stream().filter(criteria).toList());
    }

    private static void moveFiles(List<Card> toMove) {
        FileUtil.mapDir(FileUtil.SEARCH_FOLDER_FILEPATH, File::delete);

        for (Card card : toMove) {
            BufferedImage img = null;
            try {
                File file = card.getImage();
                img = ImageIO.read(card.getImage());
            }
            catch (IOException ignored) {}

            Objects.requireNonNull(img);

            try {
                ImageIO.write(img, "png", new File(FileUtil.SEARCH_FOLDER_FILEPATH + "/" + card.getName() + " " + card.getId() + ".png"));
            }
            catch (IOException ignored) {}
        }
    }
}
