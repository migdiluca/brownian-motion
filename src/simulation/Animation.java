package simulation;

import models.Particle;
import models.ParticleMap;

import java.io.*;
import java.util.List;

public class Animation {
    private static final int NUMBER_OF_ITERATIONS = 1000;

    public Animation() throws IOException {
        ParticleMap particleMap = new ParticleMap(300);
        List<Particle> particles = particleMap.getParticleList();

        try(BufferedWriter br = new BufferedWriter(new FileWriter("animation_static"))){
            for(Particle particle : particles){
                br.write(particle.getRadius() + " " + particle.getMass()+"\n");
            }
        }

        try(BufferedWriter br = new BufferedWriter(new FileWriter("animation_dynamic"))){
            for(int i=0; i<NUMBER_OF_ITERATIONS; i++){
                br.write("#T"+i+ " " + particleMap.getCurrentTime() +'\n');
                for(Particle particle : particles){
                    br.write(particle.getPos().getX() +" "+particle.getPos().getY()+'\n');
                }
                particleMap.executeStep();

            }
        }

    }

}
