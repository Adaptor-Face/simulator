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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kristoffer
 */
public class Stats {

    File log;
    ArrayList<String> runTimeLog, tempLog;

    public Stats() {
        tempLog = new ArrayList<>();
        runTimeLog = new ArrayList<>();
        newLog();
    }

    private void newLog() {
        runTimeLog.clear();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
        Date date = new Date();
        log = new File("C:/SosLogs/stepLog_" + dateFormat.format(date) + ".txt");
    }

    public void log(String event) {
        runTimeLog.add(event);
        runTimeLog.forEach(e->{
        });
    }

    public void nonFinalLog(String eventPart) {
        tempLog.add(eventPart);
    }

    public void endEventLog() {
        StringBuilder str = new StringBuilder();
        tempLog.forEach(e -> {
            str.append(e).append(", ");
        });
        log(str.toString().substring(0, str.toString().length()-2));
        tempLog.clear();
    }

    public void writeToLogFile(boolean write) {
        if (!runTimeLog.isEmpty() && write) {
            FileWriter writer;
            final PrintWriter stepPrint;
            try {
                log.createNewFile();
                writer = new FileWriter(log.getAbsolutePath(), true);
                stepPrint = new PrintWriter(writer);
                runTimeLog.forEach(entry -> {
                    stepPrint.println(entry);
                });
                stepPrint.close();
            } catch (IOException ex) {
                Logger.getLogger(Stats.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                newLog();
            }
        }
    }

}
