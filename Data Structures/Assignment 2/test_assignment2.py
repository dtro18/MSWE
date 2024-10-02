import unittest
from module.assignment2 import Stack
from module.assignment2 import Queue
from module.assignment2 import StackWithTwoQs

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_push(self):
        # Tests if the stack has been correctly implmented.
        stack = Stack()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        self.assertEqual(stack.serialize(), "321")

    def test_pop(self):
        stack = Stack()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        stack.pop()
        self.assertEqual(stack.serialize(), "21")
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
        
class TestTask2(unittest.TestCase):
    def test_arithmetic_eval(self):
        stack = Stack()
        
        with self.assertRaises(ValueError) as context:
            stack.arithmeticEval("foo / 30 + 7")
        # Verify that the exception's message is "NaN"
        self.assertEqual(str(context.exception), "NaN")

        self.assertEqual(stack.arithmeticEval("10 + 20 * 2"), '50')
        self.assertEqual(stack.arithmeticEval("10 + 20 * -2"), '-30')
        self.assertEqual(stack.arithmeticEval("10 * 10 * 10 * 10"), "10000")

class TestTask3(unittest.TestCase):
    def test_implement_queue(self):
        q = Queue()
        q.enqueue(1)
        q.enqueue(2)
        q.enqueue(3)
        self.assertEqual(q.poll(), 1)
        self.assertEqual(q.get_size(), 3)
        self.assertEqual(q.dequeue(), 1)
        self.assertEqual(q.dequeue(), 2)
        self.assertEqual(q.dequeue(), 3)
        self.assertEqual(q.get_size(), 0)

        with self.assertRaises(ValueError) as context:
            q.dequeue()
        # Verify that dequeueing from an empty queue raises an error
        self.assertEqual(str(context.exception), "Queue is empty.")

class TestTask4(unittest.TestCase):
    def test_implement_stack_twoqs(self):
        stack = StackWithTwoQs()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        self.assertEqual(stack.peek(), 3)
        self.assertEqual(stack.get_size(), 3)
        self.assertEqual(stack.pop(), 3)
        self.assertEqual(stack.pop(), 2)
        self.assertEqual(stack.pop(), 1)
        self.assertEqual(stack.get_size(), 0)
        with self.assertRaises(ValueError) as context:
            stack.pop()
        # Verify that dequeueing from an empty queue raises an error
        self.assertEqual(str(context.exception), "Queue is empty.")
