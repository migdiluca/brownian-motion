import simulation.Animation;
import simulation.CollisionStatisticsGrapher;
import simulation.SimulationHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CollisionStatisticsGrapher c = new CollisionStatisticsGrapher(2.0f, 10, 10);
        c.run();
        c.graph();
        /*try{
            Animation animation = new Animation();
        }catch (IOException e){
            System.err.println(e);
        }*/

    }
}
