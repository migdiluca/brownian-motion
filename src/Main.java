import models.Collision;
import simulation.ParticleMap;

import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        ParticleMap pm = new ParticleMap(400);
        pm.printMap();
    }
}
