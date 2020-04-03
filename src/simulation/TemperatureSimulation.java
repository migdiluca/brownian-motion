package simulation;

import models.Particle;
import models.ParticleMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TemperatureSimulation {
    private float durationTime;
    private int numberOfExecutions;
    private int particleNumber = 300;
    private List<Double> maxSpeeds;
    private double currentSpeed = 0;

    public TemperatureSimulation(float durationTime, int numberOfExecutions){
        this.durationTime = durationTime;
        this.numberOfExecutions = numberOfExecutions;
    }

    public TemperatureSimulation(int particleNumber, List<Double> maxSpeeds, float durationTime){
        this.durationTime = durationTime;
        this.particleNumber = particleNumber;
        this.maxSpeeds = maxSpeeds;
    }

    public void run() throws IOException {
        int[][] values = new int[this.numberOfExecutions][];
        for(int e =0; e<this.numberOfExecutions; e++){
            ParticleMap particleMap = new ParticleMap(particleNumber);
            runExecution(Integer.toString(e), particleMap);
        }
    };

    public void runTemperature() throws IOException {
        int[][] values = new int[this.numberOfExecutions][];

        for(int i = 0; i <maxSpeeds.size(); i++){
            currentSpeed = maxSpeeds.get(i);
            ParticleMap particleMap = new ParticleMap(particleNumber, currentSpeed);
            runExecution("Temperature" + i, particleMap);
        }
    };

    private void runExecution(String e, ParticleMap particleMap) throws IOException {
        Particle bigParticle = particleMap.getBigParticle();

        double lastX = bigParticle.getPos().getX(), lastY = bigParticle.getPos().getY();
        (new File("output_files")).mkdir();
        (new File("output_files/coefficient_of_diffusion")).mkdir();

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output_files/coefficient_of_diffusion/positions"+e))){
            double currentTime = 0;

            if(currentSpeed > 0)
                bw.write(Double.toString(currentSpeed)+'\n');

            while(currentTime<durationTime){
                particleMap.executeStep();
                currentTime = particleMap.getCurrentTime();

                if(currentTime>=durationTime)
                    break;

                double currentX = bigParticle.getPos().getX(), currentY = bigParticle.getPos().getY();
                if(lastX != currentX || lastY != currentY){
                    bw.write(currentTime+" "+currentX+" "+currentY+'\n');
                }
            }
        }
    }
}
