import cnf.CNF;
import cnf.CNFConstructorDIMACS;
import cnf.CNFConstructorString;
import solver.Solver;
import java.util.Scanner;

class Main {
    static final double GAMMA = 2;
    public static void main(String[] args) {
        CNF cnf;
        Scanner sc = new Scanner(System.in);
        System.out.println("choose the mode:\n" + 
            "* enter '1' : string format(Debug)\n" + 
            "* enter '2' : DIMACS format");
        int mode = sc.nextInt();
        if (mode == 1) {
            System.out.println("---string---");
            System.out.println("Please enter the formula in format:\n" + 
            "* 1. formula = clause1&cluase2 ...\n" + 
            "* 2. clause = (r|!p|q ... )\n" + 
            "* e.g. (x1|x2)&(!x2|x3)");
            String cnfString = sc.nextLine();
            sc.close();
            CNFConstructorString cons = new CNFConstructorString();
            cnf = cons.string2CNF(cnfString);
        } else {
            sc.nextLine();
            CNFConstructorDIMACS cons = new CNFConstructorDIMACS();
            System.out.println("---DIMACS---");
            System.out.println("Please enter the formula in DIMACS format:");
            String[] info = sc.nextLine().split(" ");
            int numOfClause = Integer.parseInt(info[info.length - 1]);
            for (int i = 0; i < numOfClause; i++) {
                String line = sc.nextLine();
                cons.processOneLine(line); 
            }
            cnf = cons.get();
        }
        
        Solver so = new Solver(cnf, GAMMA);
        String result = so.solveSAT();
        if (result == null) {
            System.out.println("UNSAT");
        } else {
            System.out.println("SAT");
            System.out.println(result);
        }
    }
}