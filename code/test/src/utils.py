def cnf2string(cnf):
    s = ""
    for i in range(len(cnf)):
        clause = cnf[i]
        s += "("
        for j in range(len(clause)):
            variable = clause[j]
            if variable < 0:
                s += "!"
            s += str(abs(variable))
            if j != len(clause) - 1:
                s += "|"
        s += ")"
        if i != (len(cnf) - 1):
            s += "&"
    return s

