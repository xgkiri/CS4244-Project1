import java.util.ArrayList;

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
        while(this.cnf.findPropagationUnit() != null) {
            TraceUnit traceUnit = this.cnf.findPropagationUnit();
            this.trace.addTraceUnit(traceUnit.setLevel(this.currentLevel));
            broadcastAssign(traceUnit.getLiteral(), traceUnit.getLiteral().getValue());
        }
    }

    boolean findConflict() {
        return this.cnf.haveConflict();
    }

    void learnFromConflict() {  
        // learn new clause and add it into CNF

    }
    
    void backtrack() {
        // when conflict occur, after learning from it, go back to certain level
    }
}
