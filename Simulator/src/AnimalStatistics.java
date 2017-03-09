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
    private static String logString = "starvation_Seal, birth_Seal, eaten_Seal, age_Seal, starvation_PolarBear, birth_PolarBear, eaten_PolarBear, age_PolarBear, PolarBear, Seal, ";
    private static int step = 0;

    private static final HashMap<String, Integer> DEATH_CAUSE_VALUE;
    private static File sealLog;
    private static File polarBearLog;
    private static File totalLog;
    private static final HashMap<Class, Number> TOTAL_MAP;

    static {
        TOTAL_MAP = new HashMap<>();
        TOTAL_MAP.put(Seal.class, 0);
        TOTAL_MAP.put(PolarBear.class, 0);
    }

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

    static {
        MAP.put(Seal.class, DEFAULT_MAP);
        MAP.put(PolarBear.class, DEFAULT_MAP);
    }
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

    public static void logSingleEvent(Class animalClass, String deathCause, int age, int foodLevel) {
        if (animalClass.equals(Seal.class)) {
            sealEventLog.add(DEATH_CAUSE_VALUE.get(deathCause) + ", " + age + ", " + foodLevel + ", " + step);
        } else if (animalClass.equals(PolarBear.class)) {
            polarBearEventLog.add(DEATH_CAUSE_VALUE.get(deathCause) + ", " + age + ", " + foodLevel + ", " + step);
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

    public static void addToTotal(Object obj) {
        TOTAL_MAP.put(obj.getClass(), TOTAL_MAP.get(obj.getClass()).intValue() + 1);
    }

    private static String getLogStatistics() {
        String returnString = "";
        for (Class animalClass : STEP_MAP.keySet()) {
            for (String event : STEP_MAP.get(animalClass).keySet()) {
                returnString += "" + STEP_MAP.get(animalClass).get(event) + ", ";// + animalClass.getName() + ", " + event + ". ";
            }
        }
        if (STEP_MAP.keySet().size() == 1) {
            for (Class c : STEP_MAP.keySet()) {
                if (c.equals(Seal.class)) {
                    returnString += "0, 0, 0, 0, ";
                } else if (c.equals(PolarBear.class)) {
                    returnString = "0, 0, 0, 0, " + returnString;
                }
            }
        }
        return returnString;

    }

    public static void stepLog(String populationInfo) {
        step++;
        String str[] = populationInfo.replace(":", ",").replace(" ", "").split(",");
        HashMap<String, Number> popMap = new HashMap<>();
        for (int i = 0; i < str.length; i++) {
            if (str[i].matches("[0-9]+")) {
                int number = Integer.parseInt(str[i]);
                popMap.put(str[i - 1], number);
            }
        }
        logToFile(popMap);
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

    private static String getTotal() {
        String rString = "";
        for (Class c : TOTAL_MAP.keySet()) {
            rString += c.getName() + ": " + TOTAL_MAP.get(c) + "   ";
        }
        return rString;
    }

    private static void logToFile(HashMap<String, Number> popMap) {
        if (newFile) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
            Date date = new Date();
            stepLog = new File("C:/SosLogs/stepLog_" + dateFormat.format(date) + ".txt");
            sealLog = new File("C:/SosLogs/sealLog_" + dateFormat.format(date) + ".txt");
            polarBearLog = new File("C:/SosLogs/polarBearLog_" + dateFormat.format(date) + ".txt");
            totalLog = new File("C:/SosLogs/totalLog_" + dateFormat.format(date) + ".txt");
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
            if(totalLog.exists()){
                totalLog.delete();
            }
            FileWriter totalWriter = new FileWriter(totalLog.getAbsolutePath(), true);
            PrintWriter stepPrint = new PrintWriter(stepWriter);
            PrintWriter sealPrint = new PrintWriter(sealWriter);
            PrintWriter polarBearPrint = new PrintWriter(polarBearWriter);
            PrintWriter totalPrint = new PrintWriter(totalWriter);
            totalLog.createNewFile();
            if (newFile) {
                getLogStatistics();
                stepPrint.println(logString);
                sealPrint.println("DeathCause, age, foodLevel, step");
                polarBearPrint.println("DeathCause, age, foodLevel, step");
                newFile = false;
            }
            for (String line : sealEventLog) {
                sealPrint.println(line);
            }
            for (String line : polarBearEventLog) {
                polarBearPrint.println(line);
            }
            String log = getLogStatistics();
            for (String name : popMap.keySet()) {
                log += popMap.get(name) + ", ";
            }
            totalPrint.println(getTotal());
            totalPrint.close();
            stepPrint.println(log);
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
