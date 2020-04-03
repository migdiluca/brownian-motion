package simulation;

import models.Particle;
import models.ParticleMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BigParticlePositions {
    private float durationTime;
    private int numberOfExecutions;

    public BigParticlePositions(float durationTime, int numberOfExecutions){
        this.durationTime = durationTime;
        this.numberOfExecutions = numberOfExecutions;
    }

    public void run() throws IOException {
        int[][] values = new int[this.numberOfExecutions][];
        for(int e =0; e<this.numberOfExecutions; e++){
            ParticleMap particleMap = new ParticleMap(300);
            runExecution(e, particleMap);
        }

    };


    private void runExecution(int e, ParticleMap particleMap) throws IOException {
        Particle bigParticle = particleMap.getBigParticle();

        double lastX = bigParticle.getPos().getX(), lastY = bigParticle.getPos().getY();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output_files/coefficient_of_diffusion/positions"+e))){
            double currentTime = 0;
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
