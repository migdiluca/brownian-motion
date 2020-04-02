package simulation;

import models.Particle;
import models.ParticleMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VelocitiesStatisticsGrapher {
    private ParticleMap particleMap;

    private double durationTime;
    private int numberOfExecutions;


    public VelocitiesStatisticsGrapher(double durationTime, int numberOfExecutions) {
        this.durationTime = durationTime;
        this.numberOfExecutions = numberOfExecutions;
    }

    public void run() throws IOException {

        (new File("output_files")).mkdir();
        (new File("output_files/velocities")).mkdir();

        int[][] values = new int[this.numberOfExecutions][];
        for (int e = 0; e < this.numberOfExecutions; e++) {
            this.particleMap = new ParticleMap(300);
            runExecution(e);
        }

    }

    private void runExecution(int e) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output_files/velocities/velocities" + e))) {
            double currentTime = 0;

            bw.write("#Velocities at 0" + '\n');

            for (Particle particle : particleMap.getParticleList()) {
                double velocity = Math.sqrt(Math.pow(particle.getVel().getX(), 2) + Math.pow(particle.getVel().getY(), 2));
                bw.write(Double.toString(velocity) + '\n');
            }

            while (currentTime < durationTime) {
                particleMap.executeStep();
                double previousTime = currentTime;
                currentTime = particleMap.getCurrentTime();

                if (previousTime <  2 * this.durationTime / 3 && currentTime >=  2 * this.durationTime / 3) {
                    bw.write("#Velocities at 2/3" + '\n');

                    for (Particle particle : particleMap.getParticleList()) {
                        double velocity = Math.sqrt(Math.pow(particle.getVel().getX(), 2) + Math.pow(particle.getVel().getY(), 2));
                        bw.write(Double.toString(velocity) + '\n');
                    }
                }
            }
        }

    }
}
