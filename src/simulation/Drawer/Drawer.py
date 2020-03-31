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
    xResolution = 1440
    yResolution = 1000
    WHITE = (255, 255, 255)
    RED = (255, 40, 40)
    BLUE = (20, 20, 245)
    YELLOW = (252, 211, 3)
    BACKGROUND = WHITE
    BLACK = (40, 40, 40)
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
        self.DISPLAY = pygame.display.set_mode((self.xResolution, self.yResolution), 0, 32)
        self.DISPLAY.fill(self.BACKGROUND)

    def drawScale(self):
        array = np.arange(0.0, 1.0, 0.02)
        w, h = pygame.display.get_surface().get_size()
        x = (w - self.SCALE_DISPLAY_SIZE + 30) / self.boxSize
        y = ((h / self.boxSize) / 2) - len(array)

        boxScale = 2

        self.writeText(x + 3, y - 2, "Rapido", 20, self.getColor(1.0))

        for scale in array:
            color = self.getColor(1 - scale)
            self.drawSquare(x, y, color, boxScale)
            y += boxScale

        self.writeText(x + 3, y - 5, "Lento", 20, self.getColor(0.0))

    def writeText(self, x, y, text, size, color):
        font = pygame.font.SysFont("Arial", size)
        label = font.render(text, 1, color)
        self.DISPLAY.blit(label, (x * self.boxSize, y * self.boxSize))

    def drawWalls(self):
        self.DISPLAY.fill(self.BACKGROUND)
        w, h = pygame.display.get_surface().get_size()
        for x in range(0, self.mapSize):
            self.drawSquare(self.X_OFFSET + (x * self.boxSize), self.Y_OFFSET, self.BLACK)
            self.drawSquare(self.X_OFFSET + (x * self.boxSize), self.Y_OFFSET + self.boxSize * (self.mapSize - 1), self.BLACK)

        for y in range(0, self.mapSize):
            self.drawSquare(self.X_OFFSET, self.Y_OFFSET + (self.boxSize * y), self.BLACK)
            self.drawSquare(self.X_OFFSET + self.boxSize * (self.mapSize - 1), self.Y_OFFSET + (self.boxSize * y), self.BLACK)

    def drawSquare(self, x, y, color, boxAmount=1):
        pygame.draw.rect(self.DISPLAY, color,
                         (x, y, self.boxSize * boxAmount, self.boxSize * boxAmount))

    def cleanEmptySquares(self):
        for y in range(len(self.map)):
            for x in range(len(self.map[y])):
                if self.map[y][x] == -1:
                    self.drawSquare(x, y, self.BLUE)

    def getColor(self, scale):
        colorAux = plt.cm.YlOrRd(scale)
        return [colorAux[0] * 255, colorAux[1] * 255, colorAux[2] * 255]

    def drawArrow(self, x, y, rotation=0, scale=1):
        newBox = self.boxSize * self.chunkSize
        lineBold = 0.001 * newBox / 2
        lineLength = (0.8 * newBox / 2)
        arrowSize = (0.15 * newBox / 2)
        xCord = x * newBox + (newBox / 2)
        yCord = y * newBox + (newBox / 2)
        rotationRadians = math.pi * rotation / 180
        sin = math.sin(rotationRadians)
        cos = math.cos(rotationRadians)
        color = self.getColor(scale)
        pygame.gfxdraw.aapolygon(self.DISPLAY, ((xCord - (lineBold * sin), yCord - (lineBold * cos)),
                                                (xCord + (lineBold * sin), yCord + (lineBold * cos)),
                                                (xCord + (lineLength * cos) + (lineBold * sin),
                                                 yCord - (lineLength * sin) + (lineBold * cos)),
                                                (xCord + (lineLength * cos) + (arrowSize * sin),
                                                 yCord - (lineLength * sin) + (arrowSize * cos)),
                                                (xCord + ((lineLength + arrowSize) * cos),
                                                 yCord - ((lineLength + arrowSize) * sin)),
                                                (xCord + (lineLength * cos) - (arrowSize * sin),
                                                 yCord - (lineLength * sin) - (arrowSize * cos)),
                                                (xCord + (lineLength * cos) - (lineBold * sin),
                                                 yCord - (lineLength * sin) - (lineBold * cos))), color)
        pygame.gfxdraw.filled_polygon(self.DISPLAY, ((xCord - (lineBold * sin), yCord - (lineBold * cos)),
                                                     (xCord + (lineBold * sin), yCord + (lineBold * cos)),
                                                     (xCord + (lineLength * cos) + (lineBold * sin),
                                                      yCord - (lineLength * sin) + (lineBold * cos)),
                                                     (xCord + (lineLength * cos) + (arrowSize * sin),
                                                      yCord - (lineLength * sin) + (arrowSize * cos)),
                                                     (xCord + ((lineLength + arrowSize) * cos),
                                                      yCord - ((lineLength + arrowSize) * sin)),
                                                     (xCord + (lineLength * cos) - (arrowSize * sin),
                                                      yCord - (lineLength * sin) - (arrowSize * cos)),
                                                     (xCord + (lineLength * cos) - (lineBold * sin),
                                                      yCord - (lineLength * sin) - (lineBold * cos))), color)

    def drawCircles(self, positions):
        i = 0
        for position in positions:
            if i == 0:
                self.bigParticlePositions.append(position)
                pygame.draw.circle(self.DISPLAY, self.YELLOW, (int(self.X_OFFSET + self.boxSize * position[0]), int(self.Y_OFFSET + self.boxSize * position[1])), self.boxSize * 50)
            else:
                pygame.draw.circle(self.DISPLAY, self.BLUE, (int(self.X_OFFSET + self.boxSize * position[0]), int(self.Y_OFFSET + self.boxSize * position[1])), self.boxSize * 5)
            i += 1

        for position in self.bigParticlePositions:
            pygame.gfxdraw.pixel(self.DISPLAY, int(self.X_OFFSET + (position[0] * self.boxSize)), int(self.Y_OFFSET + self.boxSize * position[1]), self.YELLOW)

        meanArray = np.mean(self.bigParticlePositions, axis=0)
        self.writeText(1, 1, "Particula grande:", 20, self.BLACK)
        self.writeText(1, 20, "Posicion media", 20, self.BLACK)
        self.writeText(1, 30, '(' + str(int(meanArray[0])) + ', ' + str(int(meanArray[1])) + ')', 20, self.BLACK)
        self.writeText(1, 60, "Posicion actual", 20, self.BLACK)
        self.writeText(1, 70, '(' + str(int((positions[0])[0])) + ', ' + str(int((positions[0])[1])) + ')', 20, self.BLACK)

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
