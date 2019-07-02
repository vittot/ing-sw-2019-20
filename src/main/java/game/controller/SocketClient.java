package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketClient extends Client implements ServerMessageHandler {
    private final String host;
    private final int port;
    private Socket socket;
    private Thread receiver;
    private Thread processer;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outStream;
    private ClientController controller;
    private LinkedBlockingQueue<ServerGameMessage> gameMessages;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.gameMessages = new LinkedBlockingQueue<>();
        this.nPingLost = 0;
    }

    public boolean init() {
        try{
            if(socket != null && socket.isConnected())
                close();
            socket = new Socket(host, port);
            //socket.connect(new InetSocketAddress(host, port), 30000);
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

        this.disconnectionExecutor = Executors.newSingleThreadScheduledExecutor();
        waitNextPing();

        processer = new Thread(
                () -> {
                    do {
                        ServerGameMessage msg = null;
                        try {
                            msg = gameMessages.poll(1, TimeUnit.MINUTES);
                            if(msg != null) //it's null if timeout is elapsed
                                msg.handle(controller);
                        } catch (InterruptedException e) {
                            return;
                        }

                    }while(!stop);
                }
        );
        processer.setName("PROCESSER THREAD");
        processer.start();

        receiver = new Thread(

                () -> {
                    ServerMessage sm;
                    do{
                        sm = this.receiveMessage();
                        if(sm!=null)
                        {
                            final ServerMessage smR = sm;
                            smR.handle(this);
                        }

                    }while(sm!=null && !stop);
                    if(!stop)
                    {
                        System.out.println("ERROR received : " + sm);
                        handler.manageConnectionError();
                    }
                }
        );
        receiver.setName("RECEIVER THREAD");
        receiver.start();


    }

    /**
     * Send a message to the server
     * It must be synchronized to avoid interferences between game messages and ping messages
     * @param msg
     */
    public synchronized void sendMessage(ClientMessage msg) {
        if(stop)
            return;
        try{
            outStream.writeObject(msg);
            outStream.flush();
            outStream.reset();
        }catch(IOException e)
        {
            System.out.println("ECCEZIONE IN SEND MESSAGE"); //TODO: call retry connection method
            e.printStackTrace();

            controller.manageConnectionError();

        }
    }

    private ServerMessage receiveMessage() {
        try {
            return (ServerMessage) inputStream.readObject();
        } catch (IOException e){
            System.out.println("IOEXCEPTION READING");
            e.printStackTrace();
            return null;
        }catch(ClassNotFoundException e)
        {
            System.out.println("CLASS NOT FOUND EXCEPTION READING");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void handle(ServerGameMessage msg) {
        try {
            gameMessages.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(PingMessage msg) {
        sendMessage(new PongMessage());
        //System.out.println("PING FROM SERVER");
        nPingLost = 0;
        disconnectionExecutor.shutdownNow();
        waitNextPing();
    }

    public void close() {
        try {
            stop = true;
            stopWaitingPing();
            inputStream.close();
            outStream.close();
            socket.close();
            receiver.interrupt();
            processer.interrupt();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
