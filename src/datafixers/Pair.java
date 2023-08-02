package datafixers;

import java.util.Objects;

/**
 * Pair class that holds 2 values of any class
 * @param <F> First data type
 * @param <S> Second datatype
 * @author Dolt
 */
public class Pair<F, S> {
    private F f;
    private S s;

    public Pair(F f, S s) {
        this.f = f;
        this.s = s;
    }

    public F getFirst() {
        return f;
    }

    public S getSecond() {
        return s;
    }

    public void setFirst(F f) {
        this.f = f;
    }

    public void setSecond(S s) {
        this.s = s;
    }


    /**
     * Average toString implementation
     * @return a String with separated entries on the pair's first and second value, processed by toString
     */
    @Override
    public String toString() {
        return "Pair[" + this.getFirst().toString() +"," + this.getSecond().toString() + "]";
    }


    @Override
    public int hashCode() {
        return Objects.hash(f, s);
    }

    /**
     * .of() implementation, because I hate constructors
     */
    public static <F, S> Pair<F, S> of(F f, S s) {
        return new Pair<>(f, s);
    }
}
