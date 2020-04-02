import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

def readFile():
    file_location = "../output_files/velocities/velocities0"
    velocitiesFile = open(file_location, 'r')

    velocitiesLines = velocitiesFile.readlines()

    velocities = []

    v1, lineIndex = readOneTimeVelocities(velocitiesLines)
    velocities.append(v1)

    v2, index = readOneTimeVelocities(velocitiesLines[lineIndex::])
    velocities.append(v2)

    return velocities

def readOneTimeVelocities(lines):
    lineIndex = 0
    line = lines[0]
    while line[0] == '#':
        lineIndex += 1
        line = lines[lineIndex]

    velocities = []
    while lineIndex < len(lines) - 1 and line[0] != '#':
        velocities.append(float(line) / 1000)
        lineIndex += 1
        line = lines[lineIndex]

    return velocities, lineIndex

def velocities():
    velocities = readFile()

    maxValue = max([max(velocities[0]), max(velocities[1])])
    bins = np.arange(0, maxValue, 0.05)
    sns.distplot(velocities[0], hist=True, kde=False, bins = bins,
                 hist_kws={'edgecolor':'black', 'weights': np.ones(len(velocities[0]))/len(velocities[0])})
    sns.distplot(velocities[1], hist=True, kde=False, bins = bins,
                 hist_kws={'edgecolor':'black', 'weights': np.ones(len(velocities[1]))/len(velocities[1])})

    plt.xlabel('Velocidad')
    plt.ylabel('Probabilidad')
    plt.legend(labels=['Velocidad inicial','Velocidad a 2/3 de la simulacion'])
    plt.show()
