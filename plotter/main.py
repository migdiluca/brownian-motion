from plotter.collisions import collisions
from plotter.velocities import velocities


def main():
    option = display_options()
    run_plotter(option)


def display_options():
    print("Simulation plotter. Please be sure that you ran the simulation first.")
    print("Select an option:")
    print(" 1. Collisions")
    print(" 2. Speed")
    print(" 3. Particle trajectory")
    print(" 4. Coefficient of diffusion")
    print(" 5. Graph all")

    option = 0
    flag = True
    while flag:
        input_value = input("Enter your option : ")
        try:
            option = int(input_value)
            if 5 >= option >= 1:
                flag = False
        except ValueError:
            print("Please enter a valid option")

    return option


def run_plotter(option):
    options = {
        1: collisions,
        2: velocities,
        3: not_implemented,
        4: not_implemented,
        5: not_implemented
    }
    options[option]()


def not_implemented():
    print("Sorry, we haven't implemented that function yet")


if __name__ == "__main__":
    main()