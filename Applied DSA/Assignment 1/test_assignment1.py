import unittest
from module.assignment1 import binarySearch

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_hashFn(self):
        # Ensures that the hash function returns diff idx for diff strings
        self.assertEqual(binarySearch([4,9,10,13,17,17,19,21], 17), [4, 5])
        self.assertEqual(binarySearch([2,4,6,8,10,14,16], 12), [-1, -1])
        self.assertEqual(binarySearch([], 0), [-1, -1])

