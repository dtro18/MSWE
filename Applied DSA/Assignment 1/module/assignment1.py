# Implementing binary search


def binarySearch(arr, val):
    l, r = 0, len(arr) - 1

    res = [-1, -1]
    # Find first
    while l <= r:
        m = (l + r) // 2
        if arr[m] == val:
            res[0] = m
            r = m - 1
        elif val < arr[m]:
            r = m - 1
        elif val > arr[m]:
            l = m + 1
            
    # Find second
    l, r = 0, len(arr) - 1
    while l <= r:
        m = (l + r) // 2
        if arr[m] == val:
            res[1] = m
            l = m + 1
        elif val < arr[m]:
            r = m - 1
        elif val > arr[m]:
            l = m + 1
    return res
