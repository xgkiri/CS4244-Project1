import java.util.ArrayList;
import java.io.*;

class Solver {
    private CNF cnf;
    private Trace trace;
    private int currentLevel;
    
    Solver(CNF cnf) {
        this.cnf = cnf;
        this.trace = new Trace(new ArrayList<TraceUnit>());
        this.currentLevel = 0;
    }
    /* 
    CNF solveSAT() {
        // main method to solve SAT problem
    }
    */
    boolean doOneDecision() {
        Literal literal = findUnassignedLiteral();
        if(literal == null) {
            return false;
        }
        else {
            currentLevel += 1;
            // NOTE: always set value = 1?
            broadcastAssign(literal, 1);
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

    void unitPropagation() {
        // do unit propagation after an assignment
        while(true) {
            TraceUnit traceUnit = this.cnf.findPropagationUnit();
            if(traceUnit == null) {
                break;
            }
            else{
                this.trace.addTraceUnit(traceUnit.setLevel(this.currentLevel));
                broadcastAssign(traceUnit.getLiteral(), traceUnit.getLiteral().getValue());
            }
        }
    }

    boolean findConflict() {
        return this.cnf.haveConflict();
    }

    int learnFromConflict() {  
        // learn new clause and add it into CNF
        TraceUnit UIP = this.trace.findUIP(this.currentLevel);
        ArrayList<TraceUnit> successorList = this.trace.findSuccessor(UIP);
        ArrayList<TraceUnit> complementList = this.trace.getComplement(successorList);
        return getConflictReason(complementList, successorList);
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
                literals.add(compTU.getLiteral().reverse());
            }
        }
        // create new clause and add it into cnf
        Literal[] literalsOfClause = new Literal[literals.size()];
        for(int i = 0; i < literals.size(); i++) {
            literalsOfClause[i] = literals.get(i);
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

    }

    public String toString() {
        return "\n---CNF---\n" + this.cnf.toString() + "\n\n" + 
                    "---trace---\n" + this.trace.toString() + "\n\n" + 
                    "---current level---\n" + this.currentLevel;
    }
}
