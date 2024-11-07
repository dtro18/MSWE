import unittest
import module.assignment5
# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_contains_permutation(self):
        # Normal case, should pass
        s1 = "ab"
        s2 = "eidbaooo"
        self.assertEqual(module.assignment5.recursiveCheck(s1, s2, 0, len(s1)), True)
        # Negative case, should fail
        s2 = "eidboaoo"
        self.assertEqual(module.assignment5.recursiveCheck(s1, s2, 0, len(s1)), False)

        # len s1 > s2. Should fail
        s2 = "eidbaooo"
        s1, s2 = s2, s1
        self.assertEqual(module.assignment5.recursiveCheck(s1, s2, 0, len(s1)), False)
    def test_queens(self):
        # Normal case
        input = [1, 2, 3, 4, 5, 6, 7, 8]
        self.assertEqual(module.assignment5.minQueenMoves(input), 7)
        # Normal case
        input = [1, 1, 1, 1, 1, 1, 1, 1]
        self.assertEqual(module.assignment5.minQueenMoves(input), 7)
        #One-off case
        input = [1, 5, 8, 6, 3, 7, 2, 5]
        self.assertEqual(module.assignment5.minQueenMoves(input), 1)