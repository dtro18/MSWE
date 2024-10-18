from collections import defaultdict
def quickSort(arr):
    if len(arr) < 2:
        return arr
    
    pivot = arr[-1]

    equal = []
    less = []
    greater = []

    for n in arr:
        if n < pivot:
            less.append(n)
        elif n > pivot:
            greater.append(n)
        else:
            equal.append(n)
    
    return quickSort(less) + equal + quickSort(greater)

a = "paolo"
b = "oloap"
print("".join(quickSort(a)))
print("".join(quickSort(b)))

def groupAnagrams(strings):
    # Should return list of lists (of anagrams)
    dict = defaultdict(list)
    for string in strings:
        anagram = "".join(quickSort(string))
        dict[anagram].append(string)
    return list(dict.values())

print(groupAnagrams(["bucket","rat","mango","tango","ogtan","tar"]))