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

    int x;
    int y;
    int z;

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
 * @param x the number to add to the x - coordinate
 * @param y the number to add to the y - coordinate
 * @param z the number to add to the z - coordinate 
 */
    public void changeLocation(int x, int y, int z) {
            this.x += x;
            this.y += y;
            this.z += z;
    }
    
    @Override
    public Object clone(){
        return new Location(x,y,z);
    }
}
