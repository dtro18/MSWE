class User:

    def __init__(self, name='', address='', ssn='', initDeposit=0):
        self.name = name
        self.address = address
        self.ssn = ssn
        self.balance = initDeposit



class userLinkedList:
    # Nested node class
    class ListNode:
        def __init__(self, element, id=1, next=None):
            self.element = element
            self.uniqueId = id
            # Points at another ListNode
            self.next = next
        
    # Init obj of type userLinkedList
    def __init__(self):
        # Will point at ListNode
        self.head = None

    def is_empty(self):
        return self.head == None
    
    def print(self):
        cur = self.head
        while cur:
            # Only print if the node is occupied
            print("ID: " + str(cur.uniqueId))
            print("Name: " + cur.element.name)
            print("Address: " + cur.element.address)
            print("SSN: " + cur.element.ssn) 
            print("Balance: " + str(cur.element.balance))
            print("\n")
            cur = cur.next
    
    def addUser(self, user: User, startingId):
        # Check if there are any nodes at all. If no, create the first node.
        if self.is_empty():
            newNode = self.ListNode(user, 1, None)
            self.head = newNode
            self.tail = newNode
            return
        # Checking another edge case where we previously deleted node with ID: 1, so we have to specially handle adding it back
        if self.head.uniqueId > startingId:
            newNode = self.ListNode(user, startingId, self.head)
            self.head = newNode
            return
        
        # If nodes exist, create a cursor to iterate and insert at first avail
        cur = self.head
        prev = None

        # Check first for empty IDs
        while cur:
            # Found insertion point
            if cur and prev and cur.uniqueId > (prev.uniqueId + 1):
                newNode = self.ListNode(user, prev.uniqueId + 1, cur)
                prev.next = newNode
                return
            else:
                prev = cur
                cur = cur.next

        # If reached end of list
        newNode = self.ListNode(user, prev.uniqueId + 1, None)
        prev.next = newNode

    def deleteUser(self, id):
       
        prev = None
        cur = self.head
        while cur:
            if cur.uniqueId == id:
                # Check if we're trying to delete the first node. If we are, handle it.
                if cur == self.head:
                    self.head = cur.next
                    return
                prev.next = cur.next
                return
            prev = cur
            cur = cur.next

    def payUserToUser(self, payerId, payeeId, amount):
        if self.is_empty():
            raise IndexError("Linked List is empty.")
        cur = self.head
        payerNode = None
        payeeNode = None

        # Loop while both nodes have not been located
        while not payerNode or not payeeNode:
            if cur == None:
                raise IndexError("ID not found.")
            if cur.uniqueId == payerId:
                payerNode = cur
            if cur.uniqueId == payeeId:
                payeeNode = cur
            cur = cur.next
        if ((payerNode.element.balance - amount) < 0):
            print("Warning: Negative balance. Transaction cancelled")
            return
        else:
            payerNode.element.balance -= amount
            payeeNode.element.balance += amount


    def getMedianID(self):

        # Fast and slow pointer technique, with the addition of dummy node.
        # Dummy node handles even cases.

        dummy = self.ListNode(0)
        dummy.next = self.head
        slow = fast = dummy

        while fast and fast.next:
            fast = fast.next.next
            slow = slow.next
        return slow.uniqueId
    
    def mergeAccounts(self, ID1, ID2):
        if self.is_empty():
            raise ValueError("Accounts not found")
        cur = self.head

        # Keeping this one
        lowerNode = None
        # Deleting this one
        higherNode = None

        # Loop while both nodes have not been located
        while not lowerNode or not higherNode:
            if cur == None:
                raise IndexError("ID not found.")
            if cur.uniqueId == ID1:
                lowerNode = cur
            if cur.uniqueId == ID2:
                higherNode = cur
            cur = cur.next
        # Lowernode should be the one that's kept
        if lowerNode.uniqueId > higherNode.uniqueId:
            lowerNode, higherNode = higherNode, lowerNode

        # Check if they're actually the same person
        if (lowerNode.element.name != higherNode.element.name or
            lowerNode.element.address != higherNode.element.address or
            lowerNode.element.ssn != higherNode.element.ssn):
            raise IndexError("IDs do not belong to the same user.")

        lowerNode.element.balance += higherNode.element.balance
        self.deleteUser(higherNode.uniqueId)
        
        # Returns the merged banks under bank1
    def mergeBanks(self, bank1, bank2):
        dummy = self.ListNode(0)
        tail = dummy
        p1, p2 = bank1.head, bank2.head
        while p1 and p2:
            if p1.uniqueId == p2.uniqueId:
                # Want to send p2's element to the next available id that is greater than its current id.
                bank2.addUser(p2.element, p2.uniqueId)
                p2 = p2.next
                tail.next = p1
                p1 = p1.next
            elif p1.uniqueId < p2.uniqueId:
                tail.next = p1
                p1 = p1.next
            else:
                tail.next = p2
                p2 = p2.next
            tail = tail.next
        if p1:
            tail.next = p1
        elif p2:
            tail.next = p2

        bank1.head = dummy.next

        

        

