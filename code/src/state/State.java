package state;

import java.util.ArrayList;
import java.util.Comparator;
import cnf.Literal;

public class State {
    protected ArrayList<Score> scores;
    private final double gamma;
    // descending order
    private final Comparator<Score> comp = (x, y) -> {
        if (x.literal.noAssignment() && !y.literal.noAssignment()) {
            return -1;
        } else if (!x.literal.noAssignment() && y.literal.noAssignment()) {
            return 1;
        } else if (x.literal.noAssignment() && y.literal.noAssignment()) {
            if (y.score - x.score > 0) {
                return 1;
            } else if (y.score - x.score < 0) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    };

    public State(double gamma) {
        this.scores = new ArrayList<Score>();
        this.gamma = gamma;
    }

    public void add(Literal literal) {
        for (Score score : scores) {
            if (score.literal.sameLiteral(literal)) {
                score.score += 1;
                return;
            }
        }
        this.scores.add(new Score(literal));
    }

    public void inc(Literal literal) {
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

    public Literal getMax() {
        this.doSorting();
        /* 
        try {
            return this.scores.get(0).literal;
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
        */
        if (!this.scores.get(0).literal.noAssignment()) {
            return null;
        } else {
            return this.scores.get(0).literal;
        }
    }

    public void update() {
        for (Score score : scores) {
            score.score /= gamma;
        }
    }
    /* 
    void delete(Literal literal) {
        boolean exist = false;
        for (Score score : scores) {
            if (score.literal.sameLiteral(literal)) {
                exist = true;
                this.scores.remove(score);
                break;
            }
        }
        if (!exist) {
            throw new IllegalStateException("No such literal!");
        }
    }
    */
}
