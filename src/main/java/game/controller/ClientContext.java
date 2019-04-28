package game.controller;

/**
 * Model portion for the Client
 * It's a singleton for every Client
 */
public class ClientContext {
    private static ClientContext instance;
    //TODO: add model data necessary for the SocketClient

    private ClientContext() {
    }

    public static synchronized ClientContext get() {
        if (instance == null) {
            instance = new ClientContext();
        }

        return instance;
    }
}
