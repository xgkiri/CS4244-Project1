package cnf;
import java.util.ArrayList;


public class CNFConstructorDIMACS {
    private CNF cnf;

    public CNFConstructorDIMACS() {
        this.cnf = new CNF(new ArrayList<Clause>());
    }

    public void processOneLine(String line) {
        ArrayList<Literal> literalList = new ArrayList<Literal>();
        String[] literals = line.split(" ");
        for (int i = 0; i < literals.length - 1; i++) {
            String literal = literals[i];
            if (literal.contains("-")) {
                String pos = literal.substring(1);
                literalList.add(new Literal(pos, -1, true));
            } else {
                literalList.add(new Literal(literal, -1, false));
            }
        }
        this.cnf.addClause(new Clause(literalList));
    }

    public CNF get() {
        return this.cnf;
    }
}
