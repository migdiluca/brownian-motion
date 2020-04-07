import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

def readFile(number):
    file_location = "../output_files/velocities/velocities" + str(number)
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
        velocities.append(float(line)
                          )
        lineIndex += 1
        line = lines[lineIndex]

    return velocities, lineIndex

def readAllFiles():
    velocities = [[], []]
    for i in range(0, 10):
        velocitiesAux = readFile(i)
        velocities[0].extend(velocitiesAux[0])
        velocities[1].extend(velocitiesAux[1])

    return velocities

def velocities():
    velocities = readAllFiles()

    maxValue = max([max(velocities[0]), max(velocities[1])])
    bins = np.arange(0, maxValue, 0.0025)
    sns.distplot(velocities[0], hist=True, kde=False, bins=bins,
                 hist_kws={'edgecolor':'black', 'weights': np.ones(len(velocities[0]))/len(velocities[0])})
    sns.distplot(velocities[1], hist=True, kde=False, bins=bins,
                 hist_kws={'edgecolor':'black', 'weights': np.ones(len(velocities[1]))/len(velocities[1])})

    plt.xlabel('Velocidad (m/s)')
    plt.ylabel('Probabilidad')
    plt.legend(labels=['Velocidad inicial', 'Velocidad a 2/3 de la simulacion'], loc='upper center', bbox_to_anchor=(0.5, -0.1), shadow=True, ncol=3)
    plt.show()
