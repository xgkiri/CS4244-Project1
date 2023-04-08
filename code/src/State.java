import java.util.ArrayList;
import java.util.Comparator;

class Score {
    protected final Literal literal;
    protected double score;

    Score(Literal literal) {
        this(literal, 0);
    }

    Score(Literal literal, double score) {
        this.literal = literal;
        this.score = score;
    }

    boolean equalsTo(Score other) {
        return this.literal.sameLiteral(other.literal);
    }
}

class State {
    protected ArrayList<Score> scores;
    private final double gamma;
    // descending order
    private final Comparator<Score> comp = (x, y) -> (y.score - x.score) > 0 ? 1 : -1;

    State(double gamma) {
        this.scores = new ArrayList<Score>();
        this.gamma = gamma;
    }

    void add(Literal literal) {
        for (Score score : scores) {
            if (score.literal.sameLiteral(literal)) {
                score.score += 1;
            }
        }
        this.scores.add(new Score(literal));
    }

    void inc(Literal literal) {
        boolean exist = false;
        for (Score score : scores) {
            if (score.literal.sameLiteral(literal)) {
                exist = true;
                score.score += 1;
            }
        }
        if (!exist) {
            throw new IllegalStateException("No such literal!");
        }
    }

    void doSorting() {
        this.scores.sort(comp);
    }

    Literal getMax() {
        this.doSorting();
        try {
            return this.scores.get(0).literal;
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    void update() {
        for (Score score : scores) {
            score.score /= gamma;
        }
    }

    void delete(Literal literal) {
        boolean exist = false;
        for (Score score : scores) {
            if (score.literal.sameLiteral(literal)) {
                exist = true;
                this.scores.remove(score);
            }
        }
        if (!exist) {
            throw new IllegalStateException("No such literal!");
        }
    }
}
