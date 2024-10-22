# Referenced Neetcode Sliding Window video for this question.
# Task 1

# Checks if s2 contains a permutation of s1.
s1 = "ab"
s2 = "eidboooo"

# Should check if two strings of equal length are permutations of each other
def isPermutation(s1, s2):
    s1Dict = {}
    s2Dict = {}
    
    for char in s1:
        s1Dict[char] = 1 + s1Dict.get(char, 0)
    for char in s2:
        s2Dict[char] = 1 + s2Dict.get(char, 0)
    return s1Dict == s2Dict


def recursiveCheck(s1, s2, i, j):
    print(s1)
    print(s2[i:j])
    if isPermutation(s1, s2[i:j]):
        return True
    elif j >= len(s2):
        return False
    
    return recursiveCheck(s1, s2, i + 1, j + 1) # Where i, j are indices of s2

# print(recursiveCheck(s1, s2, 0, len(s1)))
    
# Can only move queens vertically
# queenPosition = [1 2 3 4 5 6 7 8] gives row values (1-indexed)
# at queenPosition[col] = row
# Can only move queens vertically

def minQueenMoves (queenPositions):

    # Change queenPositions to be 0-indexed for easier math
    queenPositions = [num - 1 for num in queenPositions]
    
    # Generate all solutions to n queens
    occupiedRows = set() 
    occupiedPosDiag = set() # calculated by c + r
    occupiedNegDiag = set() # calculated by c - r
    validArr = [] # Holds valid solutions to n queens
    n = len(queenPositions)

    # Attempts to place queens in valid positions.
    def backtrack(col, builtArr):
        # Return if we reach the last column with no hiccups
        if col == n:
            validArr.append(builtArr.copy())
            return
        # Test position in each row of a given colummn
        for row in range(n):
            if (row in occupiedRows or
                (col - row) in occupiedNegDiag or
                (col + row) in occupiedPosDiag):
                continue

            # Add the row of interest into the relevant arrays/sets
            builtArr.append(row)
            occupiedRows.add(row)
            occupiedPosDiag.add(col + row)
            occupiedNegDiag.add(col - row)
            # Recursive call incrementing cols once to move on
            backtrack(col + 1, builtArr)
            # Undo everything and try the next combination
            occupiedRows.remove(row)
            occupiedPosDiag.remove(col + row)
            occupiedNegDiag.remove(col - row)
            builtArr.pop()

    # How many vertical moves does input differ from the most optimal moves?
    def findMinMoves():
        minMoves = float("inf")
        for solution in validArr:
            count = 0
            for i in range(len(solution)):
                if solution[i] != queenPositions[i]:
                    count += 1  
            minMoves = min(minMoves, count)
        return minMoves


    backtrack(0, [])
    return findMinMoves()

    

# Normal case
input = [1, 2, 3, 4, 5, 6, 7, 8]
print(minQueenMoves(input))
# Normal case
input = [1, 1, 1, 1, 1, 1, 1, 1]
print(minQueenMoves(input))
# One-off case
input = [1, 5, 8, 6, 3, 7, 2, 5]
print(minQueenMoves(input))