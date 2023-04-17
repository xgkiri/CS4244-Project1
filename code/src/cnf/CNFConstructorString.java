package cnf;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* input string: 
* 1. cnf = clause1&cluase2 ...
* 2. clause = (r|!p|q ... )
*/

public class CNFConstructorString {
    private final Pattern clausePattern;
    private final Pattern symbolPattern;
    private final Pattern notPattern;

    public CNFConstructorString(){
        this.clausePattern = Pattern.compile("\\((.*)\\)");
        this.symbolPattern = Pattern.compile("!?(.*)");
        this.notPattern = Pattern.compile("!");
    }

    public CNF string2CNF(String string) {
        ArrayList<Clause> clauseList = new ArrayList<Clause>();
        // 1. use "&" to split input string into several clauses
        String[] clauseStringList = string.split("&");
        for(String clauseString : clauseStringList) {
            Matcher clausMatcher = clausePattern.matcher(clauseString);
            clausMatcher.find();
            // 2. use "|" to split each clause into several literals
            String[] literalStringList = clausMatcher.group(1).split("\\|");
            ArrayList<Literal> literalList = new ArrayList<Literal>();
            for(int i = 0; i < literalStringList.length; i++) {
                // 3. construct CNF bottom up: literal -> clause -> CNF
                Matcher symbolMatcher = symbolPattern.matcher(literalStringList[i]);
                Matcher notMatcher = notPattern.matcher(literalStringList[i]);
                symbolMatcher.find();
                literalList.add(new Literal(symbolMatcher.group(1), -1, notMatcher.find()));
            }
            clauseList.add(new Clause(literalList));
        }
        return new CNF(clauseList);
    }

    @Override
    public String toString() {
        return "CNF constructor usage:\n1. clause1&cluase2 ...\n2. clause = (r|!p|q ... )";
    }
}
