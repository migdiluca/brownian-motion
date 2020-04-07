import os
import plotter.utils as utils
import numpy as np
import seaborn as sns
import pandas as pd
import matplotlib.pyplot as plt


def coefficient_of_diffusion():
    print("-> Coefficient of diffusion selected")

    default = utils.ask_boolean("Do you want to use default values? 'y' for yes and 'n' for no : ")
    starting_time = 50
    ending_time = 100
    step_size = 1
    if not default:
        starting_time = utils.ask_for_float("Enter starting time for measuring big particle movement : ")
        ending_time = utils.ask_for_float("Enter ending time for measuring big particle movement : ")
        step_size = starting_time = utils.ask_for_float("Enter a step size for time bins : ")

    print("Running with parameters: starting_time = " + str(
        starting_time) + ", ending_time = " + str(ending_time) + ", step_size: "
          + str(step_size) + ".")

    big_particle(starting_time, ending_time, step_size)

    small_particle(starting_time, ending_time, step_size)


def big_particle(starting_time, ending_time, step_size):
    starting_index = int(starting_time / step_size)

    print("[BIG PARTICLE] Reading files")
    executions = read_file_particle_positions("big_particle")

    times = np.arange(0, ending_time, step_size)

    print("[BIG PARTICLE] Separating data in bins and calculating mean position")
    bins = create_bins(times, executions)

    print("[BIG PARTICLE] Calculating squared distances for each execution")
    bins_with_sd = msd(bins[starting_index:])

    plot(times[starting_index:-1], [np.mean(bin) for bin in bins_with_sd])


def small_particle(starting_time, ending_time, step_size):
    starting_index = int(starting_time / step_size)

    print("[SMALL PARTICLE] Reading files")
    executions = read_file_particle_positions("small_particle")

    times = np.arange(0, ending_time, step_size)

    print("[SMALL PARTICLE] Separating data in bins and calculating mean position")
    bins = create_bins(times, executions)

    print("[SMALL PARTICLE] Calculating squared distances for each execution")
    bins_with_sd = msd(bins[starting_index:])

    plot(times[starting_index:-1], [np.mean(bin) for bin in bins_with_sd])


def read_file_particle_positions(dir):
    folder = "..\\output_files\\coefficient_of_diffusion\\"+dir
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
            last_val = result[-1]
            result.append(((start_t+end_t)/2, last_val[1], last_val[2]))
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
            norm_bin.append(np.linalg.norm(np.array([bin[i][1], bin[i][2]]) - start_array) ** 2)
        norms_bins.append(norm_bin)
    return norms_bins


def mean_position_and_time(values):
    mean_time = np.mean([time for time, x, y in values])
    mean_x = np.mean([x for time, x, y in values])
    mean_y = np.mean([y for time, x, y in values])
    return mean_time, mean_x, mean_y


def plot(times, values):
    t_sustract_min = [t-min(times) for t in times]
    m = linear_regression([(x, y) for x, y in zip(t_sustract_min, values)])
    print("Coefficient of diffusion: D = "+str(m/2))
    plt.plot(times, np.array(t_sustract_min)*m, 'b')
    plt.plot(times, values, 'ro')
    plt.show()

    plot_linear_regression_error(t_sustract_min, values, m)


def linear_regression(data):
    sum_of_xsquared = sum([x**2 for x, y in data])
    sum_of_xy = sum([x*y for x, y in data])

    return sum_of_xy/sum_of_xsquared


def plot_linear_regression_error(times, data, m):
    m_values = np.arange(-0.001, 0.005, 0.000001)
    error_values = [error([(x, y) for x, y in zip(times, data)], m) for m in m_values]
    plt.figure()
    plt.axvline(m, color='r')
    plt.plot(m_values, error_values)
    plt.show()


def error(data, parameter):
    err = 0
    for pair in data:
        x, y = pair
        err = err + (y-x*parameter)**2
    return err



















