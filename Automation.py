import os
import re
import numpy as np


thresholdValues = np.geomspace(10, 10000, 6)

for threshVal in thresholdValues:
    # Read in the file
    path = os.getcwd()
    file_name = path + "/config/deliberative.xml"

    with open(file_name, 'r') as file:
        file_data = file.read()

    # Replace the target string
    expression_to_replace = re.search(
        r"<tasks number=\"\S*\"", file_data).group(0)

    number = threshVal  # Change this to do different simulations
    file_data = file_data.replace(
        expression_to_replace, "<tasks number=\"{:3d.2f}\"".format(number))

    # Write the file out again
    with open(file_name, 'w') as file:
        file.write(file_data)
    cmd = "java -jar ../logist/logist.jar config/deliberative.xml deliberative-main"
    os.system(cmd)
