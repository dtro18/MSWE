import sys, string, collections, heapq

RECURSION_LIMIT = 500000
sys.setrecursionlimit(RECURSION_LIMIT)
# iterate through the file one line at a time 

def parse(reader, words, stopWords):
    nextChar = reader.read(1)
    if nextChar == '':
        return

    if nextChar.isalnum():
        parse_word(nextChar, reader, words, stopWords)

    return parse(reader, words, stopWords)
    
# Returns a word, or an empty string if the word is invalid.
def parse_word(prevChar, reader, words, stopWords):
    curWord = prevChar

    while True:
        
        c = reader.read(1)
        if c == '' or not c.isalnum():
            break
        curWord += c

    if len(curWord) >= 2 and curWord.lower() not in stopWords:
        words.append(curWord.lower())

with open('stop_words.txt') as f:
    stopWords = f.read().split(',')
    stopWords = set(stopWords)

words = []

with open('pride-and-prejudice.txt') as f:
    parse(f, words, stopWords)

freqDict = collections.Counter(words)
maxHeap = []
heapq.heapify(maxHeap)
for key, value in freqDict.items():
    heapq.heappush(maxHeap, (-value, key))
for _ in range(25):
    val, key = heapq.heappop(maxHeap)
    print(key + " - " + str(-val))
