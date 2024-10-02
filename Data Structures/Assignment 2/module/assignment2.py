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
        return returnedNode.element
    
    def peek(self): # returns the latest element from the stack without removing it 
        return self.head.element
    
    def size(self): # returns the size of the stack.
        cur = self.head
        res = 0
        while cur: 
            res += 1
            cur = cur.next
        return res
    
    # Serializes stack to a string, which can be printed. Used for Unit Testing.
    def serialize(self):
        # For testing purposes.
        resStr = ""
        cur = self.head
        while cur: 
            resStr += str(cur.element)
            cur = cur.next
        # So that test case can be validated.
        return resStr
    
    def print(self):
        cur = self.head
        while cur: 
            print(cur.element)
            cur = cur.next

    # Helper function for the arithmetic eval. 
    def reverseStack(self):
        tempStack1 = Stack() 
        while not self.is_empty():
            tempStack1.push(self.pop())
        tempStack2 = Stack()
        while not tempStack1.is_empty():
            tempStack2.push(tempStack1.pop())
        while not tempStack2.is_empty():
            self.push(tempStack2.pop())

    # Evaluates a numeric string input using order of operations
    # Two pointer method to deal with negative numbers
    # Reset function to work off r + 1 when a space is encountered
    def arithmeticEval(self, string):
        i = 0
        num = ''
        while i < len(string):
            char = string[i]
            if char not in "1234567890*/+- ":
                raise ValueError("NaN")
            elif char == " ":
                i += 1
                continue
            # Case will load positive numbers, neg numbers, +/- symbols
            elif char in "1234567890+-":
                l = r = i
                while string[r] != " ":
                    r += 1
                self.push(string[l:r])
                # i is set to the next space after whatever was just inserted
                i = r
            elif char in "*/":
                firstVal = int(self.pop())
                # Finding second value 
                i += 1
                while i < len(string):
                    if string[i] in "1234567890-":
                        while i < len(string) and string[i] != " ":
                            num += string[i]
                            i += 1
                        break
                    else:
                        i += 1
                secondVal = int(num) 
                # Reset the numstring once we've found second value
                num = ''
                # Carry out the */ operations
                if char == '*':
                    self.push(str(firstVal * secondVal))
                if char == "/":
                    self.push(str(firstVal // secondVal))
            else:
                # Found a number
                while i < len(string) and string[i] != " ":
                    num += string[i]
                    i += 1
                self.push(num)
                num = ''

        self.reverseStack()

        while self.size() > 1:
            secondVal = int(self.pop())
            operand = self.pop() # + or -
            firstVal = int(self.pop())
            if operand == "+":
                self.push(str(firstVal + secondVal))
            elif operand == "-":
                self.push(str(secondVal - firstVal))
        return self.pop()
    
class Queue:

    class QueueNode:
        def __init__(self, element, next=None):
            self.element = element
            # Points at another ListNode
            self.next = next
        
    # Init obj of type userLinkedList
    def __init__(self):
        # Will point at ListNode
        self.head = None
        self.tail = None
        self.size = 0

    def is_empty(self):
        return self.head == self.tail == 0
    
    def print(self):
        cur = self.head
        while cur:

            print("\n")
            cur = cur.next

    # Should be O(1) time
    def enqueue(self, node):
        # Check if there are any nodes at all. If no, create the first node.
        if self.is_empty():
            newNode = self.QueueNode(node, None)
            self.head = newNode
            self.tail = newNode
            return

        # Insert node at the front with at least one existing node
        newNode = self.QueueNode(node, self.head.next)
        self.head = newNode

    
    def dequeue(self):
        pass
            




stack = Stack()

print(stack.arithmeticEval("10 + 20 * -2"))
