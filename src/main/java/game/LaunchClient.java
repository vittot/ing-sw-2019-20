package game;

import game.controller.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
