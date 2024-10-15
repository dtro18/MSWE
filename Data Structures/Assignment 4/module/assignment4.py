import os
os.chdir('C:/MSWE Projects/Data Structures/Assignment 4')
import collections
class User:
    def __init__(self, id, lastName, dept, program, year):
        self.id = id
        self.lastName = lastName
        self.dept = dept
        self.program = program
        self.year = year

    def __eq__(self, other):
        return self.id == other.id

class Node:
    def __init__(self, element, parent):
        self.right = None
        self.left  = None
        self.element = element
        self.parent = None

    def printNodeToText(self, file):
        elemInfo = []
        for value in vars(self.element).values():
            elemInfo.append(str(value) + "\n")
        elemInfo.append("\n")
        file.writelines(elemInfo)
class BST:
    def __init__(self):
        self.head = None
    
    # Inserts a passed User object, creates node, and inserts it
    def insert(self, user):
        cur = self.head
        # Base case (handed an empty tree)
        if not cur:
            self.head = Node(user, None)

        # Continue until you find a node that has no children
        while cur:
            # Move left
            if user.lastName.lower() < cur.element.lastName.lower():
                if not cur.left:
                    cur.left = Node(user, cur)
                    return
                cur = cur.left
            # Move right bc will never have an == case
            else:
                if not cur.right:
                    cur.right = Node(user, cur)
                    return
                cur = cur.right


    # Deletes a user, passing in a user id.
    def delete(self, name):

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
            if cur.element.lastName.lower() == name.lower():
                print("Executing")
                if cur.left and cur.right:
                    minNode = findMin(cur.right)
                    cur.element = minNode.element
                    del minNode
                    return
                # Determine which side of the grandparent to insert the grandchildren in
                elif cur.element.lastName.lower() < cur.parent.element.lastName.lower():
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
                elif cur.element.lastName.lower() > cur.parent.element.lastName.lower():
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
            if cur.element.lastName.lower() > name.lower():
                cur = cur.left
            # Move right bc will never have an == case
            else:
                cur = cur.right

        raise ValueError("Node not found")

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



            
bst = BST()
# Task 1: Build a BST from the input data
with open('tree-input.txt', 'r') as file:
    for line in file:
        # Init a user with the values obtained from file
        operation = line[0]
        id = line[1:8]
        lname = line[8:33].rstrip()
        dept = line[33:37]
        prog = line[37:41].rstrip()
        year = line[41]
        user = User(id, lname, dept, prog, year)
        if operation == "I":
            bst.insert(user)

# Task 2: Recursive DFS print of the tree.
bst.printTreeDFS()
bst.printTreeBFS()

bst.delete("Schafer")
bst.printTreeBFS()
# Todo: check delete implementation