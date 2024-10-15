import unittest
from module.assignment4 import User, Node, BST


# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_buildBST(self):
        # Task 1 Build a BST from the input data:
        bst = BST()
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
        # Verify Task worked by checking text files.

    # Tests if a user with no children has been deleted
    def test_deleteNoChild(self): 
        # Creating a smaller tree to demonstrate delete function.
        bst = BST()
        with open('tree-testdelete-input.txt', 'r') as file:
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
        noChildUser = User("8212399", "Schafer", "0251", "EST", "1")
        oneChildUser = User("8400912", "Green", "0045", "RFM", "1")
        twoChildUser = User("8534534", "McKay", "0251", "CT", "1")
        bst.delete(noChildUser)
        bst.printTreeBFS()
        