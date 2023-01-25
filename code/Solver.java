import java.util.ArrayList;

class Solver {
    private CNF cnf;
    private int level;
    private Trace trace;
    
    Solver(CNF cnf) {
        this.cnf = cnf;
        this.level = 0;
        this.trace = new Trace(new ArrayList<TraceUnit>());
    }

    CNF solveSAT() {
        // main method to solve SAT problem
    }

    void unitPropagation() {
        // do unit propagation after an assignment
    }

    void learnFromConflict() {  
        // learn new clause and add it into CNF
    }
    
    void backtrack() {
        // when conflict occur, after learning from it, go back to certain level
    }
}
