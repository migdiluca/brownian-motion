package models;

import models.Particle;

public class Collision implements Comparable<Collision> {

    private Particle particle1, particle2;
    private float timeLeft;

    public Collision(float time) {
        timeLeft = time;
    }
    public Collision(Particle particle1, Particle particle2) {
        this.particle1 = particle1;
        this.particle2 = particle2;

        calculateTimeToCollision();
    }

    private void calculateTimeToCollision() {
        float a1 = particle1.getVel().getX() - particle2.getVel().getX(),
                a2 = particle1.getVel().getY() - particle2.getVel().getY(),
                b1 = particle1.getPos().getX() - particle2.getPos().getX(),
                b2 = particle1.getPos().getY() - particle2.getPos().getY(),
                R1 = particle1.getRadius(),
                R2 = particle2.getRadius();

        float A = (float)Math.pow(2*(b1*a1 + b2*a2), 2);
        float B = (float)2*(b1*a1+b2*a2);
        float C = (float)(Math.pow(R1+R2,2) + Math.pow(b1, 2) + Math.pow(b2, 2));

        if(A == 0){
            this.timeLeft = Float.MAX_VALUE;
            return;
        }

        float insideRoot = (float)(Math.pow(B, 2) - 4*A*C);
        if(insideRoot < 0){
            this.timeLeft = Float.MAX_VALUE;
            return;
        }

        float t = (float)(-B - Math.sqrt(insideRoot))/(2*A);

        if(t < 0){
            this.timeLeft = Float.MAX_VALUE;
            return;
        }

        this.timeLeft = t;
    }

    public void executeCollision() {

    }

    public boolean collisionContainsParticle(Particle particle) {
        if(this.particle1.equals(particle))
            return true;

        return this.particle2.equals(particle);
    }

    public float getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Particle getParticle1() {
        return particle1;
    }

    public Particle getParticle2() {
        return particle2;
    }

    @Override
    public int compareTo(Collision colision) {
        float result = this.timeLeft - colision.timeLeft;
        if(result < 0)
            return -1;
        else if (result == 0)
            return 0;
        else
            return 1;
    }
}
