import java.util.ArrayList;

class TraceUnit {
    private final Literal literal;
    private final Clause clause;
    // decision node: clause = null 
    private final int level;

    TraceUnit(Literal literal, Clause clause, int level) {
        this.literal = literal;
        this.clause = clause;
        this.level = level;
    }

    boolean levelMoreThan(int level) {
        return this.level > level;
    }

    boolean atLevel(int level) {
        return this.level == level;
    }

    boolean isDecisionNode() {
        return this.clause == null;
    }

    public String toString() {
        return this.literal.toString() + this.clause.toString() + " level = " + this.level;
    }
}

class Trace {
    private ArrayList<TraceUnit> traceUnits;
    private int currentLevel;

    Trace(ArrayList<TraceUnit> traceUnits) {
        this.traceUnits = traceUnits;
        this.currentLevel = 0;
    }

    void addTraceUnit(TraceUnit traceUnit) {
        this.traceUnits.add(traceUnit);
    }

    void removeTraceUnitLevel(int level) {
        // remove traceUnit whose level is more than given one
        for(TraceUnit traceUnit : this.traceUnits) {
            if(traceUnit.levelMoreThan(level)) {
                this.traceUnits.remove(traceUnit);
            }
        }
    }

    TraceUnit findUIP() {
        // simple way: use the current level's decision node
        for(TraceUnit traceUnit : this.traceUnits) {
            if(traceUnit.atLevel(currentLevel) && traceUnit.isDecisionNode()) {
                return traceUnit;
            }
        }
        return null;
        /*
         * NOTE: 
         * improvement can be made by finding first UIP
         * see 2.(2) ConflictAnalysis
         */ 

    }

    public String toString() {
        String traceString = "{";
        for(int i = 0; i < this.traceUnits.size(); i++) {
            traceString += this.traceUnits.get(i).toString();
            if(i != this.traceUnits.size() - 1) {
                traceString += ", ";
            }
        }
        traceString += "}";
        return traceString;
    }
}