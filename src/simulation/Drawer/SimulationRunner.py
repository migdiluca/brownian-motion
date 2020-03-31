
import time
from Drawer import Drawer
from FilesReader import FilesReader

class SimulationRunner:

    def runSingleSimulation(self, path):
        drawer = Drawer(500)
        reader = FilesReader(path)
        positions = reader.readNextPosition()
        drawer.firstUpdate()
        time.sleep(4)

        while len(positions) > 0:
            drawer.update(positions)
            positions = reader.readNextPosition()
