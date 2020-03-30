package simulation;

import models.Collision;

import models.Index;
import models.Particle;
import models.ParticleList;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class ParticleMap {

    //TODO:
    private final float TIME_STEP = 0.02f;
    private final int SMALL_RATIO = 5;
    private final int BIG_RATIO = 50;
    private final int MAX_SPEED = 1000;
    private final int MAP_SIZE = 500;

    private List<Particle> particleList = new ArrayList<>();
    private PriorityQueue<Collision> collisionsQueue = new PriorityQueue<>();
    private ParticleList[][] map;

    private float indexSize;
    private int indexAmount;

    public ParticleMap(int particleNumber) {
        calculateIndexes();
        generateParticles(particleNumber);
    }

    private void calculateIndexes() {
        indexSize = 2 * MAX_SPEED * TIME_STEP + 2 * BIG_RATIO;
        indexAmount = (int) Math.ceil(MAP_SIZE / indexSize);
        createMap(indexAmount);
    }

    private void createMap(int indexAmount) {
        map = new ParticleList[indexAmount][indexAmount];
        for(int i = 0; i < indexAmount; i++)
            for(int j = 0; j < indexAmount; j++)
                map[i][j] = new ParticleList();
    }

    private void generateParticles(int particleNumber) {
        Particle bigParticle = new Particle((float) MAP_SIZE / 2, (float) MAP_SIZE / 2, 0, 0, BIG_RATIO);
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

        Particle newParticle = new Particle(x, y, (float) Math.random() * MAX_SPEED, (float) Math.random() * MAX_SPEED, SMALL_RATIO);
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

    private void calculateNextCollision() {
        Collision nextCollision = collisionsQueue.poll();
        nextCollision.executeCollision();


        // CON REMOVE IF
//        collisionsQueue.removeIf((collision) -> {
//            boolean mustRemove = !collision.collisionContainsParticle(nextCollision.getParticle1()) && !collision.collisionContainsParticle(nextCollision.getParticle2());
//            if (!mustRemove) {
//                collision.setTimeLeft(collision.getTimeLeft() - nextCollision.getTimeLeft());
//                //calculateNewPositionAndIndex(collision.getParticle1());
//                //calculateNewPositionAndIndex(collision.getParticle2());
//            }
//            return mustRemove;
//        });

        //  CON STREAMS
        collisionsQueue = collisionsQueue.stream()
                .filter(collision -> !collision.collisionContainsParticle(nextCollision.getParticle1()) && !collision.collisionContainsParticle(nextCollision.getParticle2()))
                .peek(collision -> {
                    collision.setTimeLeft(collision.getTimeLeft() - nextCollision.getTimeLeft());
                    calculateNewPositionAndIndex(collision.getParticle1(), nextCollision.getTimeLeft());
                    calculateNewPositionAndIndex(collision.getParticle2(), nextCollision.getTimeLeft());
                })
                .collect(Collectors.toCollection(PriorityQueue::new));

        calculateNewCollisions(nextCollision.getParticle1());
        calculateNewCollisions(nextCollision.getParticle2());

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
                        Collision newCollision = new Collision(particle, otherParticle);
                        if (!Float.isNaN(newCollision.getTimeLeft())) {
                            collisionsQueue.add(newCollision);
                        }
                    }
                }
            }
        }
    }

}
