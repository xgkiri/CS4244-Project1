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

    int getLevel() {
        return this.level;
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

    boolean isConflictUnit() {
        return this.literal.isConflictLiteral(); 
    }

    boolean isDirectSuccessorOf(TraceUnit traceUnit) {
        if(this.clause == null) {
            return false;
        }
        else {
            return this.clause.contains(traceUnit.literal) && 
                !this.literal.sameLiteral(traceUnit.literal);
        }
    }

    boolean isDirectAncestorOf(TraceUnit traceUnit) {
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

class Trace {
    private ArrayList<TraceUnit> traceUnits;

    Trace(ArrayList<TraceUnit> traceUnits) {
        this.traceUnits = traceUnits;
    }

    ArrayList<TraceUnit> getTraceUnits() {
        return this.traceUnits;
    }

    void addTraceUnit(TraceUnit traceUnit) {
        this.traceUnits.add(traceUnit);
    }

    void removeTraceUnitLevel(int level) {
        // remove traceUnit whose level is more than the given one
        ArrayList<TraceUnit> newList = new ArrayList<TraceUnit>();
        for(TraceUnit traceUnit : this.traceUnits) {
            // do not use "remove" here, will cause ConcurrentModificationException
            // create a new list instead
            if(!traceUnit.levelMoreThan(level)) {
                newList.add(traceUnit);
            }
        }
        this.traceUnits = newList;
    }

    TraceUnit findUIP(int currentLevel) {
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

    TraceUnit findConflictUnit() {
        for(TraceUnit traceUnit : this.traceUnits) {
            if(traceUnit.isConflictUnit()) {
                return traceUnit;
            }
        }
        return null;
    }

    ArrayList<TraceUnit> findSuccessor(TraceUnit traceUnit) {
        // use DFS to find successors of the given trace unit (in a multi-connected directed graph)
        ArrayList<TraceUnit> successorList = new ArrayList<TraceUnit>();
        dfsForSuccessor(successorList, traceUnit);
        return successorList;
    }

    void dfsForSuccessor(ArrayList<TraceUnit> successorList, TraceUnit traceUnit) {
        ArrayList<TraceUnit> directSuccessorList = findDirectSuccessor(traceUnit);
        if(directSuccessorList.isEmpty()) {
            return;
        }
        else {
            for(TraceUnit directSuccessor : directSuccessorList) {
                // multi-connected graph
                if(!successorList.contains(directSuccessor)) {
                    successorList.add(directSuccessor);
                    dfsForSuccessor(successorList, directSuccessor);
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

    ArrayList<TraceUnit> findAncestor(TraceUnit traceUnit) {
        ArrayList<TraceUnit> ancestorList = new ArrayList<TraceUnit>();
        dfsForAncestor(ancestorList, traceUnit);
        return ancestorList;
    }

    void dfsForAncestor(ArrayList<TraceUnit> ancestorList, TraceUnit traceUnit) {
        ArrayList<TraceUnit> directAncestorListList = findDirectAncestor(traceUnit);
        if(directAncestorListList.isEmpty()) {
            return;
        }
        else {
            for(TraceUnit directAncestor : directAncestorListList) {
                if(!ancestorList.contains(directAncestor)) {
                    ancestorList.add(directAncestor);
                    dfsForAncestor(ancestorList, directAncestor);
                }
            }
        }
    }

    ArrayList<TraceUnit> findDirectAncestor(TraceUnit ancestor) {
        ArrayList<TraceUnit> directAncestorList = new ArrayList<TraceUnit>();
        for(TraceUnit traceUnit : this.traceUnits) {
            if(traceUnit.isDirectAncestorOf(ancestor)) {
                directAncestorList.add(traceUnit);
            }
        }
        return directAncestorList;
    }

    ArrayList<TraceUnit> getIntersection(ArrayList<TraceUnit> p, ArrayList<TraceUnit> q) {
        ArrayList<TraceUnit> intersection = new ArrayList<TraceUnit>();
        for(TraceUnit traceUnit : p) {
            if(q.contains(traceUnit)) {
                intersection.add(traceUnit);
            }
        }
        return intersection;
    }

    ArrayList<TraceUnit> getComplement(ArrayList<TraceUnit> listToExclude) {
        ArrayList<TraceUnit> complementList = new ArrayList<TraceUnit>();
        for(TraceUnit traceUnit : this.traceUnits) {
            if(!listToExclude.contains(traceUnit)) {
                complementList.add(traceUnit);
            }
        }
        return complementList;
    }

    @Override
    public String toString() {
        String traceString = "{";
        for(int i = 0; i < this.traceUnits.size(); i++) {
            traceString += this.traceUnits.get(i).toString();
            if(i != this.traceUnits.size() - 1) {
                traceString += ",\n";
            }
        }
        traceString += "}";
        return traceString;
    }
}