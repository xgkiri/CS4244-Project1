from utils import string2cnf, auto_line_feed
from pycryptosat import Solver

puzzle_src = '../data/puzzle'
file_type = '.txt'
with open(puzzle_src + file_type, 'r') as file:
    str_formula = file.read().rstrip()

cnf, encoding = string2cnf(str_formula)
s = Solver()
s.add_clauses(cnf)
sat, solution = s.solve()
print(sat)
if sat:
    solution = list(solution)
    answer = {}
    for i in range(1, len(solution)):
        key = [k for k, v in encoding.items() if v == i][0]
        answer[key] = solution[i]
    answer = auto_line_feed(str(answer), ',')
    with open(puzzle_src + '_answer' + file_type, 'w') as file:
        file.write(answer)