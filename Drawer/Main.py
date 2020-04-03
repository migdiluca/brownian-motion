import pygame
from pygame.locals import *
import random
import Drawer
import time
import FilesReader
from SimulationRunner import SimulationRunner

paths = ["../../current", "../../particles2000", "../../particles3000", "../../particles5000"]

simulationRunner = SimulationRunner()

simulationRunner.runSingleSimulation("../")
