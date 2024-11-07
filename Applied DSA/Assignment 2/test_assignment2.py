import unittest
from module.assignment2 import binarySearch1d, binarySearch2d

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_binarySearch1d(self):
        # Ensures that the hash function returns diff idx for diff strings
        self.assertEqual(binarySearch1d([4,9,10,13,17,18,19,21], 17), [4, 4])
        self.assertEqual(binarySearch1d([2,4,6,8,10,14,16], 12), [-1, -1])
        self.assertEqual(binarySearch1d([], 0), [-1, -1])

class TestTask2(unittest.TestCase):
    def test_binarySearch2d(self):
        matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]]
        self.assertEqual((binarySearch2d(matrix, 4)), False)
        self.assertEqual((binarySearch2d(matrix, 13)), False)
        # See if can find with duplicate arrays
        matrix = [[1,3,5,7],[11,11,16,20],[23,30,34,60]]
        self.assertEqual((binarySearch2d(matrix, 11)), True)

        # Testing search on empty arrays
        matrix = [[]]
        self.assertEqual((binarySearch2d(matrix, 0)), False)
        matrix = []
        self.assertEqual((binarySearch2d(matrix, 0)), False)
    
