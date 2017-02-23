/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public class Land extends Landscape {

    public Land() {
        this(LAND_FOOD_DENSITY);
    }

    public Land(double value) {
        setFoodDensity(value);
        setType(LAND);
    }
}
