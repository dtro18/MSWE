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

# Implemented BST class from Assignment 4.
class BST:
    def __init__(self):
        self.head = None
    
    # Inserts a passed User object, creates node, and inserts it
    def insert(self, val):
        cur = self.head
        # Base case (handed an empty tree)
        if not cur:
            self.head = Node(val)

        # Continue until you find a node that has no children
        while cur:
            # Move left
            if val < cur.val:
                if not cur.left:
                    cur.left = Node(val)
                    return
                cur = cur.left
            # Move right bc will never have an == case
            else:
                if not cur.right:
                    cur.right = Node(val)
                    return
                cur = cur.right

    # Deletes a node with < 2 children, passing in a given user object.
    def delete(self, user):
        cur = self.head
        # Continue until you find a node that has no children
        while cur.left != None and cur.right != None:
            # Base case: We found the node
            if cur.element == user:
                # Elem to be deleted has two children -- cannot delete
                if cur.left and cur.right:
                    raise ValueError("Cannot delete this node.")
                
                # Determine which side of the grandparent to insert the grandchildren in
                if cur.element.lastName.lower() < cur.parent.element.lastName.lower():
                    # Left side
                    # Elem being deleted has no children.
                    if not cur.left and not cur.right:
                        cur.parent.left = None
                    # Cur's left side exists, should be hooked up to grandparent
                    elif cur.left:
                        cur.parent.left = cur.left
                    else:
                        cur.parent.left = cur.right
                elif cur.element.lastName.lower() > cur.parent.element.lastName.lower():
                    # Right Side
                    # Elem being deleted has no children.
                    if not cur.left and not cur.right:
                        cur.parent.right = None
                    elif cur.left:
                        cur.parent.right = cur.left
                    else:
                        cur.parent.right = cur.right
                return
            if cur.element.lastName.lower() < user.lastName.lower():
                cur = cur.left
            # Move right bc will never have an == case
            else:
                cur = cur.right
        raise ValueError("Node not found")

    # Modified from A4 to just print to console (rather than txt file)
    def printTreeDFS(self):
        def dfs(node):
            if not node:
                return
            dfs(node.left)
            print(node.val)
            dfs(node.right)

        dfs(self.head)

    # Modified from A4 to just print to console (rather than txt file)
    def printTreeBFS(self):
        # Init q with root.
        q = collections.deque()
        q.append(self.head)
        while q:
            node = q.popleft()
            if node:
                print(node.val)
                q.append(node.left)
                q.append(node.right)

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


bst = BST()
bst.insert(5)
bst.insert(6)
bst.insert(7)
bst.insert(8)
bst.insert(1)
bst.insert(2)
bst.insert(3)

bst.printTreeBFS()
bstTransformer = BSTToheapTransformer(bst)
print(bstTransformer.minHeap.arr)
print(bstTransformer.maxHeap.arr)