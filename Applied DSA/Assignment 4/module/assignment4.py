import collections
import heapq
import os
os.chdir('C:/MSWE Projects/Applied DSA/Assignment 4')

# References - Leetcode Count Connected Components in an Undirected Graph
# Assumptions - Cities that occur more than once in the data are treated as one city, taking the first population to be the "true" population for the city.
class City:
    
    def __init__(self, name, pop): # Where connected is a list of all the cities this city can connect to
        self.name = name
        self.population = pop
        self.connected = []
class Graph:

    # Nested Vertex Class
    class Vertex:
        # Defines only one allowed attribute for a vertex object 
        __slots__ = '_element'

        # Constructor to be called from graph's insert_vertex
        def __init__(self, x):
            self._element = x

        # Returns element associated with vertex
        def element(self):
            return self._element

        # Allows this object to be used as a key when indexing into a map. Uses its own memory address.
        # The same value in different vertices will produce different hashes.
        def __hash__(self):
            return hash(id(self))
        
        def __lt__(self, other):
            return self
    # Nested Edge Class
    class Edge:
        __slots__ = '_origin', '_destination', '_element'

        def __init__(self, u, v, x=1):
            self._origin = u
            self._destination = v
            self._element = x
        
        # Returns the endpoints of an edge.
        def endpoints(self):
            return (self._origin, self._destination)

        # Returns the other vertex given one
        def opposite(self, x):
            if x == self._origin:
                return self._destination
            else:
                return self._origin

        # Returns element associated with edge (i.e. weights, distances, etc)
        def element(self):
            return self._element
        
        # Hashes an edge for use in a dictionary or set.
        # Edges are equivalent if they connect the same origin and destination, so they should hash the same.
        def __hash__(self):
            return hash((self._origin, self._destination))
    
    # Constructor for the graph class
    def __init__(self, directed=False):
        # Dict of dicts.
        # Maps a vertex (key) to a dict (value) which consists of (vertex: edge) key/value pairs.
        self._outgoing = {}
        # Won't be used unless the graph is directed.
        self._incoming = {} if directed else self._outgoing
        self.parent = []

    def is_directed(self):
        # Returns false if self._incoming exists.
        return self._incoming is not self._outgoing
    
    def vertex_count(self):
        # Counts vertices
        return len(self._outgoing)
    
    def vertices(self):
        # Returns all the vertices
        return self._outgoing.keys()
    
    def edge_count(self):   
            # Accesses a vertex in the outgoing array, and accesses that vertex's dictionary to find how many vertices
            # It connects to (same as edges)
        total = sum(len(self._outgoing[vertex]) for vertex in self._outgoing)
        return total if self.is_directed() else total // 2

    def edges(self):
        # Grabs all the edges in a graph and returns as a set. 
        result = set()
        for neiToEdge in self._outgoing.values():
            result.update(neiToEdge.values())
        return result
    
    def get_edge(self, u, v):
        return self._outgoing[u].get(v)
    
    def degree(self, v):
        # Returns number of edges attached to a vertex v
        adj = len(self._outgoing[v])
        return len(adj[v])

    def incident_edges(self, v):
        # Returns list of all the edges incident to a vertex v
        edges = []
        for edge in self._outgoing[v].values():
            edges.append(edge)
        return edges

    def insert_vertex(self, x=None):
        # x is the new vertex's element
        v = self.Vertex(x)
        self._outgoing[v] = {}
        if self.is_directed():
            self._incoming[v] = {}
        return v
    
    def insert_edge(self, u, v, x=1):
        # Inserts edge between u and v. 
        e = self.Edge(u, v, x)
        self._outgoing[u][v] = e
        self._incoming[v][u] = e

    def unionFind(self):
        self.parent = [i for i in range(self.vertex_count())]
        numNodes = self.vertex_count()
        # Maintains size of each connected component, which is inisially all 1 for all of the nodes.
        # Once a representative is established for each connected component, we'll update that parent's rank
        rank = [1] * numNodes
        def find(n1):
            res = n1
            # Index is the node, and value is the parent. If a node has a parent that isn't itself,
            # then value will be the index of the parent.
            while res != self.parent[res]:
                # Set the child to the grandparent. Indexing child should now point not to the parent,
                # but the grandpareant.
                self.parent[res] = self.parent[self.parent[res]]
                # Update res.
                res = self.parent[res]
            return res
        # Given two nodes, connects them if they weren't already connected.
        def union(n1, n2):
            p1 = find(n1)
            p2 = find(n2)

            if p1 == p2:
                # Same parent, just return. Connected components shouldn't be decremented
                return 0
            if rank[p2] > rank[p1]:
                self.parent[p1] = p2
                rank[p2] += rank[p1]
            else:
                self.parent[p2] = p1
                rank[p1] += rank[p2]
            return 1

        connectedComponents = numNodes
        # Assigns a node to a numerical value.
        vertexToInt = {v: idx for idx, v in enumerate(self.vertices())}
        edges = self.edges()
        for edge in edges:
            v1, v2 = edge.endpoints()
            n1 = vertexToInt[v1] 
            n2 = vertexToInt[v2]
            connectedComponents -= union(n1, n2)
        return connectedComponents
    
    def findIslandPop(self):
        vertices = self.vertices()
        # Flatten the parents set to find potential parents/representatives of each island.
        potentialParents = set(self.parent)
        # Go through and search the potential parents to see if any are linked.
        actualParents = set()
        for idx in potentialParents:
            cur = idx
            while cur != self.parent[idx]:
                cur = self.parent[cur]
            actualParents.add(cur)

        # DFS to add all the populations
        visited = set()
        def dfsAddPops(v):
            # Skip visited cities
            if v in visited:
                return 0
            visited.add(v)
            # Grab the vertex's population and store it
            pop = v.element().population
            # Loop thru that vertex's neighbors
            for adjVert in self._outgoing[v].keys():
                # Recursive call
                pop += dfsAddPops(adjVert)
            return pop

        # Define a list to hold populations of multiple islands, if there are multiple islands
        islandPops = []
        for idx in actualParents:
            # Convert index to vertex obj
            vertex = list(self.vertices())[idx]
            islandPops.append(dfsAddPops(vertex))
        return islandPops.copy()
    def dijkstra(self, src, dest):

        d = {} # what we return????
        cloud = {} # maps a reachable v to its distance from source. Vertices in the cloud are "finalized" and distance cannot be shortened. 
        pq = []
        heapq.heapify(pq)
        
        # for each vertex, add entry to q, with source having dist 0
        for v in self.vertices():
            if v is src:
                d[v] = 0
            else:
                d[v] = float("inf")

            heapq.heappush(pq, (d[v], v))

        while pq:
            key, u = heapq.heappop(pq) # Pop the smallest distance (i.e. explore the closest node from where we are)
            if u in cloud:
                continue
            # Finalize u's position. There is no shorter way to get to u.
            cloud[u] = key # Map vertex u to its final distance from source

            # Add neighboring vertices into the heap.
            for e in self.incident_edges(u):
                v = e.opposite(u)
                # Relaxation step
                if v not in cloud:
                    wgt = e.element()
                    if d[u] + wgt < d[v]:
                        d[v] = d[u] + wgt
                        heapq.heappush(pq, (d[v], v))
        return cloud[dest]
    

def dijkstra(g, src, dest):

    d = {} # what we return????
    cloud = {} # maps a reachable v to its distance from source. Vertices in the cloud are "finalized" and distance cannot be shortened. 
    pq = []
    heapq.heapify(pq)
    
    # for each vertex, add entry to q, with source having dist 0
    for v in g.vertices():
        if v is src:
            d[v] = 0
        else:
            d[v] = float("inf")

        heapq.heappush(pq, (d[v], v))

    while pq:
        key, u = heapq.heappop(pq) # Pop the smallest distance (i.e. explore the closest node from where we are)
        if u in cloud:
            continue
        # Finalize u's position. There is no shorter way to get to u.
        cloud[u] = key # Map vertex u to its final distance from source

        # Add neighboring vertices into the heap.
        for e in g.incident_edges(u):
            v = e.opposite(u)
            # Relaxation step
            if v not in cloud:
                wgt = e.element()
                if d[u] + wgt < d[v]:
                    d[v] = d[u] + wgt
                    heapq.heappush(pq, (d[v], v))
    return cloud[dest]
