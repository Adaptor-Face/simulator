/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Kristoffer
 */
public class DiffusjonSimulator {

    private int numberOfParticles, startPoint, dimentions, step, width;
    private final ArrayList<Location> particles;
    private ArrayList<Location> rookMove;
    private ArrayList<Location> bishopMove;
    private ArrayList<Location> knightMove;
    private ArrayList<Location> kingMove;
    private ArrayList<Location> moves;
    private ArrayList<Location> newLocs;

    public DiffusjonSimulator(int numberOfParticles, int width, int dimentions) {
        createMoves();
        this.numberOfParticles = numberOfParticles;
        this.startPoint = (width / 2);
        this.width = width;
        this.dimentions = dimentions;
        this.particles = new ArrayList<>();
        moves = new ArrayList(rookMove);
        reset();
    }

    public boolean simulateOneStep(boolean decimal) {
        boolean canMove = false;
        for (Location loc : moves) {
            if (loc.getDimentions() <= dimentions) {
                canMove = true;
            }
        }
        if (!decimal) {
            if (canMove) {
                particles.forEach((Location loc) -> {

                    Location move = getMove(loc);
                    while (move.getDimentions() > dimentions) {
                        move = getMove(loc);
                    }
                    loc.changeLocation(move);
                    if ((loc.getX() < 0 || loc.getX() > width - 1) || (loc.getY() < 0 || loc.getY() > width - 1) || (loc.getZ() < 0 || loc.getZ() > width - 1)) {
                        move.invertLocation();
                        loc.changeLocation(move, 2);
                        move.invertLocation();
                    }
                });
                newLocs = new ArrayList<>(particles);
                step++;
            }
        } else {
            particles.clear();
            Set<Location> noDupes = new LinkedHashSet<>(newLocs);
            particles.addAll(noDupes);
            final ArrayList<Location> moves = new ArrayList<>();
            particles.forEach((Location loc) -> {
                this.moves.forEach((Location move) -> {
                    if (move.getDimentions() <= dimentions) {
                        Location newLoc = new Location(loc.getX(), loc.getY(), loc.getZ());
                        newLoc.changeLocation(move);
                        moves.add(newLoc);
                    }
                });
            });
            newLocs = new ArrayList(moves);
        }
        return canMove;
    }

    public int getStep() {
        return step;
    }

    public List<Location> getLocs() {
        return newLocs;
    }

    public void reset() {
        particles.clear();
        int x = startPoint;
        int y = 0;
        int z = 0;
        if (dimentions >= 2) {
            y = startPoint;
        }
        if (dimentions >= 3) {
            z = startPoint;
        }
        Location location = new Location(x, y, z);
        this.step = 0;
        for (int i = 0; i < numberOfParticles; i++) {
            particles.add((Location) location.clone());
        }
        newLocs = new ArrayList<>(particles);

    }

    private Location getMove(Location loc) {
        int moveNum = ThreadLocalRandom.current().nextInt(0, moves.size());
        return moves.get(moveNum);
    }

    public List<Location> getMoves() {
        return moves;
    }

    public void setMoves(int num) {
        switch (num) {
            case 0:
                moves.clear();
                moves.addAll(rookMove);
                break;
            case 1:
                moves.clear();
                moves.addAll(bishopMove);
                break;
            case 2:
                moves.clear();
                moves.addAll(kingMove);
                break;
            case 3:
                moves.clear();
                moves.addAll(knightMove);
                break;
            default:
                break;
        }
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

        kingMove.add(new Location(1, 0, 0));
        kingMove.add(new Location(1, 1, 0));
        kingMove.add(new Location(0, 1, 0));
        kingMove.add(new Location(-1, 1, 0));
        kingMove.add(new Location(-1, 0, 0));
        kingMove.add(new Location(-1, -1, 0));
        kingMove.add(new Location(0, -1, 0));
        kingMove.add(new Location(1, -1, 0));

        kingMove.add(new Location(0, 0, 1));
        kingMove.add(new Location(1, 0, 1));
        kingMove.add(new Location(1, 1, 1));
        kingMove.add(new Location(0, 1, 1));
        kingMove.add(new Location(-1, 1, 1));
        kingMove.add(new Location(-1, 0, 1));
        kingMove.add(new Location(-1, -1, 1));
        kingMove.add(new Location(0, -1, 1));
        kingMove.add(new Location(1, -1, 1));

        kingMove.add(new Location(0, 0, -1));
        kingMove.add(new Location(1, 0, -1));
        kingMove.add(new Location(1, 1, -1));
        kingMove.add(new Location(0, 1, -1));
        kingMove.add(new Location(-1, 1, -1));
        kingMove.add(new Location(-1, 0, -1));
        kingMove.add(new Location(-1, -1, -1));
        kingMove.add(new Location(0, -1, -1));
        kingMove.add(new Location(1, -1, -1));

        knightMove.add(new Location(2, 1, 0));
        knightMove.add(new Location(1, 2, 0));
        knightMove.add(new Location(2, -1, 0));
        knightMove.add(new Location(1, -2, 0));

        knightMove.add(new Location(-2, 1, 0));
        knightMove.add(new Location(-1, 2, 0));
        knightMove.add(new Location(-2, -1, 0));
        knightMove.add(new Location(-1, -2, 0));

        knightMove.add(new Location(2, 0, 1));
        knightMove.add(new Location(1, 0, 2));
        knightMove.add(new Location(2, 0, -1));
        knightMove.add(new Location(1, 0, -2));

        knightMove.add(new Location(-2, 0, 1));
        knightMove.add(new Location(-1, 0, 2));
        knightMove.add(new Location(-2, 0, -1));
        knightMove.add(new Location(-1, 0, -2));
    }

}