# Task 1: Model list of users in a Linked List sorted by ID.
# Task 2: Utilize addUser function to add a new user. Because no ids have been freed (yet),
#         we just add these to the end of the LL.

# Creating different users.
user1 = User("Dylan1 OC", "57 Raven Ln", '111-111-1111', 100)
user2 = User("Dylan2 OC", "57 Raven Ln", '111-111-1111', 200)
user3 = User("Dylan3 OC", "57 Raven Ln", '111-111-1111', 300)
user4 = User("Dylan Duplicate OC", "57 Raven Ln", '111-111-1111', 400)
user5 = User("Dylan Duplicate OC", "57 Raven Ln", '111-111-1111', 500)

bankOC = userLinkedList()
bankOC.addUser(user1, 1)
bankOC.addUser(user2, 1)
bankOC.addUser(user3, 1)
bankOC.addUser(user4, 1)
bankOC.addUser(user5, 1)

print("Task 1/2 \n")
bankOC.print()

# Task 3: Implement the deleteUser function. Free up the unique id, which can later be reassigned.

# Deleting user with id 4.
bankOC.deleteUser(3)
print("Removed user 3. Printing list again...")
bankOC.print()

print("Adding new user, Dylan6. Should be inserted into the LL, taking up the freed id 4.")
user6 = User("Dylan6 OC", "57 Raven Ln", '111-111-1111', 0)
bankOC.addUser(user6, 1)
bankOC.print()

# Task 4: User with ID 1 pays user with ID2 by some amount

print("User 1's balance before payment: " + str(user1.balance))
print("User 2's balance before payment: " + str(user2.balance))
bankOC.payUserToUser(1, 2, 100)
print("User 1's balance after payment: " + str(user1.balance))
print("User 2's balance after payment: " + str(user2.balance))

# Edge case: What if the payment exceeds the available funds?

print("User 1's balance before payment: " + str(user1.balance))
print("User 3's balance before payment: " + str(user3.balance))
bankOC.payUserToUser(1, 3, 200)

# Task 5: Write a method that gets the median id of the list.
print("Odd number, median id is: ")
# Odd case: When there's an odd amount of elements, returns median as expected.
print(bankOC.getMedianID())
# Even case: When there's an even amount of elements, returns the id of the first median element.
bankOC.deleteUser(1)
print("Even number, median id is: ")
print(bankOC.getMedianID())

# Task 6: Write a method that merges two accounts into one.
# Merges the "Dylan duplicate" accounts, which have balances of 400 and 500, for a total of 900.

bankOC.mergeAccounts(4, 5)
bankOC.print()

# Task 7: Merge two linked lists. If there's a contested ID, create a new ID for one and add it to the new list.

bankOC1 = userLinkedList()
bankLA1 = userLinkedList()

# Adding users to bankOC1
user1 = User("Dylan1 OC", "57 Raven Ln", '111-111-1111', 100)
user2 = User("Dylan2 OC", "57 Raven Ln", '111-111-1111', 200)
user3 = User("Dylan3 OC", "57 Raven Ln", '111-111-1111', 300)

bankOC1.addUser(user1, 1)
bankOC1.addUser(user2, 1)
bankOC1.addUser(user3, 1)

print("List 1 before merging: ")
bankOC1.print()

# Adding users to bankOC2 with some overlapping IDs
user4 = User("Dylan4 LA", "57 Raven Ln", '222-222-2222', 400)
user5 = User("Dylan5 LA", "57 Raven Ln", '333-333-3333', 500)
user6 = User("Dylan6 LA", "57 Raven Ln", '444-444-4444', 600)

bankLA1.addUser(user4, 1)
bankLA1.addUser(user5, 1)
bankLA1.addUser(user6, 1)

print("List 2 before merging: ")
bankLA1.print()
bankOC1.mergeBanks(bankOC1, bankLA1)

bankOC1.print()