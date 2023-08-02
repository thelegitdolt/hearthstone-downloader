package datafixers;

import java.io.File;
import java.io.IOException;

public class Lambdas {

    @FunctionalInterface
    public interface FileConsumer {
        void apply(File file) throws IOException;
    }

    @FunctionalInterface
    public interface TriConsumer<A, B, C> {
        void apply(A a, B b, C c);
    }
}
