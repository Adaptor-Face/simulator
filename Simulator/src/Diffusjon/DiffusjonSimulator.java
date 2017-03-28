/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

import java.util.ArrayList;

/**
 *
 * @author Kristoffer
 */
public class DiffusjonSimulator {
    private int step;
    private ArrayList<Location> particles;

    public DiffusjonSimulator(int numberOfParticles, int startPoint) {
        Location location = new Location(startPoint);
        this.step = 0;
        this.particles = new ArrayList<>();
        for (int i = 0; i < numberOfParticles; i++) {
            particles.add((Location) location.clone());
        }
    }


    public void simulateOneStep() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public int getStep(){
        return step;
    }
    
}
