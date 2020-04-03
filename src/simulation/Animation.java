package simulation;

import models.Particle;
import models.ParticleMap;
import models.ParticleMapNoCellIndex;

import java.io.*;
import java.util.List;

public class Animation {
    private static final int NUMBER_OF_ITERATIONS = 100000;

    public Animation(int particleNumber, double maxSpeed) throws IOException {
        long timeStart = System.currentTimeMillis();
        ParticleMap particleMap = new ParticleMap(particleNumber, maxSpeed);
        List<Particle> particles = particleMap.getParticleList();

        try (BufferedWriter br = new BufferedWriter(new FileWriter("animation_static"))) {
            br.write(particleMap.getMapSize() + "\n");
            br.write(particleMap.getMaxSpeed() + "\n");
        }

        try (BufferedWriter br = new BufferedWriter(new FileWriter("animation_dynamic"))) {
            for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                br.write("#T" + i + " " + particleMap.getCurrentTime() + '\n');
                for (Particle particle : particles) {
                    br.write(particle.getPos().getX() * 1000 + " " + particle.getPos().getY() * 1000 + '\n');
                }
                particleMap.executeStep();
                if (particleMap.didBigParticleCrashed())
                    System.out.println("CRASHED");
            }
        }

        particleMap.printBigParticle();

        System.out.println(System.currentTimeMillis() - timeStart);

    }

}
