import os
import plotter.utils as utils
import numpy as np
import seaborn as sns
import pandas as pd
import matplotlib.pyplot as plt


def coefficient_of_diffusion():
    print("-> Coefficient of diffusion selected")

    default = utils.ask_boolean("Do you want to use default values? 'y' for yes and 'n' for no : ")
    starting_time = 0
    ending_time = 11
    step_size = 1
    if not default:
        starting_time = utils.ask_for_float("Enter starting time for measuring big particle movement : ")
        ending_time = utils.ask_for_float("Enter ending time for measuring big particle movement : ")
        step_size = starting_time = utils.ask_for_float("Enter a step size for time bins")

    starting_index = int(starting_time/step_size)

    print("Reading files")
    executions = read_file_particle_positions()

    times = np.arange(0, ending_time, step_size)

    print("Separating data in bins and calculating mean position")
    bins = create_bins(times, executions)

    print("Calculating squared distances for each execution")
    bins_with_sd = msd(bins[starting_index:])

    plot(times[starting_index:], bins_with_sd)


def read_file_particle_positions():
    folder = "..\\output_files\\coefficient_of_diffusion"
    values = []
    for filename in os.listdir(folder):
        with open(folder + "\\" + filename, 'r') as f:  # open in readonly mode
            lines = [line.rstrip() for line in f]
            new_values = []
            for line in lines:
                time, x, y = line.split(" ")
                new_values.append((float(time), float(x), float(y)))
            values.append(new_values)
    return values


def create_bins(times, executions):
    bins = [[] for x in range(len(times) - 1)]

    for execution in executions:
        new_values = values_in_range(times, execution)
        for i in range(len(new_values)):
            bins[i].append(new_values[i])

    return bins


def values_in_range(times, execution):
    result = []

    start_t = times[0]
    for end_t in times[1:]:
        new_values = []
        for value in execution:
            if start_t <= value[0] < end_t:
                new_values.append(value)
            elif value[0] >= end_t:
                break
        if len(new_values) == 0:
            result.append(result[-1])
        elif len(new_values) == 1:
            result.append(new_values[0])
        else:
            result.append(mean_position_and_time(new_values))
        start_t = end_t

    return result


def msd(bins):
    norms_bins = [[0 for vec in bins[0]]]
    starting_values = bins[0]
    for bin in bins[1:]:
        norm_bin = []
        for i in range(len(bin)):
            start_value = starting_values[i]
            start_array = np.array([start_value[1], start_value[2]])
            norm_bin.append(np.linalg.norm(np.array([bin[i][1], bin[i][2]]) - start_array)**2)
        norms_bins.append(norm_bin)
    return norms_bins


def mean_position_and_time(values):
    mean_time = np.mean([time for time, x, y in values])
    mean_x = np.mean([x for time, x, y in values])
    mean_y = np.mean([y for time, x, y in values])
    return mean_time, mean_x, mean_y


def plot(times, values):
    df = pd.DataFrame(np.array([list(a) for a in zip(times, [np.mean(value) for value in values])]), columns=["time", "DCM"])
    sns.lmplot(x="time", y="DCM", data=df, ci=None)
    plt.show()