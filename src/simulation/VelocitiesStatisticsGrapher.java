package simulation;

import models.Particle;
import models.ParticleMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VelocitiesStatisticsGrapher {
    private ParticleMap particleMap;

    private double durationTime;
    private int numberOfExecutions;
    private int particleNumber;

    public VelocitiesStatisticsGrapher(int particleNumber, double durationTime, int numberOfExecutions) {
        this.durationTime = durationTime;
        this.numberOfExecutions = numberOfExecutions;
        this.particleNumber = particleNumber;
    }

    public void run() throws IOException {

        (new File("output_files")).mkdir();
        (new File("output_files/velocities")).mkdir();

        int[][] values = new int[this.numberOfExecutions][];
        for (int e = 0; e < this.numberOfExecutions; e++) {
            this.particleMap = new ParticleMap(particleNumber);
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

            double[] velocities = new double[particleMap.getParticleList().size()];
            for(int i = 0; i < velocities.length; i++)
                velocities[i] = 0.0;

            int sumAmount = 0;
            while (currentTime < durationTime) {
                particleMap.executeStep();
                currentTime = particleMap.getCurrentTime();

                if (currentTime >=  2 * this.durationTime / 3) {
                    int i = 0;
                    sumAmount++;
                    for (Particle particle : particleMap.getParticleList()) {
                        double velocity = Math.sqrt(Math.pow(particle.getVel().getX(), 2) + Math.pow(particle.getVel().getY(), 2));
                        velocities[i] += velocity;
                        i++;
                    }
                }
            }

            bw.write("#Velocities at 2/3" + '\n');
            for (double velocity : velocities) {
                bw.write(Double.toString(velocity / sumAmount) + '\n');
            }
        }

    }
}
