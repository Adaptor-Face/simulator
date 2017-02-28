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
import java.util.ArrayList;
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
    private static File stepLog;
    private static String logString = "";
    private static int step = 0;

    private static final HashMap<String, Integer> DEATH_CAUSE_VALUE;
    private static File sealLog;
    private static File polarBearLog;

    static {
        DEATH_CAUSE_VALUE = new HashMap<>();
        DEATH_CAUSE_VALUE.put(STAT_DEATH_AGE, 0);
        DEATH_CAUSE_VALUE.put(STAT_DEATH_STARVATION, 1);
        DEATH_CAUSE_VALUE.put(STAT_DEATH_EATEN, 2);
    }

    static {
        DEFAULT_MAP = new HashMap<>();
        DEFAULT_MAP.put(STAT_DEATH_AGE, 0);
        DEFAULT_MAP.put(STAT_DEATH_EATEN, 0);
        DEFAULT_MAP.put(STAT_DEATH_STARVATION, 0);
        DEFAULT_MAP.put(STAT_BIRTH, 0);
    }
    private static final HashMap<Class, HashMap<String, Integer>> MAP = new HashMap<>();
    private static HashMap<Class, HashMap<String, Integer>> STEP_MAP = new HashMap<>();
    private static ArrayList<String> sealEventLog = new ArrayList<>();
    private static ArrayList<String> polarBearEventLog = new ArrayList<>();

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

    public static void logSingleEvent(Class animalClass, String deathCause, int age) {
        if (animalClass.equals(Seal.class)) {
            sealEventLog.add(DEATH_CAUSE_VALUE.get(deathCause) + ", " + age + ", " + step);
        } else if (animalClass.equals(PolarBear.class)) {
            polarBearEventLog.add(DEATH_CAUSE_VALUE.get(deathCause) + ", " + age + ", " + step);
        }
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

    private static String getLogStatistics() {
        String returnString = "";
        for (Class animalClass : STEP_MAP.keySet()) {
            for (String event : STEP_MAP.get(animalClass).keySet()) {
                returnString += "" + STEP_MAP.get(animalClass).get(event) + ", ";// + animalClass.getName() + ", " + event + ". ";
                logString += event + "_" + animalClass.getName() + ", ";
            }
        }
        return returnString;

    }

    public static void stepLog() {
        step++;
        logToFile();
        for (Class animalClass : STEP_MAP.keySet()) {
            for (String event : STEP_MAP.get(animalClass).keySet()) {
                STEP_MAP.get(animalClass).put(event, 0);
            }
        }
    }

    public static void endLog() {
        MAP.clear();
        newFile = true;
        step = 0;
    }

    private static void logToFile() {
        if (newFile) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
            Date date = new Date();
            stepLog = new File("C:/SosLogs/stepLog_" + dateFormat.format(date) + ".txt");
            sealLog = new File("C:/SosLogs/sealLog_" + dateFormat.format(date) + ".txt");
            polarBearLog = new File("C:/SosLogs/polarBearLog_" + dateFormat.format(date) + ".txt");
            try {
                stepLog.createNewFile();
                sealLog.createNewFile();
                polarBearLog.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(AnimalStatistics.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            FileWriter stepWriter = new FileWriter(stepLog.getAbsolutePath(), true);
            FileWriter sealWriter = new FileWriter(sealLog.getAbsolutePath(), true);
            FileWriter polarBearWriter = new FileWriter(polarBearLog.getAbsolutePath(), true);
            PrintWriter stepPrint = new PrintWriter(stepWriter);
            PrintWriter sealPrint = new PrintWriter(sealWriter);
            PrintWriter polarBearPrint = new PrintWriter(polarBearWriter);
            if (newFile) {
                getLogStatistics();
                stepPrint.println(logString);
                sealPrint.println("DeathCause, age, step");
                polarBearPrint.println("DeathCause, age, step");
                newFile = false;
            }
            for(String line : sealEventLog){
                sealPrint.println(line);
            }
            for(String line : polarBearEventLog){
                polarBearPrint.println(line);
            }
            stepPrint.println(getLogStatistics());
            stepPrint.close();
            sealPrint.close();
            sealEventLog.clear();
            polarBearEventLog.clear();
            polarBearPrint.close();
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
