package game;

import game.controller.GameServer;

import java.io.IOException;
import java.net.BindException;
import java.util.Scanner;

public class LaunchServer {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        GameServer server = null;
        boolean retry;
        do{
            try{
            server = new GameServer(5000);
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
}
