# Brownian Motion

## About
This project was made as an assignment for a course called 'Simulación de Sistemas' (Systems simulation).

Brownian Motion is the motion of particles driven by collisions between each other. It is given a container and a group of particles with initial random positions and velocities and the simulation computes the movement of each particle.
The main computation model used is called 'Event driven collisions' that instead of computing each timestep of the simulation, it predicts when the earliest collision is going to happen and then advances the whole system to that point in time. 
Our implementation combines 'Event driven' and normal time step simulation in a way so we can apply 'Cell index method' (see [repository](https://github.com/Fastiz/cell-index-method-simulation)) to improve the performance.

## Project contents
The project contains the program implemented in Java that runs the simulation described in the previous section, a plotter in python to display different results achieved in the simulation and a simulation drawer implemented in python using [pygame](https://www.pygame.org/) that displays the simulation. 

## How to run

### Simulation
For running the simulation mark *src* as sources root and then run *src/Main.java* with the following arguments and format (all optional):

* **particleNumber** - the number of small particles in the container (defaults 200)
* **maxSpeed** - the initial speed of a given particle is a random number between 0 and maxSpeed (uniform distribution) (defaults 0.2)
* **numberOfIterations** - determines the length of the simulation

Note: because of the event driven approach one may realise that by decreasing the particleNumber the simulation seems to speed up. This is because de drawer of the simulation displays each simulation frame with the same time duration ignoring the time transcurred between collisions.

All arguments should be specified in the format *key=value* where key may be one of the previous values.

Arguments example: 
'particleNumber=100 maxSpeed=0.5'

After the simulation ends two files will be created: 'dynamicFile' and 'staticFile'.

### Displaying the simulation
For display python must be installed with [pygame](https://www.pygame.org/) dependency.

After the dependency is installed just run:
python Drawer/Main.py

## Demo

[Video of 200 small particles and a bigger one](https://www.youtube.com/watch?v=cYVbYV5lpyw&feature=youtu.be)

## Additional information

For the assignment it was required to make a presentation about our implementation and to showcase the results obtained. 

[Link to the presentation](https://github.com/migdiluca/brownian-motion/blob/master/SdS-TP3-%20G3_Presentaci%C3%B3n.pdf) (in spanish)
Note that even though it's in spanish there are plenty of plots that may be of interest.

