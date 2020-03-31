package simulation;

import models.ParticleMap;

public class SimulationHandler {

    public void run(int particles) {
        ParticleMap particleMap = new ParticleMap(particles);
        for(int i = 0; i < 40000 ;i++) {
            System.out.println(i);
            particleMap.executeStep();
        }
        long time = System.currentTimeMillis();
        while(System.currentTimeMillis() - time < 1000) {
            particleMap.executeStep();
        }
        particleMap.printMap();

        particleMap.printBigParticle();
    }
}
