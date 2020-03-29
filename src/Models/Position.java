package Models;

public class Position {
    private float x, y;

    public Position(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float SquaredDistanceFrom(Position otherPos){
        return SquaredDistanceBetween(this, otherPos);
    }

    public static float SquaredDistanceBetween(Position pos1, Position pos2){
        return (float)(Math.pow(pos1.x-pos2.x, 2) + Math.pow(pos1.y-pos2.y, 2));
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
