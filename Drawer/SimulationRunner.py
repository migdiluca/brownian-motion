
import time
from Drawer import Drawer
from FilesReader import FilesReader

class SimulationRunner:

    def runSingleSimulation(self, path):
        reader = FilesReader(path + 'animation_dynamic')
        mapSize, maxSpeed = reader.readStatic(path + 'animation_static')
        drawer = Drawer(int(mapSize * 1000), maxSpeed)
        positions = reader.readNextPosition()
        drawer.firstUpdate()
        time.sleep(4)

        while len(positions) > 0:
            drawer.update(positions)
            positions = reader.readNextPosition()
            skipPositions = 150
            while skipPositions > 0:
                reader.readNextPosition()
                skipPositions -= 1

        time.sleep(1000)