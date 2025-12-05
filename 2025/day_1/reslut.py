# https://adventofcode.com/2025/day/1

import io 

def read_file(file_path):
    with io.open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    return content
  
def find_password():
    file_path = '2025/day_1/input/input.txt'
    content = read_file(file_path)
    lines = content.splitlines()
    finalPass = 0
    start = 50
    for line in lines:
      left = line[0] == 'L'
      movecount = int(line.replace('L', '').strip()) if left else int(line.replace('R', '').strip())
      
      while movecount > 0:
          start +=  -1 if left else 1
          if start < 0: start = 99
          if start > 99: start = 0
          movecount -= 1
      if start == 0:
          finalPass += 1
              
    return finalPass
  

def find_password_part_two():
    file_path = '2025/day_1/input/input.txt'
    content = read_file(file_path)
    lines = content.splitlines()
    zero_counter = 0
    curr_pos = 50
    dial_nums = 100
    
    for instruction in lines:
        direction = instruction[0]
        clicks = int(instruction[1:])
        
        for _ in range(clicks):  # simulate EVERY click
            if direction == "R":
                curr_pos = (curr_pos + 1) % dial_nums
            else:  # direction == "L"
                curr_pos = (curr_pos - 1) % dial_nums
            
            if curr_pos == 0:
                zero_counter += 1
                
    return zero_counter
  
print(find_password())
print(find_password_part_two())