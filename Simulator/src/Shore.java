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
        this(1);
    }
    public Shore (double value){
        setFoodDensitiy(value);
        setType(SHORE);
    }
}
