import unittest
from module.assignment4 import City, Archipelago

# Testing City class and load function for Tasks 1/2.
class TestTask1(unittest.TestCase):
    def test_loadCities(self):

        archi = Archipelago()
        archi.loadCities()
        self.assertEqual(archi.cities[0].name, "New York")
        self.assertEqual(archi.cities[0].population, 8405837)