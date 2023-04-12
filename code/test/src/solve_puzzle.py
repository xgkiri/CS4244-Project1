from utils import string2cnf
from pycryptosat import Solver

puzzle_src = '../data/puzzle.txt'
with open(puzzle_src, 'r') as file:
    str_formula = file.read().rstrip()

cnf = string2cnf(str_formula)
s = Solver()
s.add_clauses(cnf)
sat, solution = s.solve()
print(sat)
print(solution)