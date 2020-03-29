package Models;

public class Collision implements Comparable<Collision> {

    private Particle particle1, particle2;
    private float timeLeft;

    public Collision(Particle particle1, Particle particle2) {
        this.particle1 = particle1;
        this.particle2 = particle2;
        this.timeLeft = calculateTimeToCollision();
    }

    private float calculateTimeToCollision() {
        return 5.0f;
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
