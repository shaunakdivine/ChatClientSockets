package assignment5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Users implements Runnable{
    private final int serverPort;

    private ArrayList<MyClient> users = new ArrayList<>();

    public Users(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<MyClient> getUsers() {
        return users;
    }

    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(serverPort);
            while (true) {
                Socket s = ss.accept();
                MyClient client = new MyClient(this, s);
                Thread clientThread = new Thread(client);
                users.add(client);
                clientThread.start();
            }
        }
        catch (IOException e){
                e.printStackTrace();
        }
    }
}



