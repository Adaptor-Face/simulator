/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;

/**
 *
 * @author Kristoffer
 */
public class AnimalStatistics {

    public static final String STAT_DEATH_STARVATION = "starvation";
    public static final String STAT_DEATH_AGE = "age";
    public static final String STAT_DEATH_EATEN = "eaten";
    public static final String STAT_BIRTH = "birth";
    private static final HashMap<String, Integer> DEFAULT_MAP;

    static {
        DEFAULT_MAP = new HashMap<String, Integer>();
        DEFAULT_MAP.put(STAT_DEATH_AGE, 0);
        DEFAULT_MAP.put(STAT_DEATH_EATEN, 0);
        DEFAULT_MAP.put(STAT_DEATH_STARVATION, 0);
        DEFAULT_MAP.put(STAT_BIRTH, 0);
    }
    private static final HashMap<Class, HashMap<String, Integer>> MAP = new HashMap<>();

    public static void addToStats(Class animalClass, String event) {
        event = event.toLowerCase();
        if (MAP.get(animalClass) == null) {
            HashMap<String, Integer> animalMap = new HashMap<>(DEFAULT_MAP);
            MAP.put(animalClass, animalMap);
        }
        MAP.get(animalClass).put(event, MAP.get(animalClass).get(event) + 1);
    }

    public static String getStatistics() {
        String returnString = "";
        for (Class animalClass : MAP.keySet()) {
            for (String event : MAP.get(animalClass).keySet()) {
                if (MAP.get(animalClass).get(event) > 0) {
                    returnString += "" + MAP.get(animalClass).get(event) + " " + animalClass.getName() + "s " + getAction(event) + " " + getCauseAsText(event) + ".\n";
                }
            }
            returnString += "\n";
        }
        return returnString;
    }
    

    private static String getCauseAsText(String stat) {
        if (stat.equals(STAT_DEATH_AGE)) {
            return "old age";
        } else if (stat.equals(STAT_DEATH_STARVATION)) {
            return "starvation";
        } else if (stat.equals(STAT_DEATH_EATEN)) {
            return "being eaten";
        } else if (stat.equals(STAT_BIRTH)) {
            return "";
        }
        return "no valid cause";
    }

    private static String getAction(String event) {
        if (event.equals(STAT_DEATH_AGE)) {
            return "died from";
        } else if (event.equals(STAT_DEATH_STARVATION)) {
            return "died to";
        } else if (event.equals(STAT_DEATH_EATEN)) {
            return "died to";
        } else if (event.equals(STAT_BIRTH)) {
            return "was born";
        }
        return "no valid action";
    }
}
