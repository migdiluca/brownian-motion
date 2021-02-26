import simulation.Animation;
import simulation.BigParticlePositions;
import simulation.CollisionStatisticsGrapher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Map<String, String> parsedArgs = readArgs(args);

        try{
            Animation animation = new Animation(
                    Optional.ofNullable(parsedArgs.get("particleNumber")).map(Integer::valueOf).orElse(200),
                    Optional.ofNullable(parsedArgs.get("maxSpeed")).map(Double::valueOf).orElse(0.1),
                    Optional.ofNullable(parsedArgs.get("numberOfIterations")).map(Integer::valueOf).orElse(100000));

        }catch (IOException e){
            System.err.println(e);
        }

    }

    private static Map<String, String> readArgs(String[] args){
        Map<String, String> result = new HashMap<>();

        for (String arg: args){
            String[] splitValues = arg.split("=");

            if(splitValues.length != 2) continue;

            result.put(splitValues[0], splitValues[1]);
        }

        return result;
    }
}
