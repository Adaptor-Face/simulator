/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public class Shallows extends Landscape{
    
    public Shallows (){
        this(0.45);
    }
    public Shallows (double value){
        setFoodDensitiy(value);
        setType(SHALLOWS);
    }
}
