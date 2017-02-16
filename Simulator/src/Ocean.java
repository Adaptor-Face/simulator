/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public class Ocean extends Landscape{
    
    public Ocean (){
        this(0.60);
    }
    public Ocean (double value){
        setFoodDensitiy(value);
        setType(OCEAN);
    }
}
