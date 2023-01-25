import java.util.ArrayList;

class Solver {
    private CNF cnf;
    private Trace trace;
    
    Solver(CNF cnf) {
        this.cnf = cnf;
        this.trace = new Trace(new ArrayList<TraceUnit>());
    }

    CNF solveSAT() {
        // main method to solve SAT problem
    }

    Literal findUnassignedLiteral() {
        // NOTE: can be improved by using a better heuristic?
        // see 2.(1) PickBranchingVariable
        return this.cnf.findUnassignedLiteral();
    }

    void unitPropagation(Literal literal) {
        // do unit propagation after an assignment
        this.cnf.assign(literal, literal.getValue());
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
