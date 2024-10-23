import unittest
from module.assignment4 import City, Graph
import os
os.chdir('C:/MSWE Projects/Applied DSA/Assignment 4')

# Testing City class and load function for Tasks 1/2.
class TestTask345(unittest.TestCase):
    def setUp(self):
        self.archipelagoGraph = Graph()
        self.cityToVertex = {}

        # Load cities/pops (vertices)
        with open('city_population.txt', 'r') as file:
            for line in file:
                # Init a user with the values obtained from file
                colonIdx = line.find(":")
                name = line[:colonIdx].strip().lower()
                pop = int(line[colonIdx + 1:].strip())
                city = City(name, pop)
                
                if name not in self.cityToVertex:
                    v = self.archipelagoGraph.insert_vertex(city)
                    self.cityToVertex[name] = v
                # Add it to dict for fast indexing
        with open('road_network.txt', 'r') as file:
            for line in file:
                # Init a user with the values obtained from file
                colonIdx = line.find(":")
                city1 = line[:colonIdx].strip().lower()
                city2 = line[colonIdx + 1:].strip().lower()
                v1 = self.cityToVertex.get(city1)
                v2 = self.cityToVertex.get(city2)
                self.archipelagoGraph.insert_edge(v1, v2)

    # Task 3 - Determine number of islands
    # Will be using Union Find algorithm for this purpose. Also could've used DFS+Set approach.
    def test_task_3(self):
        # Parent tracks an index for every node. Initially every node is its own parent. 
        # Purpose is to connect nodes (forest approach)
        self.assertEqual(self.archipelagoGraph.unionFind(), 1)

    # Task 4 - Determine population of islands
    def test_task_4(self):
        # Have to run union find to geneate the parent list
        self.archipelagoGraph.unionFind()
        # Pop confirmed by looping thru list and totaling all populations.
        self.assertEqual(self.archipelagoGraph.findIslandPop(), [126077108])

    # Task 5: Minimum path length between nodes
    def test_task_5(self):
        self.assertEqual((self.archipelagoGraph.dijkstra(self.cityToVertex["citrus heights"], self.cityToVertex["citrus heights"])), 0)
        self.assertEqual((self.archipelagoGraph.dijkstra(self.cityToVertex["citrus heights"], self.cityToVertex["rialto"])), 1)
        self.assertEqual((self.archipelagoGraph.dijkstra(self.cityToVertex["citrus heights"], self.cityToVertex["burien"])), 2)