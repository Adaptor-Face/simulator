/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public class Shore extends Landscape{
    
    public Shore (){
        this(SHORE_FOOD_DENSITY);
    }
    public Shore (double value){
        setFoodDensity(value);
        setType(SHORE);
    }
}
