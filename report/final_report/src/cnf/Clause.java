package cnf;

import java.util.ArrayList;

public class Clause {
    private ArrayList<Literal> literals;

    public Clause(ArrayList<Literal> literals) {
        this.literals = literals;
    }

    public ArrayList<Literal> getLiterals() {
        return this.literals;
    }

    public boolean isFalse() {
        for(Literal literal : literals) {
            if(literal.getValue() != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(Literal otherLiteral) {
        for(Literal literal : this.literals) {
            if(literal.sameLiteral(otherLiteral)) {
                return true;
            }
        }
        return false;
    }

    public void assign(Literal newLiteral, int value) {
        for(Literal literal : this.literals) {
            if(literal.sameLiteral(newLiteral)) {
                literal.assign(value);
            }
        }
    }

    public Literal findUnassignedLiteral() {
        for(Literal literal : this.literals) {
            if(literal.noAssignment()) {
                return literal;
            }
        }
        return null;
    }

    public Literal findPropagationLiteral() {
        int unassignedNumber = 0;
        for(Literal literal : this.literals) {
            if(literal.noAssignment()) {
                unassignedNumber += 1;
            }
            else if(literal.getValue() == 1) {
                return null;
            }
        }
        if(unassignedNumber == 1) {
            for(Literal literal : this.literals) {
                if(literal.noAssignment()) {
                    // NOTE: return assigned literal
                    if(literal.haveNot()) {
                        literal.assign(0);
                    }
                    else {
                        literal.assign(1);
                    }
                    return literal;
                }
            }
        }
        return null;
    }

    public void clearAssignment() {
        for(Literal literal : this.literals) {
            literal.clearAssignment();
        }
    }

    @Override
    public String toString() {
        String clauseString = "[";
        for(Literal literal : this.literals) {
            clauseString += literal.toString() + ", ";
        }
        clauseString = (clauseString.substring(0, clauseString.length() - 2) + "]");
        return clauseString;
    }
}
