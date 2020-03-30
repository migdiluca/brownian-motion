package models;

import models.Vector;

public class Particle {
    private Vector pos;
    private Vector vel;
    private float radius;
    private Index index;
    private int id;

    public Particle() {

    }

    public Particle(Vector pos, float radius){
        this.pos = pos;
        this.radius = radius;
    }

    public Particle(float x, float y, float radius){
        this.pos = new Vector(x, y);
        this.radius = radius;
    }

    public boolean IsInsideActionRadiusOf(Particle otherParticle, float actionRadius){
        float squareActionRadiusPlusRadius = (float) Math.pow(actionRadius + radius + otherParticle.radius, 2);
        return squareActionRadiusPlusRadius >= otherParticle.pos.SquaredDistanceFrom(pos);
    }

    public Vector getPos(){
        return pos;
    }

    public Vector getVel(){
        return vel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setPosition(float x, float y){
        this.pos = new Vector(x, y);
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }
}

