import collections

class Node:
    def __init__(self, val, parent):
        self.right = None
        self.left  = None
        self.parent = parent
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
        lastParent = self.parent(len(self.arr) - 1)
        for i in range(lastParent, -1, -1):
            self.downheap(i)
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
        if self.get_size() == 0:
            raise ValueError("Heap is empty")
        return self.arr[0]
        
    def heapPop(self):
        if self.get_size() == 0:
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
        lastParent = self.parent(len(self.arr) - 1)
        for i in range(lastParent, -1, -1):
            self.downheap(i)
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
        if self.get_size() == 0:
            raise ValueError("Heap is empty")
        return self.arr[0]
        
    def heapPop(self):
        if self.get_size() == 0:
            raise ValueError("Heap is empty")
        self.swap(0, len(self.arr) - 1)
        poppedElem = self.arr.pop()
        self.downheap(0)
        return poppedElem
    
# Implemented modified BST class from Assignment 4.
class BST:
    def __init__(self):
        self.head = None
    
    # Inserts a passed User object, creates node, and inserts it
    def insert(self, val):
        cur = self.head
        # Base case (handed an empty tree)
        if not cur:
            self.head = Node(val, None)

        # Continue until you find a node that has no children
        while cur:
            # Move left
            if val < cur.val:
                if not cur.left:
                    cur.left = Node(val, cur)
                    return
                cur = cur.left
            # Move right bc will never have an == case
            else:
                if not cur.right:
                    cur.right = Node(val, cur)
                    return
                cur = cur.right


    # Deletes a user, passing in a user id.
    def delete(self, val):
        if not self.head:
            raise ValueError("No nodes present")
        # Finds the minimum node in a subtree and returns it
        def findMin(treeHead):
            cur = treeHead
            while cur.left:
                cur = cur.left
            return cur

        cur = self.head
        # Continue until you find a node that has no children
        while cur:
            
            # Base case: We found the node
            if cur.val == val:
                # Node has 2 children
                if cur.left and cur.right:
                    minNode = findMin(cur.right)
                    cur.val = minNode.val
                    del minNode
                    return
                # Node has 1 or 0 children
                # Determine which side of the grandparent to insert the grandchildren in
                elif cur.parent and cur.val < cur.parent.val:
                    # Left side
                    # Elem being deleted has no children.
                    if not cur.left and not cur.right:
                        cur.parent.left = None
                    # Cur's left side exists, should be hooked up to grandparent
                    elif cur.left:
                        cur.parent.left = cur.left
                        cur.left.parent = cur.parent
                    else:
                        cur.parent.left = cur.right
                        cur.right.parent = cur.parent
                    return
                elif cur.val > cur.parent.val:
                    # Right Side
                    # Elem being deleted has no children.
                    if not cur.left and not cur.right:
                        cur.parent.right = None
                    elif cur.left:
                        cur.parent.right = cur.left
                        cur.left.parent = cur.parent
                    else:
                        cur.parent.right = cur.right
                        cur.right.parent = cur.parent
                    return
            if cur.val > val:
                cur = cur.left
            # Move right bc will never have an == case
            else:
                cur = cur.right

        raise ValueError("Node not found")
    # Not called
    def printTreeDFS(self):
        file = open("tree-output-dfs.txt", "w")
        def dfs(node):
            if not node:
                return
            dfs(node.left)
            node.printNodeToText(file)
            dfs(node.right)

        dfs(self.head)
        file.close()
    # Not called
    def printTreeBFS(self):
        # Init q with root.
        file = open("tree-output-bfs.txt", "w")
        q = collections.deque()
        q.append(self.head)
        while q:
            node = q.popleft()
            if node:
                node.printNodeToText(file)
                q.append(node.left)
                q.append(node.right)
        file.close()

# Takes a bst and makes an accessible minheap/m axheap
class BSTToheapTransformer():
    def __init__(self, bst):
        self.bst = bst
        if self.bst.head:
            self.heapArr = []
            self.minHeap = None
            self.maxHeap = None
            self.load()
            self.populateMaxHeap()
            self.populateMinHeap()

    def load(self):
        q = collections.deque()
        q.append(self.bst.head)
        while q:
            node = q.popleft()
            if node.left:
                q.append(node.left)
            if node.right:
                q.append(node.right)
            self.heapArr.append(node.val)
    
    def populateMinHeap(self):
        copy = self.heapArr[:]
        self.minHeap = minHeap(copy)

    def populateMaxHeap(self):
        copy = self.heapArr[:]
        self.maxHeap = maxHeap(copy)
