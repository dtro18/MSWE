import sympy
# Used this library to generate prime numbers for the hash function. 
import random
# Also used this to generate random numbers for hashing.

# Task 1: Implement a Hash Table
# Referenced Goodrich Ch 10.2 Hash Tables for implementation of hash code and MAD compression function
# Also referenced this library: https://www.geeksforgeeks.org/prime-functions-python-sympy/ for generating primes


# TODO Implement compression function
NUM_BUCKETS = 1000
class Hash:
    def __init__(self):
        # Generates fixed size hashTable storing list of lists
        self.hashTable = [[] for i in range(NUM_BUCKETS)]
        # How many keys have been entered
        self.size = 0

        # A hash function that converts a string x to an integer, i.e., index in the hashtable.
    def hash(self, x):
        
        # Hash Code Function
        # Shifts # 1 left 2 bits, which is 32 1s. Equivalent to 2^32
        # Will be used to validate that the result is within 32 bits
        mask = (1 << 32) - 1  # Limit to 32 bit integers
        # Holds hash value as it is computed
        h = 0
        for char in x:
            # Moves binary representation of h left 5 and checks it against mask. If it's greater than mask, snips off the leading digits that exceed 32 bits.
            # If the shift left evaluated to 0, then try a shift right of 27. Take whatever of these actually has a value. 
            h = (h << 5 & mask) | (h >> 27)
            # Shifting 5 deterrmined by experimental method to yield least number of collisions. 
            h += ord(char)
        
        # Compression function
        # p is a prime number larger than NUM_BUCKETS
        p = sympy.nextprime(NUM_BUCKETS)

        # a, b are random integers ranging from 0 to p - 1, and a must be > 0
        a = random.randint(1, p -1)
        b = random.randint(0, p - 1)
        idx = (((a * h) + b) % p) % NUM_BUCKETS
        return idx


    # Insert string x to the HashTable in the index returned by hash(x).
    def insert(self, x):
        index = self.hash(x)
        if len(self.hashTable[index]) == 0:
            self.size += 1
        self.hashTable[index].append(x)

    def get_size(self):
        return self.size
    