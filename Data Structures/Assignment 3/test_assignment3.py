import unittest
from module.assignment3 import Hash

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_hash(self):
        # Tests if the stack has been correctly implmented.
        hashMap = Hash()
        self.assertEqual(hashMap.get_size(), 0)
        hashMap.insert("Dylan")
        hashMap.insert("Tran")
        hashMap.insert("Is")
        hashMap.insert("A")
        hashMap.insert("Good")
        hashMap.insert("Programmer")
        self.assertEqual(hashMap.get_size(), 6)
        
        # Tries to input an empty string
        hashMap.insert("")
        self.assertEqual(hashMap.get_size(), 6)

        # Creates a really large input (64 bits)
        largeInput = ''.join("A" for _ in range(64)) 
        hashMap.insert(largeInput)
        self.assertEqual(hashMap.get_size(), 7)
    def test_collision(self):
        hashMap = Hash()
        # Tests that two strings that hash to the same value
        # Are both inputted (thru collision resolution)
        hashMap.insert("Collide")
        hashMap.insert("Collide")
        self.assertEqual(hashMap.get_size(), 2)