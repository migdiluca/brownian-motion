package models;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import interfaces.Collision;
import models.*;

import java.util.*;

public class ParticleMapNoCellIndex {

    private final double SMALL_RATIO = 0.005;
    private final double BIG_RATIO = 0.05;
    private final double MAP_SIZE = 0.5;
    private double MAX_SPEED = 0.1;

    private double currentTime;

    private List<Particle> particleList = new ArrayList<>();
    private PriorityQueue<Collision> collisionsQueue = new PriorityQueue<>();

    private Map<Particle, Integer> particlesVersions;

    public ParticleMapNoCellIndex(int particleNumber) {
        this.particlesVersions = new HashMap<>();
        this.currentTime = 0;
        System.out.println("Generating particles...");
        generateParticles(particleNumber);
        System.out.println("Done generating");
        initializeParticleVersions();
        calculateInitialCollisions();
    }

    private void calculateInitialCollisions() {
        particleList.forEach(p -> calculateNewCollisionsExceptInvolved(p, null));
    }

    private void initializeParticleVersions() {
        for (Particle particle : this.particleList) {
            this.particlesVersions.put(particle, 1);
        }
    }

    private void generateParticles(int particleNumber) {
        //TODO: check mass
        Particle bigParticle = new Particle((double) MAP_SIZE / 2, (double) MAP_SIZE / 2, 0, 0, BIG_RATIO, 0.1);
        particleList.add(bigParticle);

        while (particleNumber > 0) {
            generateRandomSmallParticle();
            particleNumber--;
        }
    }

    private void generateRandomSmallParticle() {
        boolean collision = true;
        double x = 0, y = 0;
        while (collision) {
            collision = false;
            x = SMALL_RATIO + Math.random() * (MAP_SIZE - (2 * SMALL_RATIO));
            y = SMALL_RATIO + Math.random() * (MAP_SIZE - (2 * SMALL_RATIO));
            for (int i = 0; i < particleList.size() && !collision; i++) {
                Particle particle = particleList.get(i);
                double distance = (double) Math.sqrt(Math.pow(x - particle.getPos().getX(), 2) + Math.pow(y - particle.getPos().getY(), 2)) - SMALL_RATIO - particle.getRadius();
                if (distance < 0)
                    collision = true;
            }
        }

        Vector vel = generateRandomSpeed();
        Particle newParticle = new Particle(x, y, vel.getX(), vel.getY(), SMALL_RATIO, 0.0001);
        particleList.add(newParticle);
    }

    private Vector generateRandomSpeed() {
        double module = Math.random() * MAX_SPEED;
        double angle = Math.random() * 2 * Math.PI;
        return new Vector(Math.cos(angle) * module, Math.sin(angle) * module);
    }

    public void printBigParticle() {
        particleList.stream().filter(particle -> particle.getRadius() == BIG_RATIO).forEach(particle -> System.out.println(particle.getPos()));
    }

    public void executeStep() {
        while (!this.collisionsQueue.isEmpty()) {
            Collision col = this.collisionsQueue.poll();
            if (!isStale(col)) {
                advanceTime(col.getTime() - currentTime);

                col.executeCollision();

                Map<Particle, Integer> involvedParticles = col.getInvolvedParticles();
                for (Particle p : involvedParticles.keySet()) {
                    this.particlesVersions.compute(p, (k, v) -> v != null ? v + 1 : null);
                    calculateNewCollisionsExceptInvolved(p, involvedParticles);
                }
                return;
            }
        }
    }

    private void advanceTime(double step) {
        particleList.forEach(particle -> calculateNewPositionAndIndex(particle, step));
        this.currentTime = step + this.currentTime;
    }

    private boolean isStale(Collision col) {
        Map<Particle, Integer> involvedParticles = col.getInvolvedParticles();

        if (col.getTime() < this.currentTime || col.getTime() == Double.POSITIVE_INFINITY)
            return true;
        for (Particle p : involvedParticles.keySet()) {
            if (this.particlesVersions.get(p) > involvedParticles.get(p))
                return true;
        }

        return false;
    }

    private void calculateNewPositionAndIndex(Particle particle, double time) {
        double newX = particle.getPos().getX() + particle.getVel().getX() * time;
        double newY = particle.getPos().getY() + particle.getVel().getY() * time;

        particle.setPosition(newX, newY);

        addWallsCollisions(particle);
    }

    private void calculateNewCollisionsExceptInvolved(Particle particle, Map<Particle, Integer> involvedParticles) {

        for (Particle otherParticle : particleList) {
            if (!particle.equals(otherParticle) && (involvedParticles == null || !involvedParticles.containsKey(otherParticle))) {
                Collision newCollision = new ParticleCollision(particle, otherParticle, particlesVersions.get(particle), particlesVersions.get(otherParticle), this.currentTime);
                if(involvedParticles == null || !involvedParticles.containsKey(otherParticle)){
                    if (!Double.isInfinite(newCollision.getTime())) {
                        collisionsQueue.add(newCollision);
                    }
                }
            }
        }

        addWallsCollisions(particle);
    }

    private void addWallsCollisions(Particle particle) {
        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.VERTICAL, 0, this.currentTime));
        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.HORIZONTAL, 0, this.currentTime));
        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.VERTICAL, MAP_SIZE, this.currentTime));
        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.HORIZONTAL, MAP_SIZE, this.currentTime));
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }
}
