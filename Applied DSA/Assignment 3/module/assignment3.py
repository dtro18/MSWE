from collections import defaultdict
# O(nlogn)
def quickSort(arr):
    if len(arr) < 2:
        return arr
    # Picks rightmost elem to be the pivot
    pivot = arr[-1] 
    # Inits three groups that elems can be sorted to
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
    # Recursive call on the less and greater groups, stitch them back together.
    return quickSort(less) + equal + quickSort(greater)

# Helper function for mergesort
def merge(s1, s2):
    i = j = 0
    mergedString = []
    while i < len(s1) and j < len(s2):
        if s1[i] < s2[j]:
            mergedString.append(s1[i])
            i += 1
        else:
            mergedString.append(s2[j])
            j += 1
    # If there's elems remaining, add them. Only one of these will execute.
    mergedString.extend(s1[i:])
    mergedString.extend(s2[j:])
    return mergedString
# O(nlogn)
def mergeSort(s):
    if len(s) < 2:
        return s
    
    mid = len(s) // 2
    # Recursive call to sort left and right halves.
    s1 = mergeSort(s[:mid])
    s2 = mergeSort(s[mid:])

    return merge(s1, s2)
    
def groupAnagrams(strings):
    # Should return list of lists (of anagrams)
    if len(strings) == 0:
        return [[]]
    dict = defaultdict(list)
    for string in strings:
        anagram = "".join(quickSort(string))
        dict[anagram].append(string)
    return list(dict.values())