/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public interface LandscapeType {
    public static Integer NONE = -1;
    public static Integer LAND = 1;
    public static Integer SHORE = 2;
    public static Integer SHALLOWS = 3;
    public static Integer OCEAN = 4;
    
    public void setType(Integer typeId);
    public Integer getType();
}
