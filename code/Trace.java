import java.util.ArrayList;

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
    // TODO: use ArrayList instead
    private ArrayList<TraceUnit> traceUnits;

    Trace(ArrayList<TraceUnit> traceUnits) {
        this.traceUnits = traceUnits;
    }

    void addTraceUnit(TraceUnit traceUnit) {
        this.traceUnits.add(traceUnit);
    }

    public String toString() {
        String traceString = "{";
        for(int i = 0; i < this.traceUnits.size(); i++) {
            traceString += this.traceUnits.get(i).toString();
            if(i != this.traceUnits.size() - 1) {
                traceString += ", ";
            }
        }
        traceString += "}";
        return traceString;
    }
}