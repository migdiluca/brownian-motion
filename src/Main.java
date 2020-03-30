import models.Collision;
import simulation.ParticleMap;
import simulation.SimulationHandler;

import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        SimulationHandler simulationHandler = new SimulationHandler();
        simulationHandler.run(300);
    }
}
