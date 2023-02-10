Solver createSolver() {
    String s = "(!a|!b)&(y|b|c)&(!z|b)&(!x|!z|a)&(!x|z|a)&(z|b)";
    CnfConstructor cons = new CnfConstructor();
    CNF cnf = cons.string2CNF(s);
    return new Solver(cnf);
}