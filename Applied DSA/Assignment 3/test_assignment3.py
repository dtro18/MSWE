import unittest
import time
from module.assignment3 import quickSort, merge, mergeSort, groupAnagrams

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):

    def test_sortingAlgos(self):
        startQuickSort = time.time()
        # Creating a crazy long string so that we can actually judge the time
        testStr = "algorithmsorttimeteststring" * 10000
        "".join(quickSort(testStr))
        endQuickSort = time.time()
        
        startMergeSort = time.time()
        "".join(mergeSort(testStr))
        endMergeSort = time.time()
        print("Quicksort: " + str(endQuickSort - startQuickSort))
        print("Mergesort: " + str(endMergeSort - startMergeSort))
    
    def test_groupAnagrams(self):
        # Group anagrams uses quicksort since it's the faster algo
        # Testing standard case
        self.assertEqual(groupAnagrams(["bucket","rat","mango","tango","ogtan","tar"]), [["bucket"],["rat","tar"],["mango"],["tango","ogtan"]])
        # Given empty list (should return list of lists)
        self.assertEqual(groupAnagrams([]), [[]])
        # Given list containing just one value
        self.assertEqual(groupAnagrams(["bucket"]), [["bucket"]])