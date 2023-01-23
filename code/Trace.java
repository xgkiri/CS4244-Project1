class TraceUnit {
    private final Literal literal;
    private final Clause clause;

    TraceUnit(Literal literal, Clause clause) {
        this.literal = literal;
        this.clause = clause;
    }

    public String toString() {
        return this.literal.toString() + this.clause.toString();
    }
}

class Trace {
    private TraceUnit[] traceUnits;

    Trace(TraceUnit[] traceUnits) {
        this.traceUnits = traceUnits;
    }

    public String toString() {
        String traceString = "{";
        for(int i = 0; i < this.traceUnits.length; i++) {
            traceString += this.traceUnits[i].toString();
            if(i != this.traceUnits.length - 1) {
                traceString += ", ";
            }
        }
        traceString += "}";
        return traceString;
    }
}