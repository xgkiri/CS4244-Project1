import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Literal implements Serializable{
    private static final long serialVersionUID = 1L;
    private final String symbol;
    private final boolean haveNot; 
    private int assignment;
    /* assignment = 0: False
     * assignment = 1: True
     * assignment = -1: No assignment 
     */

    Literal(String symbol, int assignment, boolean haveNot) {
        this.symbol = symbol;
        this.assignment = assignment;
        this.haveNot = haveNot;
    }

    boolean haveNot() {
        return this.haveNot;
    }

    void assign(int assignment) {
        this.assignment = assignment;
    }

    int getAssignment() {
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

    boolean noAssignment() {
        return this.assignment == -1;
    }

    boolean sameLiteral(Literal other) {
        // NOTE: just compare the "symbol" field, do not care about "haveNot" field
        return this.symbol.equals(other.symbol);
    }

    boolean isConflictLiteral() {
        return this.symbol.equals("#");
    }

    Literal assignmentReverse() {
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


class Clause implements Serializable{
    private static final long serialVersionUID = 1L;
    private Literal[] literals;

    Clause(Literal[] literals) {
        this.literals = literals;
    }

    boolean isFalse() {
        for(Literal literal : literals) {
            if(literal.getValue() != 0) {
                return false;
            }
        }
        return true;
    }

    boolean contains(Literal otherLiteral) {
        for(Literal literal : this.literals) {
            if(literal.sameLiteral(otherLiteral)) {
                return true;
            }
        }
        return false;
    }

    void assign(Literal literal, int value) {
        for(int i = 0; i < this.literals.length; i++) {
            if(this.literals[i].sameLiteral(literal)) {
                this.literals[i].assign(value);
            }
        }
    }

    Literal findUnassignedLiteral() {
        for(Literal literal : this.literals) {
            if(literal.noAssignment()) {
                return literal;
            }
        }
        return null;
    }

    Literal findPropagationLiteral() {
        int unassignedNumber = 0;
        for(Literal literal : this.literals) {
            if(literal.noAssignment()) {
                unassignedNumber += 1;
            }
            else if(literal.getValue() == 1) {
                return null;
            }
        }
        if(unassignedNumber == 1) {
            for(Literal literal : this.literals) {
                if(literal.noAssignment()) {
                    // NOTE: return assigned literal
                    if(literal.haveNot()) {
                        literal.assign(0);
                    }
                    else {
                        literal.assign(1);
                    }
                    return literal;
                }
            }
        }
        return null;
    }

    void clearAssignment() {
        for(Literal literal : this.literals) {
            literal.clearAssignment();
        }
    }

    @Override
    public String toString() {
        String clauseString = "[";
        for(int i = 0; i < this.literals.length; i++) {
            clauseString += this.literals[i].toString();
            if(i != this.literals.length - 1) {
                clauseString += ", ";
            }
        }
        clauseString += "]";
        return clauseString;
    }
}


class CNF implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Clause> clauses;

    CNF(ArrayList<Clause> clauses) {
        this.clauses = clauses;
    }

    void addClause(Clause clause) {
        this.clauses.add(clause);
    }

    void assign(Literal literal, int value) {
        // broadcast to each clause
        for(Clause clause : this.clauses) {
            clause.assign(literal, value);
        }
    }

    Literal findUnassignedLiteral() {
        for(Clause clause : this.clauses) {
            Literal literal = clause.findUnassignedLiteral();
            if(literal != null) {
                return literal;
            }
        }
        return null;
    }

    Clause findConflictClause() {
        for(Clause clause : this.clauses) {
            if(clause.isFalse()) {
                return clause;
            }
        }
        return null;
    }

    TraceUnit findPropagationUnit() {
        for(Clause clause : this.clauses) {
            Literal literal = clause.findPropagationLiteral();
            if(literal != null) {
                // NOTE: here just set level to 0
                TraceUnit traceUnit = new TraceUnit(literal, clause, 0);
                return traceUnit;
            }
        }
        return null;
    }

    void clearAssignment() {
        for(Clause clause : this.clauses) {
            clause.clearAssignment();
        }
    }

    @Override
    public String toString() {
        String cnfString = "{";
        for(int i = 0; i < this.clauses.size(); i++) {
            cnfString += this.clauses.get(i).toString();
            if(i != this.clauses.size() - 1) {
                cnfString += ", ";
            }
        }
        cnfString += "}";
        return cnfString;
    }
}

/*
* input string: 
* 1. cnf = clause1&cluase2 ...
* 2. clause = (r|!p|q ... )
*/

class CnfConstructor {
    private final Pattern clausePattern;
    private final Pattern symbolPattern;
    private final Pattern notPattern;

    CnfConstructor(){
        this.clausePattern = Pattern.compile("\\((.*)\\)");
        this.symbolPattern = Pattern.compile("!?(.*)");
        this.notPattern = Pattern.compile("!");
    }

    CNF string2CNF(String string) {
        ArrayList<Clause> clauseList = new ArrayList<Clause>();
        // 1. use "&" to split input string into several clauses
        String[] clauseStringList = string.split("&");
        for(String clauseString : clauseStringList) {
            Matcher clausMatcher = clausePattern.matcher(clauseString);
            clausMatcher.find();
            // 2. use "|" to split each clause into several literals
            String[] literalStringList = clausMatcher.group(1).split("\\|");
            Literal[] literalList = new Literal[literalStringList.length];
            for(int i = 0; i < literalStringList.length; i++) {
                // 3. construct CNF bottom up: literal -> clause -> CNF
                Matcher symbolMatcher = symbolPattern.matcher(literalStringList[i]);
                Matcher notMatcher = notPattern.matcher(literalStringList[i]);
                symbolMatcher.find();
                literalList[i] = new Literal(symbolMatcher.group(1), -1, notMatcher.find());
            }
            clauseList.add(new Clause(literalList));
        }
        return new CNF(clauseList);
    }

    @Override
    public String toString() {
        return "CNF constructor usage:\n1. clause1&cluase2 ...\n2. clause = (r|!p|q ... )";
    }
}