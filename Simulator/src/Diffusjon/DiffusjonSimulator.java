/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Kristoffer
 */
public class DiffusjonSimulator {
    private int step;
    private int size;
    private final ArrayList<Location> particles;
    private ArrayList<Location> rookMove;
    private ArrayList<Location> bishopMove;
    private ArrayList<Location> knightMove;
    private ArrayList<Location> kingMove;
    private ArrayList<Location> moves;

    public DiffusjonSimulator(int numberOfParticles, int startPoint, int size) {
        createMoves();
        this.size = size;
        moves = rookMove;
        Location location = new Location(startPoint);
        this.step = 0;
        this.particles = new ArrayList<>();
        for (int i = 0; i < numberOfParticles; i++) {
            particles.add((Location) location.clone());
        }
    }


    public List<Location> simulateOneStep(int dimentions) {
        particles.forEach((Location loc) -> {
            Location move = getMove(loc);
            while (move.getDimentions() > dimentions){
                move = getMove(loc);
            }
            loc.changeLocation(move);
        });
        ArrayList<Location> newLocs = new ArrayList(particles);
        step++;
        return newLocs;
    }
    public int getStep(){
        return step;
    }
    
    private Location getMove(Location loc){
        int moveNum = ThreadLocalRandom.current().nextInt(0, moves.size());
        return moves.get(moveNum);
    }

    private void createMoves() {
        rookMove = new ArrayList<>();
        bishopMove = new ArrayList<>();
        knightMove = new ArrayList<>();
        kingMove = new ArrayList<>();
        
        //rook moves
        rookMove.add(new Location(1,0,0));
        rookMove.add(new Location(-1,0,0));
        rookMove.add(new Location(0,1,0));
        rookMove.add(new Location(0,-1,0));
        rookMove.add(new Location(0,0,1));
        rookMove.add(new Location(0,0,-1));
        
        //bishop moves
        bishopMove.add(new Location(1,1,0));
        bishopMove.add(new Location(-1,1,0));
        bishopMove.add(new Location(1,-1,0));
        bishopMove.add(new Location(-1,1,0));
        
        bishopMove.add(new Location(1,0,1));
        bishopMove.add(new Location(-1,0,1));
        bishopMove.add(new Location(1,0,-1));
        bishopMove.add(new Location(-1,0,-1));
        
        bishopMove.add(new Location(0,1,1));
        bishopMove.add(new Location(0,-1,1));
        bishopMove.add(new Location(0,1,-1));
        bishopMove.add(new Location(0,-1,-1));
    }
    
}
