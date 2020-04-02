import numpy as np
import matplotlib.pyplot as plt
import os

import plotter.utils as utils


def collisions():
    print("-> Collisions selected")
    default = utils.ask_boolean("Do you want to use default values? 'y' for yes and 'n' for no : ")

    step = 0.5
    min = 0
    max = 10
    if not default:
        step, min, max = ask_for_inputs()

    executions = read_file_collisions()

    histograms = [np.histogram(execution, np.arange(min, max, step)) for execution in executions]

    accumulated_collisions(histograms)
    pdf_of_collisions(histograms)



def ask_for_inputs():
    min = utils.ask_for_float("Enter the minimum value for the time of the first collision : ")
    max = utils.ask_for_float("Enter the maximum value for the time of the last collision : ")
    step = utils.ask_for_float("Enter the step size for the bins : ")
    return step, min, max





def read_file_collisions():
    folder = "..\\output_files\\collisions"
    values = []
    for filename in os.listdir(folder):
        with open(folder + "\\" + filename, 'r') as f:  # open in readonly mode
            lines = [line.rstrip() for line in f]
            new_values = []
            for line in lines:
                new_values.append(float(line))
            values.append(new_values)
    return values


def accumulated_collisions(histograms):
    reduced_histograms = [histogram[0] for histogram in histograms]

    accumulated_values = []
    for i in range(len(reduced_histograms)):
        new_row = [reduced_histograms[i][0]]
        for j in range(len(reduced_histograms[i]))[1::]:
            new_row.append(reduced_histograms[i][j] + new_row[j-1])
        accumulated_values.append(new_row)

    times = histograms[0][1][1::]

    frequencies = [np.array(histogram)/np.array(times) for histogram in accumulated_values]

    means = [np.mean(list(values)) for values in zip(*frequencies)]
    stds = [np.std(list(values)) for values in zip(*frequencies)]

    plt.figure()
    plt.errorbar(times, means, yerr=stds, elinewidth=0.5)
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Frecuencia de colisiones (1/s)")
    plt.show()


def pdf_of_collisions(histograms):
    reduced_histograms = [histogram[0] for histogram in histograms]

    normalized_histograms = [[i/sum(histogram) for i in histogram] for histogram in reduced_histograms]

    means = [np.mean(list(values)) for values in zip(*normalized_histograms)]
    stds = [np.std(list(values)) for values in zip(*normalized_histograms)]

    times = histograms[0][1][1::]

    plt.figure()
    plt.errorbar(times, means, yerr=stds, elinewidth=0.5)
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Cantidad de colisiones")
    plt.show()
