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
