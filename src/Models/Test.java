package models;

public class Test {

    public Test(){
        Particle p1, p2;

        //Parallel particles
        p1 = new Particle(0, 0, 1, 0, 3);
        p2 = new Particle(5, 0, 10, 0, 1);

        assert (new Collision(p1, p2)).getTimeLeft() == Float.MAX_VALUE;

    }


}
