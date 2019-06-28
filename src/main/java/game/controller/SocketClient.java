package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient implements Client, ServerMessageHandler {
    private final String host;
    private final int port;
    private Socket socket;
    private Thread receiver;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outStream;
    private ExecutorService executor;
    private boolean stop;
    private ServerGameMessageHandler controller;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public boolean init() {
        try{
            //socket = new Socket(host, port);
            if(socket != null && socket.isConnected())
                close();
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 30000);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.stop = false;
            return true;
        }
        catch(IOException e)
        {
            return false;
        }

    }

    @Override
    public void startListening(ClientController handler)
    {

        this.controller = handler;
        receiver = new Thread(

                () -> {
                    ServerMessage sm;
                    do{
                        sm = this.receiveMessage();
                        if(sm!=null)
                        {
                            /*final ServerMessage smR = sm;
                            executor.submit( ()-> smR.handle(this) );*/
                            //System.out.println("RECEIVED " + sm.toString());
                            sm.handle(this);
                        }

                    }while(sm != null);
                    if(!stop)
                    {
                        handler.manageConnectionError();
                    }
                }
        );
        receiver.start();
    }

    /**
     * Send a message to the server
     * @param msg
     */
    public void sendMessage(ClientMessage msg) {
        try{
            outStream.writeObject(msg);
            outStream.reset();
        }catch(IOException e)
        {
            System.out.println("ECCEZIONE IN SEND MESSAGE"); //TODO: call retry connection method
            e.printStackTrace();
        }
    }

    private ServerMessage receiveMessage() {
        try {
            return (ServerMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            return null;
        }
    }

    @Override
    public void handle(ServerGameMessage msg) {
        //System.out.println("HANDLING " + msg);
        executor.submit(()-> msg.handle(controller));
    }

    @Override
    public void handle(PingMessage msg) {
        sendMessage(new PongMessage());
    }

    public void close() {
        try {
            stop = true;
            inputStream.close();
            outStream.close();
            socket.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
