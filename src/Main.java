import simulation.Animation;
import simulation.SimulationHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            Animation animation = new Animation();
        }catch (IOException e){
            System.err.println(e);
        }

    }
}
