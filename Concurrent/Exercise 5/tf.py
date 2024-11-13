import re, sys, collections
import concurrent.futures
import os

# assign directory
directory = 'C:\MSWE Projects\Concurrent\Exercise 5'
 
# iterate over files in
# that directory

def main():
    files = []
    masterDict = collections.Counter()
    for filename in os.listdir(directory):
        f = os.path.join(directory, filename)
        # checking if it is a file
        if os.path.isfile(f) and filename.endswith(".txt"):
            print(f)
            files.append(f)
    num_processors= os.cpu_count()
    # Creates an executor object to submit tasks to
    with concurrent.futures.ThreadPoolExecutor(max_workers=num_processors) as executor:
        # Submit takes a function and its parameters
        futures = [executor.submit(readTextFile, file) for file in files]
        # As completed will begin yielding futures as they complete, allowing processing of futures as they come.
        for future in concurrent.futures.as_completed(futures):
            masterDict.update(future.result())
    for (w, c) in masterDict.most_common(40):
        print(w, '-', c)


def readTextFile(filepath):
    stopwordsDir = os.path.join(directory, 'stop_words')
    stopwords = set(open(stopwordsDir).read().split(','))
    with open(filepath.lower(), 'r') as file:
        text = file.read().lower()
    words = re.findall('\w{3,}', text)
    counts = collections.Counter(w for w in words if w not in stopwords)
    return counts

if __name__ == "__main__":
    main()
