/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public class Landscape implements LandscapeType{
    private double foodDensitiy = 0;
    private Integer typeId = NONE;
    
    
    public final double getFoodDensitiy() {
        return foodDensitiy;
    }

    public final void setFoodDensitiy(double foodDensitiy) {
        this.foodDensitiy = foodDensitiy;
    }

    public final Integer getType() {
        return typeId;
    }

    public final void setType(Integer typeId) {
        this.typeId = typeId;
    }
    
    
}
