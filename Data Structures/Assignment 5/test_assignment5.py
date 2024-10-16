import unittest
from module.assignment5 import Node, minHeap, maxHeap, BST, BSTToheapTransformer

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_minHeap(self):
        heap = minHeap([5, 4, 3, 2, 1])
        self.assertEqual(heap.arr, [1, 2, 3, 5, 4])

        # Checking peek function.
        self.assertEqual(heap.peek(), 1)

        # Popping all values to make sure min value is popped every time.
        res = []
        for i in range(heap.get_size()):
            res.append(heap.heapPop())
        self.assertEqual(res, [1, 2, 3, 4, 5])

        # Popping from an empty heap should result in an error.
        with self.assertRaises(ValueError) as context:
            heap.heapPop()
        self.assertEqual(str(context.exception), "Heap is empty")

    def test_maxHeap(self):
        heap = maxHeap([1, 2, 3, 4, 5])
        self.assertEqual(heap.arr, [5, 4, 3, 1, 2])

        # Checking peek function.
        self.assertEqual(heap.peek(), 5)

        # Popping all values to make sure min value is popped every time.
        res = []
        for i in range(heap.get_size()):
            res.append(heap.heapPop())
        self.assertEqual(res, [5, 4, 3, 2, 1])

        # Popping from an empty heap should result in an error.
        with self.assertRaises(ValueError) as context:
            heap.heapPop()
        self.assertEqual(str(context.exception), "Heap is empty")
class TestTask2(unittest.TestCase):
    def test_BSTToHeap(self):
        bst = BST()
        bst.insert(5)
        bst.insert(6)
        bst.insert(7)
        bst.insert(8)
        bst.insert(1)
        bst.insert(2)
        bst.insert(3)
        bstTransformer = BSTToheapTransformer(bst)
        # Test that the BST has been heapified.
        self.assertEqual(bstTransformer.minHeap.arr, [1, 2, 3, 5, 7, 6, 8])
        self.assertEqual(bstTransformer.maxHeap.arr, [8, 7, 6, 2, 1, 3, 5])
        
