package models;

import interfaces.Collision;

import java.util.HashMap;
import java.util.Map;

public class WallCollision implements Collision {
    private Map<Particle, Integer> involvedParticles;
    private Particle particle;
    private int type;
    private double pos;
    private double time;
    public static final int HORIZONTAL = 0, VERTICAL = 1;


    public WallCollision(Particle particle, int version, int type, double pos, double currentTime){
        this.type = type;
        this.pos = pos;
        this.particle = particle;

        this.involvedParticles = new HashMap<>();
        this.involvedParticles.put(particle, version);

        this.time = currentTime;
        calculateTimeToCollision();
    }

    public void calculateTimeToCollision() {
        double v, r, radius = particle.getRadius();
        if (type == VERTICAL) {
            v = particle.getVel().getX();
            r = particle.getPos().getX();
        } else {
            v = particle.getVel().getY();
            r = particle.getPos().getY();
        }

        if (Double.compare(r+radius, pos) == 0 || Double.compare(r-radius, pos) == 0){
            this.time = Double.POSITIVE_INFINITY;
        }else if(r > pos && v > 0){
            this.time = Double.POSITIVE_INFINITY;
        }else if(r < pos && v < 0){
            this.time = Double.POSITIVE_INFINITY;
        }else{
            this.time += Math.abs((Math.abs(r-pos)-radius)/v);
        }
    }

    @Override
    public double getTime() {
        return this.time;
    }

    @Override
    public void executeCollision() {
        double vx = particle.getVel().getX(),
            vy = particle.getVel().getY();

        if(this.type == VERTICAL){
            particle.setVel(-vx, vy);
        }else{
            particle.setVel(vx, -vy);
        }
    }

    @Override
    public boolean containsParticle(Particle particle){
        return particle.equals(this.particle);
    }

    @Override
    public Map<Particle, Integer> getInvolvedParticles(){
        return this.involvedParticles;
    }
}
