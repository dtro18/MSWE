import sys, string, operator
import numpy as np
import collections
from numpy.lib.stride_tricks import sliding_window_view

# Example input: "Hello  World!" 
characters = np.array([' ']+list(open(sys.argv[1]).read())+[' '])
# Result: array([' ', 'H', 'e', 'l', 'l', 'o', ' ', ' ', 
#           'W', 'o', 'r', 'l', 'd', '!', ' '], dtype='<U1')

# Normalize to uppercase
characters[~np.char.isalpha(characters)] = ' '
characters = np.char.upper(characters)
# Result: array([' ', 'h', 'e', 'l', 'l', 'o', ' ', ' ', 
#           'w', 'o', 'r', 'l', 'd', ' ', ' '], dtype='<U1')

characters = np.char.replace(characters, 'A', '4')
characters = np.char.replace(characters, 'E', '3')
characters = np.char.replace(characters, 'I', '1')
characters = np.char.replace(characters, 'O', '0')


### Split the words by finding the indices of spaces
sp = np.where(characters == ' ')
# Result: (array([ 0, 6, 7, 13, 14], dtype=int64),)
# A little trick: let's double each index, and then take pairs
sp2 = np.repeat(sp, 2)
# Result: array([ 0, 0, 6, 6, 7, 7, 13, 13, 14, 14], dtype=int64)
# Get the pairs as a 2D matrix, skip the first and the last
w_ranges = np.reshape(sp2[1:-1], (-1, 2))
# Remove the indexing to the spaces themselves
w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 3)]
# Result: array([[ 0,  6],
#                [ 7, 13]], dtype=int64)
# Voila! Words are in between spaces, given as pairs of indices
words = list(map(lambda r: characters[r[0]:r[1]], w_ranges))
# Result: [array([' ', 'h', 'e', 'l', 'l', 'o'], dtype='<U1'), 
#          array([' ', 'w', 'o', 'r', 'l', 'd'], dtype='<U1')]
# Let's recode the characters as strings
swords = np.array(list(map(lambda w: ''.join(w).strip(), words)))
# Result: array(['hello', 'world'], dtype='<U5')

# Sliding window function. Grab unique np arrays and their counts.
v = sliding_window_view(swords, 2)
uniqueKeys, counts = np.unique(v, return_counts=True, axis=0)
# Flatten inner np arrays into one string, creating np array of twogram strings.
flattenedUniqueKeys = np.array(list(map(lambda arr: str(arr[0] + " " + arr[1]), uniqueKeys)))

# Create dict by zipping keys and counts
twoGramDict = dict(zip(flattenedUniqueKeys, counts))

# Sort by counts desc
sortedDict = dict(sorted(twoGramDict.items(), key=lambda item: item[1], reverse=True))
topFive = list(sortedDict.items())[:5]

# Iterating through just to print
for listTuple in topFive:
    print(str(listTuple[0]) + " - " + str(listTuple[1]))