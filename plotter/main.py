from plotter.collisions import collisions


def main():
    option = display_options()
    run_plotter(option)


def display_options():
    print("Simulation plotter. Please be sure that you ran the simulation first.")
    print("Select an option:")
    for i in range(len(options)):
        print(" "+str(i+1)+". "+options[i+1][0])

    option = 0
    flag = True
    while flag:
        input_value = input("Enter your option : ")
        try:
            option = int(input_value)
            if len(options)-1 >= option-1 >= 0:
                flag = False
        except ValueError:
            print("Please enter a valid option")

    return option


def run_plotter(option):
    options[option][1]()


def not_implemented():
    print("Sorry, we haven't implemented that function yet")


options = {
    1: ("Collisions", collisions),
    2: ("Speed", not_implemented),
    3: ("Particle trajectory", not_implemented),
    4: ("Coefficient of diffusion", not_implemented),
    5: ("Graph all", not_implemented)
}

if __name__ == "__main__":
    main()