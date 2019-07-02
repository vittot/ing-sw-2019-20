package game.controller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class XMLParser {

    /**
     * Read the configuration file
     */
    public static void readConfigFile()
    {
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/config.xml"));
            Element root = document.getRootElement();
            int turnTimerMs = Integer.parseInt(root.getChildText("turnTimerMs"));
            int waitingRoomTimerMs = Integer.parseInt(root.getChildText("waitingRoomTimerMs"));
            int pingIntervalMs = Integer.parseInt(root.getChildText("pingIntervalMs"));
            int pingWaitingTimeMs = Integer.parseInt(root.getChildText("pingWaitingTimeMs"));
            Configuration.TURN_TIMER_MS = turnTimerMs;
            Configuration.WAITING_ROOM_TIMER_MS = waitingRoomTimerMs;
            Configuration.PING_WAITING_TIME_MS = pingWaitingTimeMs;
            Configuration.PING_INTERVAL_MS = pingIntervalMs;

        } catch (Exception e) {
            System.out.println("Error while reading config file, default values loaded");
        }
    }

    /**
     * Write the default configuration file
     */
    public static void writeDefaultConfigFile() {
        File f = new File("config.xml");
        try (PrintWriter pw = new PrintWriter(f)){
            pw.println("<config>");
            pw.println("    <turnTimerMs>300001</turnTimerMs>");
            pw.println("    <waitingRoomTimerMs>10001</waitingRoomTimerMs>");
            pw.println("    <pingIntervalMs>10001</pingIntervalMs>");
            pw.println("    <pingWaitingTimeMs>4001</pingWaitingTimeMs>");
            pw.println("</config>");
        } catch (FileNotFoundException e) {
            System.out.println("Error during default file writing");
        }

    }
}
