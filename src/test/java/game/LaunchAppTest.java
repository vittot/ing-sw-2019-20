package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LaunchAppTest {

    private Thread server;
    private Thread c1;
    private Thread c2;
    private Thread c3;


    @BeforeEach
    public void before(){
        server = new Thread(() -> {
            try {
                LaunchServer.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        c1 = new Thread(() -> {
            try {
                LaunchClient.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        c2 = new Thread(() -> {
            try {
                LaunchClient.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        c3 = new Thread(() -> {
            try {
                LaunchClient.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void launchGame() throws IOException, InterruptedException {
        /*server.start();
        c1.start();
        c2.start();
        c3.start();

        server.join();
        c1.join();
        c2.join();
        c3.join();*/
    }

}