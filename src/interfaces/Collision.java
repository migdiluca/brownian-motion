package interfaces;

import models.Particle;

import java.util.Map;

public interface Collision extends Comparable<Collision> {
    double getTime();

    void executeCollision();

    boolean containsParticle(Particle particle);

    Map<Particle, Integer> getInvolvedParticles();

    @Override
    default int compareTo(Collision collision) {
        double result = this.getTime() - collision.getTime();
        if(result < 0)
            return -1;
        else if (result == 0)
            return 0;
        else
            return 1;
    }
}
