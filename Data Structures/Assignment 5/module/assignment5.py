import collections
class Node:
    def __init__(self, val):
        self.right = None
        self.left  = None
        self.val = val

class minHeap:
    def __init__(self):
        self.arr = []
    def __init__(self, arr):
        self.arr = arr
        if len(self.arr) > 0:
            self.heapify()

    def heapify(self):
        # Call downheap right to left, starting with the last parent
        # (Calling downheap from a leaf does nothing)
        print(self.arr)
        lastParent = self.parent(len(self.arr) - 1)
        for i in range(lastParent, -1, -1):
            self.downheap(i)
        print(self.arr)
    # Returns parent index of a given node
    def parent(self, idx):
        return (idx - 1) // 2
    
    # Returns left child of a given node
    def left(self, idx):
        return (2 * idx) + 1

    # Returns right child of a given node
    def right(self, idx):
        return (2 * idx) + 2

    def hasLeft(self, idx):
        return self.left(idx) < len(self.arr)

    def hasRight(self, idx):
        return self.right(idx) < len(self.arr)
    
    # Swaps two idxs
    def swap(self, i, j):
        self.arr[i], self.arr[j] = self.arr[j], self.arr[i]

    # Upheap fn
    def upheap(self, idx):
        parent = self.parent(idx)
        if idx > 0 and self.arr[idx] < self.arr[parent]:
            self.swap(idx, parent)
            self.upheap(parent)
        # Edge case: minimal element bubbles to the top, where it will stop swapping bc par Idx and idx will be same

    # Downheap fn
    def downheap(self, idx):
        if self.hasLeft(idx):
            left = self.left(idx)
            smallChild = left
            if self.hasRight(idx):
                right = self.right(idx)
                if self.arr[right] < self.arr[left]:
                    smallChild = right
            if self.arr[smallChild] < self.arr[idx]:
                self.swap(smallChild, idx)
                self.downheap(smallChild)
        
    def get_size(self):
        return len(self.arr)
    
    def insert(self, elem):
        self.arr.append(elem)
        self.upheap(len(self.arr) - 1)

    def peek(self):
        if self.get_size == 0:
            raise ValueError("Heap is empty")
        return self.arr[0]
        
    def heapPop(self):
        if self.get_size == 0:
            raise ValueError("Heap is empty")
        self.swap(0, len(self.arr) - 1)
        poppedElem = self.arr.pop()
        self.downheap(0)
        return poppedElem
    

class maxHeap:
    def __init__(self):
        self.arr = []
    def __init__(self, arr):
        self.arr = arr
        if len(self.arr) > 0:
            self.heapify()

    def heapify(self):
        # Call downheap right to left, starting with the last parent
        # (Calling downheap from a leaf does nothing)
        print(self.arr)
        lastParent = self.parent(len(self.arr) - 1)
        for i in range(lastParent, -1, -1):
            self.downheap(i)
        print(self.arr)
    # Returns parent index of a given node
    def parent(self, idx):
        return (idx - 1) // 2
    
    # Returns left child of a given node
    def left(self, idx):
        return (2 * idx) + 1

    # Returns right child of a given node
    def right(self, idx):
        return (2 * idx) + 2

    def hasLeft(self, idx):
        return self.left(idx) < len(self.arr)

    def hasRight(self, idx):
        return self.right(idx) < len(self.arr)
    
    # Swaps two idxs
    def swap(self, i, j):
        self.arr[i], self.arr[j] = self.arr[j], self.arr[i]

    # Upheap fn
    def upheap(self, idx):
        parent = self.parent(idx)
        if idx > 0 and self.arr[idx] > self.arr[parent]:
            self.swap(idx, parent)
            self.upheap(parent)
        # Edge case: minimal element bubbles to the top, where it will stop swapping bc par Idx and idx will be same

    # Downheap fn
    def downheap(self, idx):
        if self.hasLeft(idx):
            left = self.left(idx)
            smallChild = left
            if self.hasRight(idx):
                right = self.right(idx)
                if self.arr[right] > self.arr[left]:
                    smallChild = right
            if self.arr[smallChild] > self.arr[idx]:
                self.swap(smallChild, idx)
                self.downheap(smallChild)
        
    def get_size(self):
        return len(self.arr)
    
    def insert(self, elem):
        self.arr.append(elem)
        self.upheap(len(self.arr) - 1)

    def peek(self):
        if self.get_size == 0:
            raise ValueError("Heap is empty")
        return self.arr[0]
        
    def heapPop(self):
        if self.get_size == 0:
            raise ValueError("Heap is empty")
        self.swap(0, len(self.arr) - 1)
        poppedElem = self.arr.pop()
        self.downheap(0)
        return poppedElem
    
# Builds a tree-based heap given an array-based heap
class HeapBuilder():
    # [1, 2, 3, 4, 5, 6, 7]
    def __init__(self, inputArr, mode):
        self.head = None
        if mode == "MIN":
            self.heapArr = minHeap(inputArr)
        elif mode == "MAX":
            self.heapArr = maxHeap(inputArr)
        self.createTree()

    def createTree(self):
        # BFS approach

        nodeQ = collections.deque()
        valQ = collections.deque(self.heapArr.arr) # Load the values into a queue

        if self.heapArr.get_size() >= 1:
            self.head = Node(valQ.popleft())
            nodeQ.append(self.head)

        while valQ and nodeQ:
            node = nodeQ.popleft()
            if valQ:
                leftVal = valQ.popleft()
                if leftVal is not None:
                    node.left = Node(leftVal)
                    nodeQ.append(node.left)
            if valQ:
                rightVal = valQ.popleft()
                if rightVal is not None:
                    node.right = Node(rightVal)
                    nodeQ.append(node.right)

    def printTree(self):
        q = collections.deque([self.head])

        while q:
            node = q.popleft()
            if node:
                print(node.val)
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)
        # Do we need to delete nodes without values?


heapBuild = HeapBuilder([1, 2, 3, 4, 5, 6, 7], "MIN")
heapBuild.printTree()
            