package simulation;

import models.Particle;
import models.ParticleMap;
import models.ParticleMapNoCellIndex;

import java.io.*;
import java.util.List;

public class Animation {
    public Animation(int particleNumber, double maxSpeed, int numberOfIterations) throws IOException {
        long timeStart = System.currentTimeMillis();
        ParticleMap particleMap = new ParticleMap(particleNumber, maxSpeed);
        List<Particle> particles = particleMap.getParticleList();

        try (BufferedWriter br = new BufferedWriter(new FileWriter("animation_static"))) {
            br.write(particleMap.getMapSize() + "\n");
            br.write(particleMap.getMaxSpeed() + "\n");
        }

        try (BufferedWriter br = new BufferedWriter(new FileWriter("animation_dynamic"))) {
            for (int i = 0; i < numberOfIterations; i++) {
                br.write("#T" + i + " " + particleMap.getCurrentTime() + '\n');
                for (Particle particle : particles) {
                    br.write(particle.getPos().getX() * 1000 + " " + particle.getPos().getY() * 1000 + '\n');
                }
                particleMap.executeStep();
            }
        }

        System.out.println(System.currentTimeMillis() - timeStart);

    }

}
