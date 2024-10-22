import unittest
from module.assignment5 import containsPermutation

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_contains_permutation(self):
        # Normal case, should pass
        s1 = "ab"
        s2 = "eidbaooo"
        self.assertEqual(containsPermutation(s1, s2), True)
        # Negative case, should fail
        s2 = "eidboaoo"
        self.assertEqual(containsPermutation(s1, s2), False)

        # len s1 > s2. Should fail
        s2 = "eidbaooo"
        s1, s2 = s2, s1
        self.assertEqual(containsPermutation(s1, s2), False)
