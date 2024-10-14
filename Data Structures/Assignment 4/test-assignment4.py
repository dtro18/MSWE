import unittest
from module.assignment4 import User, Node, BST


# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_insert(self):
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
        self.assertEqual(bst.head)