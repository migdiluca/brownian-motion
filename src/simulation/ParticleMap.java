package simulation;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import models.*;

import java.util.*;
import java.util.stream.Collectors;

public class ParticleMap {

    //TODO:
    private final float TIME_STEP = 0.02f;
    private final int SMALL_RATIO = 5;
    private final int BIG_RATIO = 50;
    private final int MAX_SPEED = 1000;
    private final int MAP_SIZE = 500;

    private float currentTime;

    private List<Particle> particleList = new ArrayList<>();
    private PriorityQueue<Collision> collisionsQueue = new PriorityQueue<>();
    private ParticleList[][] map;

    private Map<Particle, Integer> particlesVersions;

    private float indexSize;
    private int indexAmount;

    public ParticleMap(int particleNumber) {
        this.particlesVersions = new HashMap<>();
        this.currentTime = 0;
        calculateIndexes();
        generateParticles(particleNumber);

        initializeParticleVersions();
        calculateInitialCollisions();
    }

    private void calculateIndexes() {
        indexSize = 2 * MAX_SPEED * TIME_STEP + 2 * BIG_RATIO;
        indexAmount = (int) Math.ceil(MAP_SIZE / indexSize);
        createMap(indexAmount);
    }

    private void calculateInitialCollisions() {
        particleList.forEach(this::calculateNewCollisions);
    }

    private void createMap(int indexAmount) {
        map = new ParticleList[indexAmount][indexAmount];
        for(int i = 0; i < indexAmount; i++)
            for(int j = 0; j < indexAmount; j++)
                map[i][j] = new ParticleList();
    }

    private void initializeParticleVersions(){
        for(Particle particle : this.particleList){
            this.particlesVersions.put(particle, 1);
        }
    }

    private void generateParticles(int particleNumber) {
        //TODO: check mass
        Particle bigParticle = new Particle((float) MAP_SIZE / 2, (float) MAP_SIZE / 2, 0, 0, BIG_RATIO, 1);
        particleList.add(bigParticle);
        addToMap(bigParticle);

        while (particleNumber > 0) {
            generateRandomSmallParticle();
            particleNumber--;
        }
    }

    private void generateRandomSmallParticle() {
        boolean collision = true;
        float x = 0, y = 0;
        while (collision) {
            collision = false;
            x = (float) Math.random() * MAP_SIZE;
            y = (float) Math.random() * MAP_SIZE;
            for (int i = 0; i < particleList.size() && !collision; i++) {
                Particle particle = particleList.get(i);
                float distance = (float) Math.sqrt(Math.pow(x - particle.getPos().getX(), 2) + Math.pow(y - particle.getPos().getY(), 2)) - SMALL_RATIO - particle.getRadius();
                if (distance < 0)
                    collision = true;
            }
        }

        //TODO: set a random initial mass
        Particle newParticle = new Particle(x, y, (float) Math.random() * MAX_SPEED * (Math.random() >= 0.5 ? 1 : -1), (float) Math.random() * MAX_SPEED * (Math.random() >= 0.5 ? 1 : -1), SMALL_RATIO, 1);
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
        for(int i = 0; i < indexAmount; i++)
            for(int j = 0; j < indexAmount; j++) {
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
        boolean flag = true;
        while(!this.collisionsQueue.isEmpty()){
            Collision col = this.collisionsQueue.poll();

            if(!isStale(col)){
                col.executeCollision();
                Map<Particle, Integer> involvedParticles = col.getInvolvedParticles();
                for(Particle p : involvedParticles.keySet()){
                    this.particlesVersions.compute(p, (k, v) -> v != null ? v+1 : null);
                    calculateNewCollisions(p);
                }
            }

        }
        nextTimeStep();
    }

    private void nextTimeStep() {
        particleList.forEach(particle -> calculateNewPositionAndIndex(particle, TIME_STEP));
    }

    private boolean isStale(Collision col){
        Map<Particle, Integer> involvedParticles = col.getInvolvedParticles();

        if(col.getTime()<this.currentTime)
            return true;
        for(Particle p : involvedParticles.keySet()){
            if(this.particlesVersions.get(p) > involvedParticles.get(p))
                return true;
        }

        return false;
    }

    private void calculateNewPositionAndIndex(Particle particle, float time) {
        float newX = particle.getPos().getX() + particle.getVel().getX() * time;
        float newY = particle.getPos().getY() + particle.getVel().getY() * time;

        particle.setPosition(newX, newY);

        particle.getIndex().setX((int) (newX / indexSize));
        particle.getIndex().setX((int) (newX / indexSize));
    }

    private void calculateNewCollisions(Particle particle) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                int xIndex = particle.getIndex().getX() + x;
                int yIndex = particle.getIndex().getY() + y;

                if (xIndex >= 0 && yIndex >= 0 && xIndex < indexAmount && yIndex < indexAmount) {
                    for (Particle otherParticle : map[yIndex][xIndex].getParticles()) {
                        Collision newCollision = new ParticleCollision(particle, otherParticle, particlesVersions.get(particle), particlesVersions.get(otherParticle), this.currentTime);
                        if (!Float.isNaN(newCollision.getTime())) {
                            collisionsQueue.add(newCollision);
                        }
                    }
                }
            }
        }

        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.HORIZONTAL, 0, this.currentTime));
        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.VERTICAL, 0, this.currentTime));
        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.HORIZONTAL, MAP_SIZE, this.currentTime));
        collisionsQueue.add(new WallCollision(particle, particlesVersions.get(particle), WallCollision.VERTICAL, MAP_SIZE, this.currentTime));
    }

}
