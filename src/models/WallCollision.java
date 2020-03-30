package models;

import java.util.HashMap;
import java.util.Map;

public class WallCollision implements Collision{
    private Map<Particle, Integer> involvedParticles;
    private Particle particle;
    private int pos, type;
    private float time;
    public static final int HORIZONTAL = 0, VERTICAL = 1;


    public WallCollision(Particle particle, int version, int type, int pos, float currentTime){
        this.type = type;
        this.pos = pos;
        this.particle = particle;

        this.involvedParticles = new HashMap<>();
        this.involvedParticles.put(particle, version);

        this.time = currentTime;
        calculateTimeToCollision();
    }

    public void calculateTimeToCollision(){
        float v, r;
        if(type == HORIZONTAL){
            v = particle.getVel().getX();
            r = particle.getPos().getX();
        }else{
            v = particle.getVel().getY();
            r = particle.getPos().getY();
        }

        if(r > pos && v > 0){
            this.time = Float.MAX_VALUE;
        }else if(r < pos && v < 0){
            this.time = Float.MAX_VALUE;
        }else{
            this.time += Math.abs((r-pos)/v);
        }
    }

    @Override
    public float getTime() {
        return this.time;
    }

    @Override
    public void executeCollision() {
        float vx = particle.getVel().getX(),
            vy = particle.getVel().getY();

        if(this.type == HORIZONTAL){
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
