import os
os.chdir('C:/MSWE Projects/Data Structures/Assignment 4')

class User:
    def __init__(self, id, lastName, dept, program, year):
        self.id = id
        self.lastName = lastName
        self.dept = dept
        self.program = program
        self.year = year

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
            if cur.element.lastName.lower() < user.lastName.lower():
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

    def printTree(self):
        file = open("tree-output-txt", "w")
        def dfs(node):
            if not node:
                return
            dfs(node.left)
            node.printNodeToText(file)
            dfs(node.right)

        dfs(self.head)
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
            
bst.printTree()
