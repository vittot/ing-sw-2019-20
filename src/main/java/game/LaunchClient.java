package game;

import game.controller.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class LaunchClient {

    private static View clientView;

    public static void main(String[] args) throws IOException {

        String interfaceChoice = interfaceSelection();
        if(interfaceChoice.equals("GUI")) {
            clientView = new ClientGuiWrapper();
            //clientView = ClientGUIView.getInstance();
        }else {
            clientView = new ClientTextView();
        }

        clientView.chooseConnection();


    }

    /**
     * Start the connection
     * @param connectionChoice "SOCKET" (default) or "RMI"
     * @param serverIP
     */
    public static void startConnection(String connectionChoice, String serverIP)
    {
        Client client = null; //useless assignment, but required for line 53
        boolean connected;
        if(connectionChoice.equals("RMI")) {
            try {
                setRMIHostname();
                client = new RMIClient(serverIP);
                connected = client.init();
            } catch (RemoteException e) {
                connected = false;
            }
    }
        else {
            client = new SocketClient(serverIP,5000);
            connected = client.init();
        }

        if(!connected) {
            clientView.connectionFailed();
            clientView.chooseConnection();
            return;
        }
        ClientController controller = new ClientController(client,clientView);
        controller.run();


    }

    /**
     * Set correctly the rmi hostname property
     */
    private static void setRMIHostname() {

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

    /**
     * Select the interface (GUI or CLI) from command line
     * @return
     */
    private static String interfaceSelection()
    {
        String input;
        Scanner in = new Scanner(System.in);
        /*File file = new File("input.txt");
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        do {
            System.out.println("CLI[C] or GUI[G]?");
            input = in.nextLine();
            input = input.toUpperCase();
            if(input.equals("G"))
                input = "GUI";
            else if(input.equals("C"))
                input = "CLI";
        }while(!input.equals("GUI") && !input.equals("CLI"));
        return input;

    }
}
