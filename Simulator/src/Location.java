
/**
 * Represent a location in a rectangular grid.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Location {

    // Row and column positions.
    private int row;
    private int col;

    /**
     * Represent a row and column.
     *
     * @param row The row.
     * @param col The column.
     */
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Location(String location) {
        String str[] = location.replace(" ", "").split(",");
        this.row = Integer.parseInt(str[0]);
        this.col = Integer.parseInt(str[1]);
    }

    /**
     * Implement content equality.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        } else {
            return false;
        }
    }
    
    public Integer distanceBetween(Location loc){
        Integer distX = Math.abs(loc.col - this.col); 
        Integer distY = Math.abs(loc.row - this.row);
        if(distY<distX){
            return distY;
        }
        return distX;
    }

    /**
     * Return a string of the form row,column
     *
     * @return A string representation of the location.
     */
    public String toString() {
        return row + "," + col;
    }

    /**
     * Use the top 16 bits for the row value and the bottom for the column.
     * Except for very big grids, this should give a unique hash code for each
     * (row, col) pair.
     *
     * @return A hashcode for the location.
     */
    public int hashCode() {
        return (row << 16) + col;
    }

    /**
     * @return The row.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return The column.
     */
    public int getCol() {
        return col;
    }
}
