package simulation;

import Models.Collision;
import Models.Particle;

import java.util.List;
import java.util.PriorityQueue;

public class ParticleMap {

    List<Particle> particleList;
    PriorityQueue<Collision> colisionsQueue;

    public ParticleMap(List<Particle> particleList) {
        this.particleList = particleList;
        calculateIndexes();
    }

    public void calculateIndexes(){

    }


}
