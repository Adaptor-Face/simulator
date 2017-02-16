/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public class Landscape {
    private double foodDensitiy = 0;
    private int typeId = -1;
    
    public static int LAND = 1;
    public static int SHORE = 2;
    public static int SHALLOWS = 3;
    public static int OCEAN = 4;

    public final double getFoodDensitiy() {
        return foodDensitiy;
    }

    public final void setFoodDensitiy(double foodDensitiy) {
        this.foodDensitiy = foodDensitiy;
    }

    public final int getType() {
        return typeId;
    }

    public final void setType(int typeId) {
        this.typeId = typeId;
    }
    
    
}
