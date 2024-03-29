package game;

import game.controller.GameServer;
import game.controller.XMLParser;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Launch the server application
 */
public class LaunchServer {

    /**
     * Server main class
     * load settings and open the listening connections
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        loadConfigurationSettings();
        setServerHostname();

        Scanner scanner = new Scanner(System.in);
        GameServer server = null;
        boolean retry;
        do{
            try{
                server = GameServer.get();
                server.connect(5000);
                retry = false;
            }catch(BindException e)
            {
                System.out.println("There is a server instance already running, please close it and retry.");
                System.out.println("Do you want to retry?[Y/N]");
                String choice = scanner.nextLine();
                retry = !choice.equalsIgnoreCase("N");
            }
        }while(retry);

        if(server != null)
            try {
                server.run();
            } finally {
                server.close();
            }
    }

    /**
     * Load the configuration settings
     */
    private static void loadConfigurationSettings() {

        File configFile = new File("config.xml");
        if(!configFile.exists())
            XMLParser.writeDefaultConfigFile();
        XMLParser.readConfigFile();
    }

    /**
     * Set correctly the rmi hostname property
     */
    private static void setServerHostname() {

        String localIP = getCorrectLocalIP();
        if(localIP == null)
        {
            Scanner scanner = new Scanner(System.in);
            InetAddress inetAddress;
            try {
                inetAddress = InetAddress.getLocalHost();
                System.out.println("Java host address: " + inetAddress.getHostAddress());
                System.out.println("Unable to verify the correct local ip address, insert Y if it is correct or otherwise insert the correct local ip:");
                localIP = scanner.nextLine();
                if(localIP.equalsIgnoreCase("Y"))
                    localIP = inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                System.out.println("Unable to find the local ip address, please provide it");
                localIP = scanner.nextLine();
            }


        }
        System.setProperty("java.rmi.server.hostname",localIP);
        System.out.println("Exposed address: " + localIP);
    }

    /**
     * Return che correct local ip address
     * It is necessary because the rmi system property java.rmi.server.hostname it's automatically set to InetAddress.getLocalHost().getHostAddress() which can be an incorrect address in same cases (eg: in presence of Wireshark or Virtualbox it seems to take the address of their interfaces, which are not the ones used to communicate in the lan)
     * @return the correct ip address, null in case of error
     */
    private static String getCorrectLocalIP()
    {
        String ip;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
            if(ip.equals("0.0.0.0"))
                return null;
            return ip;
        }catch(Exception e)
        {
            return null;
        }
    }
}
