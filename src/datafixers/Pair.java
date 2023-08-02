package datafixers;

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


    @Override
    public String toString() {
        return "Pair[" + this.getFirst().toString() +"," + this.getSecond().toString() + "]";
    }

    public static <F, S> Pair<F, S> of(F f, S s) {
        return new Pair<>(f, s);
    }
}
