package simulation;

import models.ParticleMap;

public class SimulationHandler {

    public void run(int particles) {
        ParticleMap particleMap = new ParticleMap(particles);
        for(int i = 0; i < 10000 ;i++) {
            particleMap.executeStep();
        }

        particleMap.printMap();
    }
}
