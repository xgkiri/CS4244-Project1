package state;

import cnf.Literal;

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