from __future__ import division
import pygame
from pygame.locals import *
import numpy as np
import random
import math
import time
import pygame.gfxdraw
from matplotlib import pyplot as plt
from matplotlib import colors
from matplotlib import cm as cmx


class Drawer:
    DISPLAY = None
    MAIN_SURFACE = None
    SECONDARY_SURFACE = None
    xResolution = 1920
    yResolution = 1080
    WHITE = (255, 255, 255)
    RED = (255, 40, 40)
    BLUE = (35, 55, 184)
    YELLOW = (252, 211, 3)
    BLACK = (25, 25, 25)
    BACKGROUND = BLACK
    MAPBORDER = WHITE
    BIGCOLOR = YELLOW
    SMALLCOLOR = BLUE
    boxSize = 0
    mapSize = 0
    X_OFFSET = 0
    Y_OFFSET = 0
    bigParticlePositions = []

    def __init__(self, mapSize):
        pygame.init()
        self.mapSize = mapSize
        self.calculateSizes()

    def calculateSizes(self):
        width = self.xResolution
        height = self.yResolution
        self.boxSize = int(min(width / self.mapSize, height / self.mapSize))
        self.X_OFFSET = (width - (self.mapSize * self.boxSize)) / 2
        self.Y_OFFSET = (height - (self.mapSize * self.boxSize)) / 2
        self.DISPLAY = pygame.display.set_mode((self.xResolution, self.yResolution), pygame.FULLSCREEN, 32)
        self.DISPLAY.fill(self.BACKGROUND)

    def writeText(self, x, y, text, size, color):
        font = pygame.font.SysFont("Arial", size)
        label = font.render(text, 1, color)
        self.DISPLAY.blit(label, (x * self.boxSize, y * self.boxSize))

    def drawWalls(self):
        self.DISPLAY.fill(self.BACKGROUND)
        w, h = pygame.display.get_surface().get_size()
        for x in range(0, self.mapSize):
            self.drawSquare(self.X_OFFSET + (x * self.boxSize), self.Y_OFFSET, self.MAPBORDER)
            self.drawSquare(self.X_OFFSET + (x * self.boxSize), self.Y_OFFSET + self.boxSize * (self.mapSize - 1), self.MAPBORDER)

        for y in range(0, self.mapSize):
            self.drawSquare(self.X_OFFSET, self.Y_OFFSET + (self.boxSize * y), self.MAPBORDER)
            self.drawSquare(self.X_OFFSET + self.boxSize * (self.mapSize - 1), self.Y_OFFSET + (self.boxSize * y), self.MAPBORDER)

    def drawSquare(self, x, y, color, boxAmount=1):
        pygame.draw.rect(self.DISPLAY, color,
                         (x, y, self.boxSize * boxAmount, self.boxSize * boxAmount))

    def cleanEmptySquares(self):
        for y in range(len(self.map)):
            for x in range(len(self.map[y])):
                if self.map[y][x] == -1:
                    self.drawSquare(x, y, self.BLUE)

    def drawCircles(self, positions):
        i = 0
        for position in positions:
            if i == 0:
                self.bigParticlePositions.append(position)
                pygame.draw.circle(self.DISPLAY, self.BIGCOLOR, (int(self.X_OFFSET + self.boxSize * position[0]), int(self.Y_OFFSET + self.boxSize * position[1])), self.boxSize * 50)
            else:
                pygame.draw.circle(self.DISPLAY, self.SMALLCOLOR, (int(self.X_OFFSET + self.boxSize * position[0]), int(self.Y_OFFSET + self.boxSize * position[1])), self.boxSize * 5)
            i += 1

        for position in self.bigParticlePositions:
            pygame.gfxdraw.pixel(self.DISPLAY, int(self.X_OFFSET + (position[0] * self.boxSize)), int(self.Y_OFFSET + self.boxSize * position[1]), self.BIGCOLOR)

        meanArray = np.mean(self.bigParticlePositions, axis=0)
        self.writeText(1, 1, "Particula grande:", 20, self.MAPBORDER)
        self.writeText(1, 20, "Posicion media", 20, self.MAPBORDER)
        self.writeText(1, 30, '(' + str(int(meanArray[0])) + ', ' + str(int(meanArray[1])) + ')', 20, self.MAPBORDER)
        self.writeText(1, 50, "Posicion actual", 20, self.MAPBORDER)
        self.writeText(1, 60, '(' + str(int((positions[0])[0])) + ', ' + str(int((positions[0])[1])) + ')', 20, self.MAPBORDER)

    def update(self, positions):
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
        self.drawWalls()
        self.drawCircles(positions)
        pygame.display.update()

    def firstUpdate(self):
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
        self.drawWalls()
        pygame.display.update()
