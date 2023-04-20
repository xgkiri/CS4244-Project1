from utils import auto_line_feed

puzzle_src = '../data/puzzle'
file_type = '.txt'
with open(puzzle_src + file_type, 'r') as file:
    str_formula = file.read().rstrip()
    with open(puzzle_src + '_new' + file_type, 'w') as new_file:
        new_file.write(auto_line_feed(str_formula, '&'))