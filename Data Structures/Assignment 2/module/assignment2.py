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
    
    # Serializes stack to a list, which can be printed. Used for Unit Testing.
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

    # Input: str = "10 + 20 * 2"
    # Output: 50
    # Input: str = “foo / 30 + 7”
    # Output: NaN
    def arithmeticEval(self, string):
        # Preprocessing
        
        i = 0
        num = ''
        while i < len(string):
            char = string[i]
            if char == " ":
                i += 1
                continue
            elif char not in "1234567890*/+- ":
                raise ValueError("NaN")
            elif (char in "+-"):
                self.push(char)
                i += 1
                continue
            elif (char in "*/"):
                firstVal = int(self.pop())
                # Finding second value 
                i += 1
                while i < len(string):
                    if string[i] in "1234567890":
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
        self.print()

        while self.size() > 1:
            secondVal = int(self.pop())
            operand = self.pop() # + or -
            firstVal = int(self.pop())
            if operand == "+":
                self.push(str(firstVal + secondVal))
            elif operand == "-":
                self.push(str(secondVal - firstVal))
        # return self.peek()

stack = Stack()
stack.arithmeticEval("10 * 10 * 10 + 150 / 2")
# 10 + 40 + 10 - 2 - 14
stack.print()