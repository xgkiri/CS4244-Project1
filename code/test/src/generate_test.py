from random_cnf_generator import Generator
from utils import cnf2string
from pycryptosat import Solver
import os
root = "../data"
str_data_name = "test_data_str.txt"
list_data_name = "test_data_list.txt"
g = Generator(20, 50, 5)
sample_num = 200
CNF_list = []
CNF_str = []
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
fd_str = open(os.path.join(root, str_data_name), "w")
fd_list = open(os.path.join(root, list_data_name), "w")
for j in range(len(CNF_str)):
    fd_str.write(CNF_str[j] + "\n")
for k in range(len(CNF_list)):
    fd_list.write(CNF_list[k] + "\n")
fd_str.close()
fd_list.close()

