package utils;

public class Statistics {
    public static double mean(double[] values){
        double sum = 0;
        for(double value : values){
            sum += value;
        }
        return sum/values.length;
    }

    public static double std(double[] values, double mean){
        double sum = 0;
        for(int i=0; i<values.length; i++){
            sum += Math.pow(values[i]-mean, 2);
        }
        return Math.sqrt(sum/values.length);
    }

    public static double std(double[] values){
        return std(values, mean(values));
    }
}
