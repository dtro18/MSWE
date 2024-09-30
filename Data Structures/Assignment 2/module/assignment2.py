# Referenced Ch 7 of Goodrich for help with implementation

class Stack:
    # Nested node class
    class _ListNode:
        def __init__(self, element, next=None):
            self.element = element
            # Points at another ListNode
            self.next = next
        
    # Init obj of type userLinkedList
    def __init__(self, element=None):
        # Will point at ListNode
        self.head = None
        self.element = element

    def is_empty(self):
        return self.head == None
    
    def push(self, element): # pushes element in the stack. 
        # Always adds to front
        newNode = self._ListNode(element, self.head)
        self.head = newNode

    def pop(self): # Removes the latest element from the stack and returns it. 
        returnedNode = self.head
        self.head = self.head.next
        return returnedNode
    
    def peek(self): # returns the latest element from the stack without removing it 
        return self.head.element
    
    def size(self): # returns the size of the stack.
        cur = self.head
        res = 0
        while cur: 
            res += 1
            cur = cur.next
        return res
    
    # Serializes stack to a list, which can be printed. Used for Unit Testing.
    def print(self):
        res = []
        cur = self.head
        while cur: 
            res.append(cur.element)
            cur = cur.next
        # print(res) 
        return res
    
    # Input: str = "10 + 20 * 2"
    # Output: 50
    # Input: str = “foo / 30 + 7”
    # Output: NaN
    def arithmeticEval(self, string):
        # Preprocessing
        arr = []
        num = ''
        for c in string:
            if c == " ":
                continue
            elif (c in "+-*/"):
                # Reached punctuation, snapshot whatever's in num
                arr.append(num)
                arr.append(c)
                num = ''
            else:
                num += c
        arr.append(num)
        print(arr)
stack = Stack()
stack.arithmeticEval("10 + 20 * 2")