package cnf;

public class Literal {
    private final String symbol;
    private final boolean haveNot; 
    private int assignment;
    /* assignment = 0: False
     * assignment = 1: True
     * assignment = -1: No assignment 
     */

    public Literal(String symbol, int assignment, boolean haveNot) {
        this.symbol = symbol;
        this.assignment = assignment;
        this.haveNot = haveNot;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public boolean haveNot() {
        return this.haveNot;
    }

    void assign(int assignment) {
        this.assignment = assignment;
    }

    public int getAssignment() {
        return this.assignment;
    }

    int getValue() {
        if(this.assignment == -1) {
            return -1;
        }
        else if(this.haveNot == false) {
            return this.assignment;
        }
        else {
            return 1 - this.assignment;
        }
    }

    public boolean noAssignment() {
        return this.assignment == -1;
    }

    public boolean sameLiteral(Literal other) {
        // NOTE: just compare the "symbol" field, do not care about "haveNot" field
        return this.symbol.equals(other.symbol);
    }

    public boolean isConflictLiteral() {
        return this.symbol.equals("#");
    }

    public Literal assignmentReverse() {
        if(this.assignment == 1) {
            return new Literal(this.symbol, -1, false);
        }
        else if(this.assignment == 0) {
            return new Literal(this.symbol, -1, true);
        }
        else {
            return null;
        }
    }

    void clearAssignment() {
        this.assignment = -1;
    }

    @Override
    public String toString() {
        if(this.haveNot == false) {
            return this.symbol + "(" + this.assignment + ")";
        }
        else {
            return "(Not)" + this.symbol + "(" + this.assignment + ")";
        }
    }
}
