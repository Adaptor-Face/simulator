/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kristoffer
 */
public class Stats {

    File distanceLog, locationLog;
    ArrayList<String> runTimeDistnaceLog, runTimeLocationLog, tempDistanceLog, tempLocationLog;

    public Stats(int number) {
        tempDistanceLog = new ArrayList<>();
        tempLocationLog = new ArrayList<>();
        runTimeDistnaceLog = new ArrayList<>();
        runTimeLocationLog = new ArrayList<>();
        ArrayList<String> xyz = new ArrayList<>(Arrays.asList("x", "y", "z"));
        StringBuilder distanceStr = new StringBuilder();
        StringBuilder locationStr = new StringBuilder();
        for (int i = 0; i < number; i++) {
            distanceStr.append("p").append(i + 1).append(", ");
            for (String e : xyz) {
                locationStr.append("p").append(i + 1).append(e).append(", ");
            }
        }
        newLog();
        runTimeDistnaceLog.add(distanceStr.toString());
        runTimeLocationLog.add(locationStr.toString());
    }

    private void newLog() {
        runTimeDistnaceLog.clear();
        runTimeLocationLog.clear();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
        Date date = new Date();
        distanceLog = new File("C:/SosLogs/distanceLog_" + dateFormat.format(date) + ".txt");
        locationLog = new File("C:/SosLogs/locationLog_" + dateFormat.format(date) + ".txt");
    }

    public Iterator<String> getCurrentDistanceLog() {
        return runTimeDistnaceLog.iterator();
    }

    public void logDistance(String event) {
        runTimeDistnaceLog.add(event);
        runTimeDistnaceLog.forEach(e -> {
        });
    }

    public void logLocation(String event) {
        runTimeLocationLog.add(event);
        runTimeLocationLog.forEach(e -> {
        });
    }

    public void nonFinalDistanceLog(String eventPart) {
        tempDistanceLog.add(eventPart);
    }

    public void nonFinalLocationLog(String eventPart) {
        tempLocationLog.add(eventPart);
    }

    public void endEventDistanceLog() {
        StringBuilder str = new StringBuilder();
        tempDistanceLog.forEach(e -> {
            str.append(e).append(", ");
        });
        logDistance(str.toString().substring(0, str.toString().length() - 2));
        tempDistanceLog.clear();
    }

    public void endEventLocationLog() {
        StringBuilder str = new StringBuilder();
        tempLocationLog.forEach(e -> {
            str.append(e).append(", ");
        });
        logLocation(str.toString().substring(0, str.toString().length() - 2));
        tempLocationLog.clear();
    }

    public void writeToLogFile(boolean write) {
        if (!runTimeDistnaceLog.isEmpty() && write) {
            final FileWriter writerLocation, writerDistance;
            final PrintWriter distancePrint, locationPrint;
            try {
                distanceLog.createNewFile();
                locationLog.createNewFile();
                writerDistance = new FileWriter(distanceLog.getAbsolutePath(), true);
                writerLocation = new FileWriter(locationLog.getAbsolutePath(), true);
                distancePrint = new PrintWriter(writerDistance);
                locationPrint = new PrintWriter(writerLocation);
                runTimeDistnaceLog.forEach(entry -> {
                    distancePrint.println(entry);
                });
                runTimeLocationLog.forEach(entry -> {
                    locationPrint.println(entry);
                });
                distancePrint.close();
                locationPrint.close();
            } catch (IOException ex) {
                Logger.getLogger(Stats.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                newLog();
            }
        }
    }

}
