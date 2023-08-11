package datafixers;

import java.io.File;
import java.io.IOException;

/**
 * A class holding all the various lambdas that I use that are not provided by Java natively
 * @author Dolt
 */
public class Lambdas {

    /**
     * A Consumer<File></File> that can throw an IOException
     * Useful, since almost all methods that transform files throw an IOException
     */
    @FunctionalInterface
    public interface FileConsumer {
        void apply(File file) throws IOException;
    }

    /**
     * A lambda that takes in three of any datatypes, with no return type
     * I swear TriConsumer was a thing, but they removed it
     */
    @FunctionalInterface
    public interface TriConsumer<A, B, C> {
        void apply(A a, B b, C c);
    }


    @FunctionalInterface
    public interface BiPredicate<A, B> {
        boolean test(A a, B b);
    }
}
