import simulation.Animation;
import simulation.BigParticlePositions;
import simulation.CollisionStatisticsGrapher;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try{
            Animation animation = new Animation(200, 0.1, 100000);
        }catch (IOException e){
            System.err.println(e);
        }

    }
}
