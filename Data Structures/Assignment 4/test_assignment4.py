import unittest
from module.assignment4 import User, Node, BST


# Testing BFS functions for Task 1.
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
    def test_delete(self): 
        # Creating a smaller tree to demonstrate delete function.
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
        noChildUser = "Schafer"
        oneChildUser = "Green"
        twoChildUser = "McKay"

        # Testing normal deletion function. Should remove Schafer, Green, and McKay from output.
        bst.delete(noChildUser)
        bst.delete(oneChildUser)
        bst.delete(twoChildUser)

        # Testing deletion of a node that's not present.
        with self.assertRaises(ValueError) as context:
            bst.delete("InvalidName")
        self.assertEqual(str(context.exception), "Node not found")
        
        # Updates the tree-output-bfs file to reflect changes.
        bst.printTreeBFS()
        
    def test_deleteEmpty(self):
        # Testing edge case where cannot delete any nodes.
        bst = BST()
        with self.assertRaises(ValueError) as context:
            bst.delete("InvalidName")
        self.assertEqual(str(context.exception), "No nodes present")

# For task 2, easier to just run the print functions from the actual program.