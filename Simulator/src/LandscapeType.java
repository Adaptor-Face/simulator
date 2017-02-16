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
    public static String NONE = "NONE";
    public static String LAND = "LAND";
    public static String SHORE = "SHORE";
    public static String SHALLOWS = "SHALLOWS";
    public static String OCEAN = "OCEAN";
    
    public void setType(String typeId);
    public String getType();
}
