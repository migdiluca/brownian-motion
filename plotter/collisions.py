import numpy as np
import matplotlib.pyplot as plt
import os
import seaborn as sns


import plotter.utils as utils


def collisions():
    print("-> Collisions selected")
    default = utils.ask_boolean("Do you want to use default values? 'y' for yes and 'n' for no : ")

    step = 0.0001
    if not default:
        step = ask_for_inputs()

    print("Running with parameters: step = "+str(step) + ".")

    executions = read_file_collisions()

    time_differences = []
    for execution in executions:
        time_differences.extend([execution[i]-execution[i-1] for i in range(1, len(execution))])

    pdf_of_collisions(time_differences, step)


def ask_for_inputs():
    step = utils.ask_for_float("Enter the step size for the bins : ")
    return step


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


def pdf_of_collisions(time_differences, step):
    max_value = max(time_differences)
    bins = np.arange(0, max_value, step)
    sns.distplot(time_differences, hist=True, kde=False, bins=bins,
                 hist_kws={'edgecolor': 'black', 'weights': np.ones(len(time_differences)) / len(time_differences)})
    plt.xlabel('Tiempo entre colisiones (s)')
    plt.ylabel('Probabilidad')
    plt.show()

