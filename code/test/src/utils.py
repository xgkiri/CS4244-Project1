import numpy as np

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
    return cnf, prop


def cnf2dimacs(cnf, prop_num):
    s = "p cnf " + str(prop_num) + " " + str(len(cnf)) + "\n"
    for i in range(len(cnf)):
        clause = cnf[i]
        for j in range(len(clause)):
            variable = clause[j]
            s += str(variable)
            if j != len(clause) - 1:
                s += " "
            else:
                s += " 0"
        if i != len(cnf) - 1:
            s += "\n"
    return s



def auto_line_feed(string, token):
    idx = np.array([i for i in range(len(string)) if string[i] == token])
    for i in range(len(idx)):
        if i != 0 and i % 4 == 0:
            string = string[:idx[i] + 1] + '\n' + string[idx[i] + 1:]
            idx += 1
    return string

