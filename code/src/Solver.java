import java.util.ArrayList;

class Solver {
    private CNF cnf;
    Trace trace;
    private int currentLevel;
    private State state;
    
    Solver(CNF cnf, double gamma) {
        this.cnf = cnf;
        this.trace = new Trace(new ArrayList<TraceUnit>());
        this.currentLevel = 0;
        this.state = new State(gamma);
    }
    
    String solveSAT() {
        // main method to solve SAT problem
        // CNF cnfCopy = CloneUnit.deepClone(this.cnf);
        initState();
        while(true) {
            unitPropagation();
            if(findConflict()) {
                if(this.currentLevel == 0) {
                    // UNSAT
                    // return null;
                    return null;
                }
                else {
                    backTrack(learnFromConflict());
                    continue;
                }
            }
            // make decision
            if(doOneDecision() == true) {
                // have unassigned literal
                continue;
            }
            else {
                // have no unassigned literal --> SAT solved
                // cnfCopy = reAssignFromTrace(cnfCopy , this.trace);
                // return cnfCopy;
                return getResultFromTrace(this.trace);
            }
        }
    }

    void initState() {
        for (Clause clause : this.cnf.getClauses()) {
            for (Literal literal : clause.getLiterals()) {
                this.state.add(literal);
            }
        }
    }

    // TODO: modify this part: 
    //       1. using the literal in state with the max score,
    //          then need to delete it from state
    //       2. add a counter to update the state periodly
    boolean doOneDecision() {
        Literal literal = findUnassignedLiteral();
        if(literal == null) {
            return false;
        }
        else {
            currentLevel += 1;
            broadcastAssign(literal);
            TraceUnit traceUnit = new TraceUnit(literal, null, currentLevel);
            this.trace.addTraceUnit(traceUnit);
            return true;
        }
    }

    Literal findUnassignedLiteral() {
        // NOTE: can be improved by using a better heuristic?
        // see 2.(1) PickBranchingVariable
        return this.cnf.findUnassignedLiteral();
    }

    void broadcastAssign(Literal literal, int value) {
        this.cnf.assign(literal, value);
    }

    void broadcastAssign(Literal literal) {
        if(literal.haveNot()) {
            this.cnf.assign(literal, 0);
        }
        else {
            this.cnf.assign(literal, 1);
        }
    }

    void unitPropagation() {
        // do unit propagation after an assignment
        while(true) {
            TraceUnit traceUnit = this.cnf.findPropagationUnit();
            if(traceUnit == null) {
                break;
            }
            else{
                this.trace.addTraceUnit(traceUnit.setLevel(this.currentLevel));
                broadcastAssign(traceUnit.getLiteral(), traceUnit.getLiteral().getAssignment());
            }
        }
    }

    boolean findConflict() {
        Clause conflictClause = this.cnf.findConflictClause();
        if(conflictClause != null) {
            this.trace.addTraceUnit(
                new TraceUnit(new Literal("#", -1, false), 
                conflictClause, currentLevel));
            return true;
        }
        else {
            return false;
        }
    }

    int learnFromConflict() {  
        // learn new clause and add it into CNF
        TraceUnit UIP = this.trace.findUIP(this.currentLevel);
        TraceUnit conflictUnit = this.trace.findConflictUnit();
        ArrayList<TraceUnit> successorList = this.trace.findSuccessor(UIP);
        ArrayList<TraceUnit> ancestorList = this.trace.findAncestor(conflictUnit);
        ArrayList<TraceUnit> intersection = this.trace.getIntersection(successorList, ancestorList);
        ArrayList<TraceUnit> complementList = this.trace.getComplement(intersection);
        return getConflictReason(complementList, intersection);
    }

    int getConflictReason(ArrayList<TraceUnit> complementList, ArrayList<TraceUnit> successorList) {
        // 1. learn new clause and add it into cnf
        // 2. return the level backtrack to
        ArrayList<Literal> literals = new ArrayList<Literal>();
        boolean sameFlag = true;
        boolean inFlag = false;
        int maxLevel = 0;
        int secondLevel = 0;
        int recordLevel = -1;
        for(TraceUnit compTU : complementList) {
            inFlag = false;
            // find the trace units cause the conflict
            for(TraceUnit succTU : successorList) {
                if(succTU.isDirectSuccessorOf(compTU)) {
                    inFlag = true;
                    break;
                }
            }
            if(inFlag == true) {
                // check if the level of all trace units is the same
                if(recordLevel == -1) {
                    recordLevel = compTU.getLevel();
                }
                else if(recordLevel != compTU.getLevel()) {
                    sameFlag = false;
                }
                // record the max level and second level
                if(compTU.getLevel() > maxLevel) {
                    secondLevel = maxLevel;
                    maxLevel = compTU.getLevel();
                }
                else if(compTU.getLevel() > secondLevel) {
                    secondLevel = compTU.getLevel();
                }
                // reverse literal
                literals.add(compTU.getLiteral().assignmentReverse());
            }
        }
        // create new clause and add it into cnf
        // NOTE: here we increase the score of literals in new-learned clause
        Literal[] literalsOfClause = new Literal[literals.size()];
        for(int i = 0; i < literals.size(); i++) {
            literalsOfClause[i] = literals.get(i);
            state.inc(literals.get(i));
        }
        this.cnf.addClause(new Clause(literalsOfClause));
        // return the level to go back
        if(sameFlag == false) {
            return secondLevel;
        }
        else {
            return 0;
        }
    }
    
    void backTrack(int level) {
        // when conflict occur, after learning from it, go back to certain level
        this.trace.removeTraceUnitLevel(level);
        this.currentLevel = level;
        reAssignFromTrace(this.trace);
    }

    void reAssignFromTrace(Trace trace) {
        CNF cnfCopy = CloneUnit.deepClone(this.cnf);
        cnfCopy.clearAssignment();
        this.cnf = cnfCopy;
        for(TraceUnit traceUnit : trace.getTraceUnits()) {
            this.cnf.assign(traceUnit.getLiteral(), traceUnit.getLiteral().getAssignment());
        }
    }

    CNF reAssignFromTrace(CNF cnf, Trace trace) {
        cnf.clearAssignment();
        for(TraceUnit traceUnit : trace.getTraceUnits()) {
            cnf.assign(traceUnit.getLiteral(), traceUnit.getLiteral().getAssignment());
        }
        return cnf;
    }

    String getResultFromTrace(Trace trace) {
        String result = "[";
        for(TraceUnit traceUnit : trace.getTraceUnits()) {
            result += traceUnit.getLiteral().getSymbol().toString() + ": ";
            if (traceUnit.getLiteral().getAssignment() == 1) {
                result += "TRUE, ";
            } else if (traceUnit.getLiteral().getAssignment() == 0) {
                result += "FALSE, ";
            } else {
                throw new IllegalStateException();
            }
        }
        result = result.substring(0, result.length() - 2);
        result += "]";
        return result;
    }

    @Override
    public String toString() {
        return "\n---CNF---\n" + this.cnf.toString() + "\n\n" + 
                    "---trace---\n" + this.trace.toString() + "\n\n" + 
                    "---current level---\n" + this.currentLevel;
    }
}
