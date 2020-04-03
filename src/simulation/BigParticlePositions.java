package simulation;

import models.Particle;
import models.ParticleMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BigParticlePositions {
    private static final int BIG_PARTICLE = 0;
    private static final int SMALL_PARTICLE = 1;

    private float durationTime;
    private int numberOfExecutions;

    public BigParticlePositions(float durationTime, int numberOfExecutions){
        this.durationTime = durationTime;
        this.numberOfExecutions = numberOfExecutions;
    }

    public void run() throws IOException {
        int[][] values = new int[this.numberOfExecutions][];
        for(int e =0; e<this.numberOfExecutions; e++){
            ParticleMap particleMap;
            particleMap = new ParticleMap(300);
            runExecution(e, particleMap, "big_particle", BIG_PARTICLE);
            particleMap = new ParticleMap(300);
            runExecution(e, particleMap, "small_particle", SMALL_PARTICLE);
        }

    };


    private void runExecution(int e, ParticleMap particleMap, String dir, int type) throws IOException {
        Particle particle = type == BIG_PARTICLE ? particleMap.getBigParticle() : particleMap.getParticleList().get(1);

        double lastX = particle.getPos().getX(), lastY = particle.getPos().getY();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output_files/coefficient_of_diffusion/"+dir+"/positions"+e))){
            double currentTime = 0;
            while(currentTime<durationTime){
                particleMap.executeStep();
                currentTime = particleMap.getCurrentTime();

                if(currentTime>=durationTime || particleMap.didBigParticleCrashed())
                    break;

                double currentX = particle.getPos().getX(), currentY = particle.getPos().getY();
                if(lastX != currentX || lastY != currentY){
                    bw.write(currentTime+" "+currentX+" "+currentY+'\n');
                }
            }
        }
    }
}
