package util;

import java.io.File;
import java.io.IOException;

public class Lambdas {

    @FunctionalInterface
    public interface FileConsumer {
        void apply(File file) throws IOException;
    }
}
