import java.util.ArrayList;

class TraceUnit {
    private final Literal literal;
    private final Clause clause;
    // decision unit: clause = null 
    private final int level;

    TraceUnit(Literal literal, Clause clause, int level) {
        this.literal = literal;
        this.clause = clause;
        this.level = level;
    }

    Literal getLiteral() {
        return this.literal;
    }

    TraceUnit setLevel(int level) {
        return new TraceUnit(this.literal, this.clause, level);
    }

    boolean levelMoreThan(int level) {
        return this.level > level;
    }

    boolean atLevel(int level) {
        return this.level == level;
    }

    boolean isDecisionUnit() {
        return this.clause == null;
    }

    boolean isDirectSuccessorOf(TraceUnit traceUnit) {
        return this.clause.contains(traceUnit.literal) && 
              !this.literal.sameLiteral(traceUnit.literal);
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
        // remove traceUnit whose level is more than the given one
        for(TraceUnit traceUnit : this.traceUnits) {
            if(traceUnit.levelMoreThan(level)) {
                this.traceUnits.remove(traceUnit);
            }
        }
    }

    TraceUnit findUIP() {
        // simple way: use the current level's decision unit
        for(TraceUnit traceUnit : this.traceUnits) {
            if(traceUnit.atLevel(currentLevel) && traceUnit.isDecisionUnit()) {
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

    ArrayList<TraceUnit> findSuccessor(TraceUnit traceUnit) {
        // use DFS to find successors of the given trace unit (in a multi-connected directed graph)
        ArrayList<TraceUnit> successorList = new ArrayList<TraceUnit>();
        DFS(successorList, traceUnit);
        return successorList;
    }

    void DFS(ArrayList<TraceUnit> successorList, TraceUnit traceUnit) {
        ArrayList<TraceUnit> directSuccessorList = findDirectSuccessor(traceUnit);
        if(directSuccessorList.isEmpty()) {
            return;
        }
        else {
            for(TraceUnit directSuccessor : directSuccessorList) {
                // multi-connected graph
                if(!successorList.contains(directSuccessor)) {
                    successorList.add(directSuccessor);
                    DFS(successorList, directSuccessor);
                }
            }
        }
    }

    ArrayList<TraceUnit> findDirectSuccessor(TraceUnit ancestor) {
        ArrayList<TraceUnit> directSuccessorList = new ArrayList<TraceUnit>();
        for(TraceUnit traceUnit : this.traceUnits) {
            if(traceUnit.isDirectSuccessorOf(ancestor)) {
                directSuccessorList.add(traceUnit);
            }
        }
        return directSuccessorList;
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