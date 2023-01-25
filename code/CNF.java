import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Literal {
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

    void assign(int assignment) {
        this.assignment = assignment;
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
        return this.symbol == other.symbol;
    }

    public String toString() {
        if(this.haveNot == false) {
            return this.symbol + "(" + this.assignment + ")";
        }
        else {
            return "(Not)" + this.symbol + "(" + this.assignment + ")";
        }
    }
}

class Clause {
    private Literal[] literals;

    Clause(Literal[] literals) {
        this.literals = literals;
    }

    boolean isFalse() {
        boolean flag = true;
        for(Literal literal : literals) {
            if(literal.getValue() != 0) {
                flag = false;
            }
        }
        return flag;
    }

    void assign(Literal literal, int value) {
        for(int i = 0; i < this.literals.length; i++) {
            if(this.literals[i].sameLiteral(literal)) {
                this.literals[i].assign(value);
            }
        }
    }

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

class CNF {
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

class CNFconstructor {
    private final Pattern clausePattern = Pattern.compile("\\((.*)\\)");
    private final Pattern symbolPattern = Pattern.compile("!?(.*)");
    private final Pattern notPattern = Pattern.compile("!");

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

    public String toString() {
        return "CNF constructor usage:\n1. clause1&cluase2 ...\n2. clause = (r|!p|q ... )";
    }
}