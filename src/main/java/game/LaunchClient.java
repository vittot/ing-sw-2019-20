package game;

import game.controller.*;

import java.io.IOException;
import java.util.Scanner;

public class LaunchClient {
    public static void main(String[] args) throws IOException {
        View clientView;
        String interfaceChoice = interfaceSelection();
        if(interfaceChoice.equals("GUI")) {
            //ClientGUIView.main(args);
            clientView = new ClientGuiWrapper();
        }else {
            clientView = new ClientTextView();
        }
        Client client;
        String connectionChoice = clientView.chooseConnection();
        if(connectionChoice.equals("RMI"))
            client = new RMIClient();
        else
            client = new SocketClient("localhost",5000);
        client.init();
        ClientController controller = new ClientController(client,clientView);
        controller.run();

    }

    private static String interfaceSelection()
    {
        String input;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("CLI or GUI?");
            input = in.nextLine();
            input = input.toUpperCase();
        }while(!input.equals("GUI") && !input.equals("CLI"));
        return input;

    }
}
