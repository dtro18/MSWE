import unittest
from module.assignment2 import Stack

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_push(self):
        # Tests if the stack has been correctly implmented.
        stack = Stack()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        self.assertEqual(stack.print(), [3, 2, 1])

    def test_pop(self):
        stack = Stack()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        stack.pop()
        self.assertEqual(stack.print(), [2, 1])
    def test_peek(self):
        stack = Stack()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        self.assertEqual(stack.peek(), 3)
    def test_size(self):
        stack = Stack()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        self.assertEqual(stack.size(), 3)
        
