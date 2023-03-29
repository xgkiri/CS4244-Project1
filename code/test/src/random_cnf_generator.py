import random

class Generator:
    def __init__(self, num_prop, num_clause, clause_len):
        self.num_prop = num_prop
        self.num_clause = num_clause
        self.clause_len = clause_len

    def get_one_clause(self):
        clause = []
        for i in range(self.clause_len):
            new_variable = random.randrange(1, self.num_prop + 1)
            negetion = random.random() >= 0.5
            if negetion:
                clause.append(new_variable * -1)
            else:
                clause.append(new_variable)
        return clause

    def get_cnf(self):
        cnf = []
        for i in range(self.num_clause):
            cnf.append(self.get_one_clause())
        return cnf


