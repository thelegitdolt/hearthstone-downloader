package util;

import datafixers.Lambdas;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A util class for methods pertaining to interacting with files and directories
 * @author Dolt
 */
public class FileUtil {
    public static final String READTXT_FILEPATH = "/Users/drew/Desktop/hearthstone.json";
    public static final String CARD_FOLDER_FILEPATH = "/Users/drew/Desktop/Hearthstone Cards";
    public static final String SEARCH_FOLDER_FILEPATH = "/Users/drew/Desktop/Hearthstone Search Results";


    /**
     * A method that applies an action to all non-directory files in a folder.
     * @param directory - directory, as a File
     * @param action - the action to undertake
     */
    public static void mapDir(File directory, Lambdas.FileConsumer action, boolean print) {
        if (!directory.isDirectory())
            return;

        Set<File> files = Stream.of(Objects.requireNonNull(directory.listFiles()))
                .filter(file -> !file.isDirectory()).collect(Collectors.toSet());

        if (print) System.out.println("Queued " + files.size() + " files to process");

        int i = 0;
        for (File file : files) {
            try {
                action.apply(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            i++;
            if (i % 500 == 0)
                System.out.println(i);
        }
    }

    /**
     * A method that applies an action to all non-directory files in a folder.
     * @param path - the path of the file, as a String
     * @param action - the action to undertake
     */
    public static void mapDir(String path, Lambdas.FileConsumer action, boolean print) {
        mapDir(new File(path), action, print);
    }

}
