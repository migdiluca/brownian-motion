def ask_for_float(input_message, error_message="Please enter a valid option"):
    value = 0
    flag = True
    while flag:
        input_value = input(input_message)
        try:
            value = float(input_value)
            flag = False
        except ValueError:
            print(error_message)
    return value


def ask_boolean(input_message, error_message="Please enter a valid option"):
    value = False
    flag = True
    while flag:
        input_value = input(input_message)
        if input_value == "y":
            value = True
            flag = False
        elif input_value == "n":
            value = False
            flag = False
        else:
            print(error_message)

    return value