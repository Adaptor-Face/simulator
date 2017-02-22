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

    private static final String DEATH_STARVATION = "Starvation";
    private static final String DEATH_AGE = "Old Age";
    private static final String DEATH_EATEN = "Eaten";
    private static final HashMap<String, Integer> SEAL_DEATH;

    static {
        SEAL_DEATH = new HashMap<String, Integer>();
        SEAL_DEATH.put(DEATH_AGE, 0);
        SEAL_DEATH.put(DEATH_EATEN, 0);
        SEAL_DEATH.put(DEATH_STARVATION, 0);
    }
    private static final HashMap<String, Integer> POLAR_BEAR_DEATH = new HashMap<>(SEAL_DEATH);
    private static final HashMap<Class, HashMap<String, Integer>> MAP;

    static {
        MAP = new HashMap<>();
        MAP.put(PolarBear.class, POLAR_BEAR_DEATH);
        MAP.put(Seal.class, SEAL_DEATH);
    }

    public static void addToDeathToll(Class animalClass, String deathCause) {
        MAP.get(animalClass).put(deathCause, MAP.get(animalClass).get(deathCause) + 1);
    }

    public static String getStatistics() {
        String returnString = "";
        for (Class animalClass : MAP.keySet()) {
            for (String deathCause : MAP.get(animalClass).keySet()) {
                returnString += "" + MAP.get(animalClass).get(deathCause) + " " + animalClass.getName() + "s died to " + getCauseAsText(deathCause) + ".\n";
            }
        }
        return returnString;
    }

    private static String getCauseAsText(String cause) {
        if (cause.equals(DEATH_AGE)) {
            return "old age";
        } else if (cause.equals(DEATH_STARVATION)) {
            return "starvation";
        } else if (cause.equals(DEATH_EATEN)) {
            return "being eaten";
        }
        return "no valid death cause";
    }

}
