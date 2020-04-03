import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns


def readFile(number):
    file_location = "../output_files/coefficient_of_diffusion/positionsTemperature" + str(number)
    positionFile = open(file_location, 'r')

    lines = positionFile.readlines()

    line = lines[0]
    speed = float(line)

    positions = [readPosition(line) for line in lines[1::]]
    return positions, speed

def readPosition(line):
    positions = []
    i = 0
    for position in line.split():
        if i > 0:
            positions.append(float(position))
        i += 1

    return positions

def positions():
    fig = plt.figure()
    ax = plt.subplot(111)
    ax.set_xlim(0, 0.5)
    ax.set_ylim(0, 0.5)
    for i in range(0, 3):
        positions, speed = readFile(i)
        x, y = zip(*positions)
        ax.plot(x, y, label = "V = " + str(speed) + "m/s")

    ax.plot(0.25, 0.25, marker = 'o')
    ax.legend(loc='upper center', bbox_to_anchor=(0.5, -0.05), shadow=True, ncol=3)
    plt.show()

positions()