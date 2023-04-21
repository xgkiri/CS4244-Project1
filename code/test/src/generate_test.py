from random_cnf_generator import Generator
from utils import cnf2string, cnf2dimacs
from pycryptosat import Solver
import os
root = "../data"
str_data_name = "test_data_str.txt"
list_data_name = "test_data_list.txt"
dimacs_data_name = "test_data_dimacs.txt"
prop_num = 10
clause_num = 100
k = 5
sample_num = 200
g = Generator(prop_num, clause_num, k)
CNF_list = []
CNF_str = []
CNF_dimacs = []
for i in range(sample_num):
    cnf = g.get_cnf()
    s = Solver()
    s.add_clauses(cnf)
    sat, _ = s.solve()
    if not sat:
        CNF_list.append("UNSAT " + str(cnf))
    else:
        CNF_list.append("SAT " + str(cnf))
    CNF_str.append(cnf2string(cnf))
    CNF_dimacs.append(cnf2dimacs(cnf, prop_num))
fd_str = open(os.path.join(root, str_data_name), "w")
fd_list = open(os.path.join(root, list_data_name), "w")
fd_dimacs = open(os.path.join(root, dimacs_data_name), "w")
for i in range(len(CNF_dimacs)):
    fd_dimacs.write(CNF_dimacs[i] + "\n\n")
for j in range(len(CNF_str)):
    fd_str.write(CNF_str[j] + "\n")
for k in range(len(CNF_list)):
    fd_list.write(CNF_list[k] + "\n")
fd_str.close()
fd_list.close()
fd_dimacs.close()

