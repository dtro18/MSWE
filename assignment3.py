
# Task 1: Implement a Hash Table
# Referenced Goodrich Ch 10.2 Hash Tables for implementation of hash code
def hash_code(s):
    # Shifts # 1 left 2 bits, which is 32 1s. Equivalent to 2^32
    # Will be used to validate that the result is within 32 bits
    mask = (1 << 32) - 1  # Limit to 32 bit integers
    # Holds hash value as it is computed
    h = 0
    for char in s:
        # Moves binary representation of h left 5 and checks it against mask. If it's greater than mask, snips off the leading digits that exceed 32 bits.
        # If the shift left evaluated to 0, then try a shift right of 27. Take whatever of these actually has a value. 
        h = (h << 5 & mask) | (h >> 27)
        # Shifting 5 deterrmined by experimental method to yield least number of collisions. 
        h += ord(char)
    return h
        