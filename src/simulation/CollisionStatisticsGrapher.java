package simulation;

import interfaces.SimulationGrapher;
import models.ParticleMap;
import utils.Statistics;

public class CollisionStatisticsGrapher implements SimulationGrapher {

    private ParticleMap particleMap;

    private double durationTime;
    private int numberOfExecutions;
    private int numberOfHistogramBars;

    private double[][] histogram;

    public CollisionStatisticsGrapher(float durationTime, int numberOfExecutions, int numberOfHistogramBars){
        this.durationTime = durationTime;
        this.numberOfExecutions = numberOfExecutions;
        this.numberOfHistogramBars = numberOfHistogramBars;
    }

    @Override
    public void run(){
        int[][] values = new int[this.numberOfExecutions][];
        for(int e =0; e<this.numberOfExecutions; e++){
            this.particleMap = new ParticleMap(300);
            values[e] = runExecution(e);
        }

        this.histogram = calculateStdAndMean(values);
    };

    @Override
    public void graph(){
        //TODO:
        for(int t=0; t<histogram.length; t++){
            System.out.print(" (" + this.histogram[t][0] + ", " + this.histogram[t][1] + ")");
        }
    }

    private int[] runExecution(int e){
        int[] histogram = new int[this.numberOfHistogramBars];

        double currentTime = 0;
        while(currentTime<durationTime){
            particleMap.executeStep();
            currentTime = particleMap.getCurrentTime();

            if(currentTime>=durationTime)
                break;

            histogram[(int)((currentTime/durationTime)*this.numberOfHistogramBars)]++;
        }

        return histogram;

    }

    private double[][] calculateStdAndMean(int[][] values){
        double[][] meanAndStd = new double[values.length][];
        for(int e=0; e<this.numberOfExecutions; e++){
            double[] row = getRowAsDouble(values, e);

            double mean = Statistics.mean(row);
            double std = Statistics.std(row, mean);

            meanAndStd[e] = new double[]{mean, std};
        }

        return meanAndStd;
    }

    private double[] getRowAsDouble(int[][] array, int rowNumber){
        double[] result = new double[array[0].length];
        for(int i = 0; i < array.length; i++){
            result[i] = array[rowNumber][i];
        }
        return result;
    }
}
