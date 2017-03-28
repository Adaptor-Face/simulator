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

    private int numberOfParticles, startPoint, size, step;
    private final ArrayList<Location> particles;
    private ArrayList<Location> rookMove;
    private ArrayList<Location> bishopMove;
    private ArrayList<Location> knightMove;
    private ArrayList<Location> kingMove;
    private ArrayList<Location> moves;
    private ArrayList<Location> newLocs;

    public DiffusjonSimulator(int numberOfParticles, int startPoint, int size) {
        createMoves();
        this.numberOfParticles = numberOfParticles;
        this.startPoint = startPoint;
        this.size = size;
        this.particles = new ArrayList<>();
        moves = rookMove;
        reset();
    }

    public void simulateOneStep(int dimentions) {
        particles.forEach((Location loc) -> {
            
            Location move = getMove(loc);
            while (move.getDimentions() > dimentions) {
                move = getMove(loc);
            }
            loc.changeLocation(move);
        });
        newLocs = new ArrayList(particles);
        step++;
    }

    public int getStep() {
        return step;
    }

    public List<Location> getLocs() {
        return newLocs;
    }
    public void reset(){
        particles.clear();
        int x = startPoint;
        int y = 0;
        int z = 0;
        if (size >= 2) {
            y = startPoint;
        }
        if (size >= 3) {
            z = startPoint;
        }
        Location location = new Location(x, y, z);
        this.step = 0;
        for (int i = 0; i < numberOfParticles; i++) {
            particles.add((Location) location.clone());
        }
        newLocs= new ArrayList(particles);
        
    }

    private Location getMove(Location loc) {
        int moveNum = ThreadLocalRandom.current().nextInt(0, moves.size());
        return moves.get(moveNum);
    }
    
    public void setMoves(int num){
        if(num == 0){
            moves.clear();
            moves.addAll(rookMove);
        } else if(num == 1){
            moves.clear();
            moves.addAll(bishopMove);
        } else if(num == 2){
            moves.clear();
            moves.addAll(kingMove);
        } else if(num == 3){
            moves.clear();
            moves.addAll(knightMove);
        }
        System.out.println(num);
    }

    private void createMoves() {
        rookMove = new ArrayList<>();
        bishopMove = new ArrayList<>();
        knightMove = new ArrayList<>();
        kingMove = new ArrayList<>();

        //rook moves
        rookMove.add(new Location(1, 0, 0));
        rookMove.add(new Location(-1, 0, 0));
        rookMove.add(new Location(0, 1, 0));
        rookMove.add(new Location(0, -1, 0));
        rookMove.add(new Location(0, 0, 1));
        rookMove.add(new Location(0, 0, -1));

        //bishop moves
        bishopMove.add(new Location(1, 1, 0));
        bishopMove.add(new Location(-1, 1, 0));
        bishopMove.add(new Location(1, -1, 0));
        bishopMove.add(new Location(-1, 1, 0));

        bishopMove.add(new Location(1, 0, 1));
        bishopMove.add(new Location(-1, 0, 1));
        bishopMove.add(new Location(1, 0, -1));
        bishopMove.add(new Location(-1, 0, -1));

        bishopMove.add(new Location(0, 1, 1));
        bishopMove.add(new Location(0, -1, 1));
        bishopMove.add(new Location(0, 1, -1));
        bishopMove.add(new Location(0, -1, -1));
        
        kingMove.add(new Location(1,0,0));
        kingMove.add(new Location(1,1,0));
        kingMove.add(new Location(0,1,0));
        kingMove.add(new Location(-1,1,0));
        kingMove.add(new Location(-1,0,0));
        kingMove.add(new Location(-1,-1,0));
        kingMove.add(new Location(0,-1,0));
        kingMove.add(new Location(1,-1,0));
        
        kingMove.add(new Location(0,0,1));
        kingMove.add(new Location(1,0,1));
        kingMove.add(new Location(1,1,1));
        kingMove.add(new Location(0,1,1));
        kingMove.add(new Location(-1,1,1));
        kingMove.add(new Location(-1,0,1));
        kingMove.add(new Location(-1,-1,1));
        kingMove.add(new Location(0,-1,1));
        kingMove.add(new Location(1,-1,1));
        
        kingMove.add(new Location(0,0,-1));
        kingMove.add(new Location(1,0,-1));
        kingMove.add(new Location(1,1,-1));
        kingMove.add(new Location(0,1,-1));
        kingMove.add(new Location(-1,1,-1));
        kingMove.add(new Location(-1,0,-1));
        kingMove.add(new Location(-1,-1,-1));
        kingMove.add(new Location(0,-1,-1));
        kingMove.add(new Location(1,-1,-1));
        
        knightMove.add(new Location(2,1,0));
        knightMove.add(new Location(1,2,0));
        knightMove.add(new Location(2,-1,0));
        knightMove.add(new Location(1,-2,0));
        
        knightMove.add(new Location(-2,1,0));
        knightMove.add(new Location(-1,2,0));
        knightMove.add(new Location(-2,-1,0));
        knightMove.add(new Location(-1,-2,0));
        
        
        knightMove.add(new Location(2,0,1));
        knightMove.add(new Location(1,0,2));
        knightMove.add(new Location(2,0,-1));
        knightMove.add(new Location(1,0,-2));
        
        knightMove.add(new Location(-2,0,1));
        knightMove.add(new Location(-1,0,2));
        knightMove.add(new Location(-2,0,-1));
        knightMove.add(new Location(-1,0,-2));
    }

}
