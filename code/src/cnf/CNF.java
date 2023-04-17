package cnf;

import java.util.ArrayList;
import trace.TraceUnit;

public class CNF {
    private ArrayList<Clause> clauses;

    CNF(ArrayList<Clause> clauses) {
        this.clauses = clauses;
    }

    public ArrayList<Clause> getClauses() {
        return this.clauses;
    }

    public void addClause(Clause clause) {
        this.clauses.add(clause);
    }

    public void assign(Literal literal, int value) {
        // broadcast to each clause
        for(Clause clause : this.clauses) {
            clause.assign(literal, value);
        }
    }

    public Literal findUnassignedLiteral() {
        for(Clause clause : this.clauses) {
            Literal literal = clause.findUnassignedLiteral();
            if(literal != null) {
                return literal;
            }
        }
        return null;
    }

    public Clause findConflictClause() {
        for(Clause clause : this.clauses) {
            if(clause.isFalse()) {
                return clause;
            }
        }
        return null;
    }

    public TraceUnit findPropagationUnit() {
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

    public void clearAssignment() {
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

