import unittest
from module.assignment5 import Node, minHeap, maxHeap, HeapBuilder, BST, BSTToheapTransformer

# Testing stack functions for Task 1.
class TestTask1(unittest.TestCase):
    def test_minHeap(self):
        # Task 1 Build a BST from the input data:
        # bst = BST()
        # with open('tree-input.txt', 'r') as file:
        #     for line in file:
        #         # Init a user with the values obtained from file
        #         operation = line[0]
        #         id = line[1:8]
        #         lname = line[8:33].rstrip()
        #         dept = line[33:37]
        #         prog = line[37:41].rstrip()
        #         year = line[41]
        #         user = User(id, lname, dept, prog, year)
        #         if operation == "I":
        #             bst.insert(user)
        # self.assertEqual(bst.head)
        pass
    def test_maxHeap(self):
        heap = maxHeap([1, 2, 3, 4, 5])
        print(heap.arr)
        self.assertEqual(heap.arr, [5, 4, 3, 1, 2])

    def test_task2(self):
        bst = BST()
        bst.insert(5)
        bst.insert(6)
        bst.insert(7)
        bst.insert(8)
        bst.insert(1)
        bst.insert(2)
        bst.insert(3)

        bst.printTreeBFS()
        bstTransformer = BSTToheapTransformer(bst)
        self.assertEqual(bstTransformer.minHeap.arr, [1, 2, 3, 5, 7, 6, 8])
        self.assertEqual(bstTransformer.maxHeap.arr, [8, 7, 6, 2, 1, 3, 5])

# heap = Heap()
# heap.insert(3)
# heap.insert(2)
# heap.insert(4)
# heap.insert(5)
# print(heap.arr)
# heap.insert(1)
# print(heap.arr)
# print(heap.peek())
# heap.heapPop()
# print(heap.arr)
# heap.heapPop()
# print(heap.arr)
# heap.heapPop()
# print(heap.arr)
# heap.heapPop()
# print(heap.arr)
# heap.heapPop()
# print(heap.arr)


# heap = minHeap([5, 4, 3, 2, 1])
# heap = minHeap([6, 8, 10, 11, 2])
# heap.heapPop()
# heap.heapPop()
# print(heap.arr)




# heapBuild = HeapBuilder([1, 2, 3, 4, 5, 6, 7], "MIN")
# heapBuild.printTree()
# heapBuild = HeapBuilder([1, 2, 3, 4, 5, 6, 7], "MAX")
# heapBuild.printTree()     

