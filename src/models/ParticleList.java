package models;

import java.util.ArrayList;
import java.util.List;

public class ParticleList {

    private List<Particle> particles = new ArrayList<>();

    public void add(Particle particle) {
        particles.add(particle);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public boolean remove(Particle particle) { return  particles.remove(particle); }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }
}
