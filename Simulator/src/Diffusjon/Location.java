/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

/**
 *
 * @author Kristoffer
 */
public class Location {

    private int x;
    private int y;
    private int z;

    public Location(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(int num) {
        this.x = num;
        this.y = num;
        this.z = num;
    }

    @Override
    public String toString() {
        return "" + x + "," + y + "," + z;
    }

    @Override
    public boolean equals(Object obj) {
        Location other;
        if (obj instanceof Location) {
            other = (Location) obj;
        } else {
            return false;
        }
        if (x != other.x) {
            return false;
        } else if (y != other.y) {
            return false;
        } else if (z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (x << 16) + (y << 8) + z;
    }

    /**
     * changes the location with the given values
     *
     * @param x the number to add to the x - coordinate
     * @param y the number to add to the y - coordinate
     * @param z the number to add to the z - coordinate
     */
    public void changeLocation(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void changeLocation(Location loc) {
        this.x += loc.x;
        this.y += loc.y;
        this.z += loc.z;
    }

    public void changeLocation(Location loc, int times) {
        for (int i = 0; i < times; i++) {
            this.x += loc.x;
            this.y += loc.y;
            this.z += loc.z;
        }
    }

    @Override
    public Object clone() {
        return new Location(x, y, z);
    }

    public int getDimentions() {
        if (x != 0 && y == 0 && z == 0) {
            return 1;
        } else if (y != 0 && z == 0) {
            return 2;
        } else if (z != 0) {
            return 3;
        }
        return Integer.MAX_VALUE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void invertLocation() {
        x = -x;
        y = -y;
        z = -z;
    }

}
