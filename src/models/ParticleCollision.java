package models;

import interfaces.Collision;

import java.util.HashMap;
import java.util.Map;

public class ParticleCollision implements Collision {
    private Map<Particle, Integer> involvedParticles;
    private Particle particle1, particle2;
    private float time;

    public ParticleCollision(float time) {
        this.time = time;
    }
    public ParticleCollision(Particle particle1, Particle particle2, int v1, int v2, float currentTime) {
        this.particle1 = particle1;
        this.particle2 = particle2;
        this.involvedParticles = new HashMap<>();

        this.involvedParticles.put(particle1, v1);
        this.involvedParticles.put(particle2, v2);

        this.time = currentTime;
        calculateTimeToCollision();
    }

    @Override
    public void executeCollision() {
        float dx = particle1.getPos().getX()-particle2.getPos().getX(),
                dy = particle1.getPos().getY()-particle2.getPos().getY(),
                dvx = particle1.getVel().getX()-particle2.getVel().getX(),
                dvy = particle1.getVel().getY()-particle2.getVel().getY(),
                m1 = particle1.getMass(),
                m2 = particle2.getMass(),
                x1 = particle1.getPos().getX(),
                x2 = particle2.getPos().getX(),
                y1 = particle1.getPos().getY(),
                y2 = particle2.getPos().getY(),
                vx1 = particle1.getVel().getX(),
                vx2 = particle2.getVel().getX(),
                vy1 = particle1.getVel().getY(),
                vy2 = particle2.getVel().getY(),
                sigma = (float)Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));


        float J = (2*m1*m2*(dvx*dx + dvy*dy))/(sigma*(m1+m2));

        float Jx =- J*(x1-x2)/sigma, Jy = -J*(y1-y2)/sigma;

        particle1.setVel(vx1 + Jx/m1, vy1 + Jy/m1);
        particle2.setVel(vx2 - Jx/m2, vy2 - Jy/m2);
    }

    @Override
    public float getTime() {
        return time;
    }

    @Override
    public boolean containsParticle(Particle particle) {
        if(this.particle1.equals(particle))
            return true;

        return this.particle2.equals(particle);
    }

    @Override
    public Map<Particle, Integer> getInvolvedParticles(){
        return this.involvedParticles;
    }

    private void calculateTimeToCollision() {
        float x1 = particle1.getPos().getX(),
                x2 = particle2.getPos().getX(),
                y1 = particle1.getPos().getY(),
                y2 = particle2.getPos().getY(),
                vx1 = particle1.getVel().getX(),
                vx2 = particle2.getVel().getX(),
                vy1 = particle1.getVel().getY(),
                vy2 = particle2.getVel().getY(),
                R1 = particle1.getRadius(),
                R2 = particle2.getRadius();

        float a1 = vx1 - vx2,
                a2 = vy1 - vy2,
                b1 = x1 - x2,
                b2 = y1 - y2;


        float A = (float)(Math.pow(a1, 2) + Math.pow(a2, 2));
        float B = (float)2*(b1*a1+b2*a2);
        float C = (float)-(Math.pow(R1+R2,2) - Math.pow(b1, 2) - Math.pow(b2, 2));

        if(A == 0){
            this.time = Float.MAX_VALUE;
            return;
        }

        float insideRoot = (float)(Math.pow(B, 2) - 4*A*C);
        if(insideRoot < 0){
            this.time = Float.MAX_VALUE;
            return;
        }

        float t = (float)(-B - Math.sqrt(insideRoot))/(2*A);

        if(t < 0){
            this.time = Float.MAX_VALUE;
            return;
        }

        this.time += t;
    }

    public void setTime(float time) {
        this.time = time;
    }


}
