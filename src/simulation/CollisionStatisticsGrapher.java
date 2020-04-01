package simulation;

import models.ParticleMap;
import utils.Statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CollisionStatisticsGrapher {

    private ParticleMap particleMap;

    private double durationTime;
    private int numberOfExecutions;


    public CollisionStatisticsGrapher(float durationTime, int numberOfExecutions, int numberOfHistogramBars){
        this.durationTime = durationTime;
        this.numberOfExecutions = numberOfExecutions;
    }

    public void run() throws IOException {
        int[][] values = new int[this.numberOfExecutions][];
        for(int e =0; e<this.numberOfExecutions; e++){
            this.particleMap = new ParticleMap(300);
            runExecution(e);
        }

    };


    private void runExecution(int e) throws IOException {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output_files/collisions/collision_times"+e))){
            double currentTime = 0;
            while(currentTime<durationTime){
                particleMap.executeStep();
                currentTime = particleMap.getCurrentTime();

                if(currentTime>=durationTime)
                    break;

                bw.write(String.valueOf(currentTime)+'\n');
            }
        }



    }
}
