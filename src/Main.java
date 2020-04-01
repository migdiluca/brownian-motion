import simulation.Animation;
import simulation.CollisionStatisticsGrapher;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CollisionStatisticsGrapher c = new CollisionStatisticsGrapher(10.0f, 10, 10);

        try{
            c.run();
        }catch (IOException e){
            System.err.println(e);
        }

        /*try{
            Animation animation = new Animation();
        }catch (IOException e){
            System.err.println(e);
        }*/

    }
}
