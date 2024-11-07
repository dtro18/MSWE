# Implementing binary search
# Referenced leetcode question binary search on a 2d array.

def binarySearch1d(arr, val):
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

# matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
def binarySearch2d(arr, val):
    if len(arr) == 0 or len(arr[0]) == 0:
        return False
    ROWS = len(arr)
    COLS = len(arr[0])

    # Identify row that it could be in
    top, bottom = 0, ROWS - 1
    while top <= bottom:
        # Row is the row we're currently looking at
        row = (top + bottom) // 2

        if (val >= arr[row][0]) and (val <= arr[row][-1]):
            # Move on
            break
        elif val < arr[row][0]:
            bottom = row - 1
        elif val > arr[row][-1]:
            top = row + 1
    
    # Perform 1d binary search
    l, r = 0, COLS - 1
    
    while l <= r:
        col = (l + r) // 2
        if arr[row][col] == val:
            return True
        elif val < arr[row][col]:
            r = col - 1
        elif val > arr[row][col]:
            l = col + 1
    return False

