import sympy
# Used this library to generate prime numbers for the hash function. 
import random
# Also used this to generate random numbers for hashing.
import os
os.chdir('C:/MSWE Projects/Data Structures/Assignment 3')
# Helps to locate the text file to be read
# Task 1: Implement a Hash Table
# Referenced Goodrich Ch 10.2 Hash Tables for implementation of hash code and MAD compression function
# Also referenced this library: https://www.geeksforgeeks.org/prime-functions-python-sympy/ for generating primes

# Referenced this to read a a large text file line-by-line
# https://stackoverflow.com/questions/3277503/how-to-read-a-file-line-by-line-into-a-list


NUM_BUCKETS = 230000
class Hash:
    def __init__(self):
        # Generates fixed size hashTable storing list of lists
        self.hashTable = [[] for i in range(NUM_BUCKETS)]
        # How many keys have been entered
        self.size = 0
        self.p = sympy.nextprime(NUM_BUCKETS)
        # a, b are random integers ranging from 0 to p - 1, and a must be > 0
        self.a = random.randint(1, self.p - 1)
        self.b = random.randint(0, self.p - 1)

        # A hash function that converts a string x to an integer, i.e., index in the hashtable.
    def hash(self, x):
        
        # Cyclic Shift Hash Code Function
        # Shifts # 1 left 2 bits, which is 32 1s. Equivalent to 2^32
        # Will be used to validate that the result is within 32 bits
        mask = (1 << 32) - 1  # Limit to 32 bit integers
        # Holds hash value as it is computed
        h = 0
        for char in x:
            # Grabs the left 5 digits and sticks them on to the right end. 
            # Experimentally determined. 
            h = (h << 5 & mask) | (h >> 27)
            # Shifting 5 deterrmined by experimental method to yield least number of collisions. 
            h += ord(char)
        
        # Compression function (MAD method)
        # p is prime > n
        # a, b are primes < p - 1
        # Purpose is to fit the hash value to the appropriate bucket size
        idx = (((self.a * h) + self.b) % self.p) % NUM_BUCKETS
        return idx


    # Insert string x to the HashTable in the index returned by hash(x).
    def insert(self, x):
        if x == '':
            return 
        index = self.hash(x)
        if len(self.hashTable[index]) == 0:
            self.hashTable[index].append(x)
        else:
            # Collision resolution mechanism - linear probing
            while len(self.hashTable[index]) != 0:
                index = (index + 1) % NUM_BUCKETS
            self.hashTable[index].append(x)
        self.size += 1
        

    def get_size(self):
        return self.size



# Task 2:
# Open file and count number of distinct anagrams.
# Alphanum should be considered; anything else is a delimiter
# Sort word and add to dict
# Ex line: The Project Gutenberg EBook of Pride and Prejudice, by Jane Austen
hashMap = Hash()

with open('pride-and-prejudice.txt', 'r') as file:
    for line in file:
        curStr = ''
        i = 0
        while i < len(line):
            if not line[i].isalnum():
                # Delimiter reached, check if the curStr is alr in the dict and if not add it
                sortedString = ''.join(sorted(curStr)).lower()
                hashIdx = hashMap.hash(sortedString)
                if len(hashMap.hashTable[hashIdx]) == 0:
                    print(sortedString)
                    hashMap.insert(sortedString)
                # Reset the string and keep building
                curStr = ''
                while i < len(line) and not line[i].isalnum():
                    i += 1
            # Add something to the string being built if its alphanumeric
            else:
                curStr += line[i]
                i += 1
        # If the line ends on an actual character, need to snapshot whatever's in curStr again.
        if len(curStr) > 0:
            sortedString = ''.join(sorted(curStr)).lower()
            idx = hashMap.hash(sortedString)
            if len(hashMap.hashTable[idx]) == 0:
                hashMap.insert(sortedString)
                print(sortedString)

# print(hashMap.get_size())