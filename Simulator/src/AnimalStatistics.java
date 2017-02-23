/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static boolean newFile = true;
    private static File log;

    static {
        DEFAULT_MAP = new HashMap<String, Integer>();
        DEFAULT_MAP.put(STAT_DEATH_AGE, 0);
        DEFAULT_MAP.put(STAT_DEATH_EATEN, 0);
        DEFAULT_MAP.put(STAT_DEATH_STARVATION, 0);
        DEFAULT_MAP.put(STAT_BIRTH, 0);
    }
    private static final HashMap<Class, HashMap<String, Integer>> MAP = new HashMap<>();
    private static HashMap<Class, HashMap<String, Integer>> STEP_MAP = new HashMap<>();

    public static void addToStats(Class animalClass, String event) {
        event = event.toLowerCase();
        if (MAP.get(animalClass) == null) {
            HashMap<String, Integer> animalMap = new HashMap<>(DEFAULT_MAP);
            MAP.put(animalClass, animalMap);
        }
        MAP.get(animalClass).put(event, MAP.get(animalClass).get(event) + 1);
        if (STEP_MAP.get(animalClass) == null) {
            HashMap<String, Integer> animalStepMap = new HashMap<>(DEFAULT_MAP);
            STEP_MAP.put(animalClass, animalStepMap);
        }
        STEP_MAP.get(animalClass).put(event, STEP_MAP.get(animalClass).get(event) + 1);

    }

    public static String getStatistics() {
        String returnString = "";
        for (Class animalClass : MAP.keySet()) {
            for (String event : MAP.get(animalClass).keySet()) {
                if (MAP.get(animalClass).get(event) > 0) {
                    returnString += "" + MAP.get(animalClass).get(event) + " " + animalClass.getName() + "s " + getAction(event) + " " + getCauseAsText(event) + ".\n";
                }
            }
            returnString += "           ";
        }
        return returnString;
    }

    private static String getLogStatistics() {
        String returnString = "";
        for (Class animalClass : STEP_MAP.keySet()) {
            for (String event : STEP_MAP.get(animalClass).keySet()) {
                returnString += "" + STEP_MAP.get(animalClass).get(event) + ", " + animalClass.getName() + ", " + event + ". ";
            }
            returnString += "\n";
        }
        return returnString;

    }

    public static void stepLog(int step) {
        //logToFile(step);

        for (Class animalClass : STEP_MAP.keySet()) {
            for (String event : STEP_MAP.get(animalClass).keySet()) {
                STEP_MAP.get(animalClass).put(event, 0);
            }
        }
    }

    public static void endLog() {
        MAP.clear();
        newFile = true;
    }

    private static void logToFile(int step) {
        if (newFile) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
            Date date = new Date();
            log = new File("C:/SosLogs/Log_" + dateFormat.format(date) + ".txt");
            newFile = false;
        }
        try {
            log.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(AnimalStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileWriter writer = new FileWriter(log.getAbsolutePath(), true);
            PrintWriter print = new PrintWriter(writer);
            print.println("Step: " + step + "   " + getLogStatistics());
            print.close();
        } catch (IOException ex) {
            Logger.getLogger(AnimalStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }

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
