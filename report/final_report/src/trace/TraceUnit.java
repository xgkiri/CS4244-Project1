package trace;

import cnf.Clause;
import cnf.Literal;

public class TraceUnit {
    private final Literal literal;
    private final Clause clause;
    // decision unit: clause = null 
    private final int level;

    public TraceUnit(Literal literal, Clause clause, int level) {
        this.literal = literal;
        this.clause = clause;
        this.level = level;
    }

    public Literal getLiteral() {
        return this.literal;
    }

    public int getLevel() {
        return this.level;
    }

    public TraceUnit setLevel(int level) {
        return new TraceUnit(this.literal, this.clause, level);
    }

    public boolean levelMoreThan(int level) {
        return this.level > level;
    }

    public boolean atLevel(int level) {
        return this.level == level;
    }

    public boolean isDecisionUnit() {
        return this.clause == null;
    }

    public boolean isConflictUnit() {
        return this.literal.isConflictLiteral(); 
    }

    public boolean isDirectSuccessorOf(TraceUnit traceUnit) {
        if(this.clause == null) {
            return false;
        }
        else {
            return this.clause.contains(traceUnit.literal) && 
                !this.literal.sameLiteral(traceUnit.literal);
        }
    }

    public boolean isDirectAncestorOf(TraceUnit traceUnit) {
        return traceUnit.isDirectSuccessorOf(this);
    }

    @Override
    public String toString() {
        String traceUnitString = this.literal.toString();
        if(this.clause == null) {
            traceUnitString += "[Decision Unit]";    
        }
        else {
            traceUnitString += this.clause.toString();
        }
        traceUnitString += (" level = " + this.level);
        return traceUnitString;
    }
}
