import csv
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

def string2cnf(str):
    count = 1
    prop = {}
    cnf = []
    clauses = str.split('&')
    for clause in clauses:
        clause = clause[1 : -1]
        new_clause = []
        literals = clause.split('|')
        for literal in literals:
            if literal[0] == '!':
                pos = literal[1:]
                if pos not in prop:
                    prop[pos] = count
                    new_clause.append(-1 * count)
                    count += 1
                else:
                    new_clause.append(-1 * prop[pos])
            else:
                if literal not in prop:
                    prop[literal] = count
                    new_clause.append(count)
                    count += 1
                else:
                    new_clause.append(prop[literal])
        cnf.append(new_clause)
    return cnf

