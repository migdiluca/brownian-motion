package models;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import interfaces.Collision;
import models.*;

import java.util.*;

public class ParticleMap {

    //TODO:
    private final double TIME_STEP = 0.01;
    private final int SMALL_RATIO = 5;
    private final int BIG_RATIO = 50;
    private final int MAX_SPEED = 1000;
    private final int MAP_SIZE = 500;

    private double currentTime;
    private int numberOfCollisions;

    private List<Particle> particleList = new ArrayList<>();
    private PriorityQueue<Collision> collisionsQueue = new PriorityQueue<>();
    private ParticleList[][] map;

    private Map<Particle, Integer> particlesVersions;

    private Particle bigParticle;

    private double indexSize;
    private int indexAmount;

    public ParticleMap(int particleNumber) {
        this.particlesVersions = new HashMap<>();
        this.currentTime = 0;
        this.numberOfCollisions = 0;
        calculateIndexes();
        System.out.println("Generating particles...");
        generateParticles(particleNumber);
        System.out.println("Done generating");
        initializeParticleVersions();
        calculateInitialCollisions();
    }

    private void calculateIndexes() {
        indexSize = (2 * MAX_SPEED * TIME_STEP) + (2 * BIG_RATIO);
        indexAmount = (int) Math.ceil(MAP_SIZE / indexSize);
        createMap(indexAmount);
        System.out.println(indexSize);
        System.out.println(indexAmount);
    }

    private void calculateInitialCollisions() {
        particleList.forEach(p -> calculateNewCollisionsExceptInvolved(p, null));
    }

    private void createMap(int indexAmount) {
        map = new ParticleList[indexAmount][indexAmount];
        for (int i = 0; i < indexAmount; i++)
            for (int j = 0; j < indexAmount; j++)
                map[i][j] = new ParticleList();
    }

    private void initializeParticleVersions() {
        for (Particle particle : this.particleList) {
            this.particlesVersions.put(particle, 1);
        }
    }

    private void generateParticles(int particleNumber) {
        //TODO: check mass
        Particle bigParticle = new Particle((double) MAP_SIZE / 2, (double) MAP_SIZE / 2, 0, 0, BIG_RATIO, 1);
        particleList.add(bigParticle);
        addToMap(bigParticle);

        this.bigParticle = bigParticle;

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

        //TODO: set a random initial mass
        Particle newParticle = new Particle(x, y, (double) Math.random() * MAX_SPEED * (Math.random() >= 0.5 ? 1 : -1), (double) Math.random() * MAX_SPEED * (Math.random() >= 0.5 ? 1 : -1), SMALL_RATIO, 1);
        particleList.add(newParticle);
        addToMap(newParticle);
    }

    private void addToMap(Particle particle) {
        int xIndex = (int) Math.floor(particle.getPos().getX() / indexSize);
        int yIndex = (int) Math.floor(particle.getPos().getY() / indexSize);
        particle.setIndex(new Index(xIndex, yIndex));
        map[yIndex][xIndex].add(particle);
    }

    public void printMap() {
        for (int i = 0; i < indexAmount; i++)
            for (int j = 0; j < indexAmount; j++) {
                System.out.println("INDICE: (" + j + ", " + i + ")");
                for (Particle p : map[i][j].getParticles()) {
                    System.out.println(p.getPos());
                }
            }
    }

    public void printBigParticle() {
        particleList.stream().filter(particle -> particle.getRadius() == BIG_RATIO).forEach(particle -> System.out.println(particle.getPos()));
    }


    public void executeStep() {
        while(!executeStepAux()){

        }
    }

    public boolean executeStepAux() {
        while (!this.collisionsQueue.isEmpty()) {
            Collision col = this.collisionsQueue.peek();

            if (!isStale(col)) {
                this.numberOfCollisions++;
                if (col.getTime() - currentTime <= TIME_STEP) {
                    this.collisionsQueue.poll();
                    advanceTime(col.getTime() - currentTime);

                    col.executeCollision();

                    Map<Particle, Integer> involvedParticles = col.getInvolvedParticles();
                    for (Particle p : involvedParticles.keySet()) {
                        this.particlesVersions.compute(p, (k, v) -> v != null ? v + 1 : null);
                        calculateNewCollisionsExceptInvolved(p, involvedParticles);
                    }
                    return true;
                } else {
                    advanceTime(TIME_STEP);
                }
            } else {
                this.collisionsQueue.poll();
            }
        }
        advanceTime(TIME_STEP);
        return false;
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

        int xNewIndex = (int) (newX / indexSize);
        int yNewIndex = (int) (newY / indexSize);

        if (xNewIndex != particle.getIndex().getX() || yNewIndex != particle.getIndex().getY()) {
            map[particle.getIndex().getY()][particle.getIndex().getX()].remove(particle);
            map[yNewIndex][xNewIndex].add(particle);
            particle.getIndex().setX(xNewIndex);
            particle.getIndex().setY(yNewIndex);
            calculateNewCollisionsExceptInvolved(particle, null);
        }

        addWallsCollisions(particle);
    }

    private void calculateNewCollisionsExceptInvolved(Particle particle, Map<Particle, Integer> involvedParticles) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                int xIndex = particle.getIndex().getX() + x;

                int yIndex = particle.getIndex().getY() + y;

                if (xIndex >= 0 && yIndex >= 0 && xIndex < indexAmount && yIndex < indexAmount) {
                    for (Particle otherParticle : map[yIndex][xIndex].getParticles()) {
                        if (!particle.equals(otherParticle) && (involvedParticles == null || !involvedParticles.containsKey(otherParticle))) {
                            Collision newCollision = new ParticleCollision(particle, otherParticle, particlesVersions.get(particle), particlesVersions.get(otherParticle), this.currentTime);
                            if (!Double.isInfinite(newCollision.getTime())) {
                                collisionsQueue.add(newCollision);
                            }
                        }
                    }
                }
            }
        }

        addWallsCollisions(particle);
    }

    private void addWallsCollisions(Particle particle) {
        int restFactor = 1;
        if (MAP_SIZE % indexSize > 0)
            restFactor++;
        if (particle.getVel().getX() < 0 && particle.getIndex().getX() == 0)
            collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.VERTICAL, 0, this.currentTime));
        if (particle.getVel().getY() < 0 && particle.getIndex().getY() == 0)
            collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.HORIZONTAL, 0, this.currentTime));
        if (particle.getVel().getX() > 0 && particle.getIndex().getX() >= indexAmount - restFactor)
            collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.VERTICAL, MAP_SIZE, this.currentTime));
        if (particle.getVel().getY() > 0 && particle.getIndex().getY() >= indexAmount - restFactor)
            collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.HORIZONTAL, MAP_SIZE, this.currentTime));
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public int getNumberOfCollisions() {
        return numberOfCollisions;
    }

    public Particle getBigParticle(){
        return this.bigParticle;
    }
}
