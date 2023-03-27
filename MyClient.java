package assignment5;

import java.io.*;
import java.net.*;
import java.util.List;

public class MyClient implements Runnable {

    private final Socket s;
    private final Users user;
    private String username = null;
    private DataOutputStream dout;

    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 6666);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            Thread writeMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try{
                            String msg = br.readLine();
                            dout.writeUTF(msg);
                            dout.flush();
                        } catch (java.io.IOException e){
                            e.getMessage();
                        }
                    }
                }
            });

            Thread getMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            String msg = din.readUTF();
                            System.out.println(msg);
                        } catch (java.io.IOException e){
                            e.getMessage();
                        }
                    }
                }
            });
            getMsg.start();
            writeMsg.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyClient(Users user, Socket s){
                this.s = s;
                this.user = user;
    }

    @Override
    public void run(){
        try {

            String msg = null;
            Boolean sendFlag = false;

            DataInputStream dis = new DataInputStream(s.getInputStream());
            this.dout = new DataOutputStream(s.getOutputStream());
            String welcome = "Server Start\nType username first to direct a message\nUse 'quit' to disconnect\nWhat is your name?";
            dout.writeUTF(welcome);
            dout.flush();
            username = dis.readUTF();
            dout.writeUTF("Hello " + username);
            dout.flush();
            System.out.println(username + " Connected");

            while (true) {
                try {
                    sendFlag = false;
                    List<MyClient> users = user.getUsers();
                    msg = dis.readUTF();
                    if (msg.equalsIgnoreCase("quit")){
                        dout.writeUTF("You have disconnected");
                        s.close();
                        users.remove(user);
                        break;
                    }
                    String[] addressedMsg = msg.split(" ", 2);
                    String name = addressedMsg[0];
                    msg = addressedMsg[1];
                    for (int i = 0; i < users.size(); i++){
                        if (users.get(i).getUsername().equalsIgnoreCase(name)) {
                            users.get(i).send(username + ": " + msg);
                            System.out.println(username + ": " + msg);
                            sendFlag = true;
                        }
                    }
                    if (!sendFlag){
                        dout.writeUTF("User is not online, message not sent");
                        dout.flush();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    dout.writeUTF("Invalid message format!");
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void send(String msg) {
        try {
            dout.writeUTF(msg);
            dout.flush();
        } catch (SocketException e){
            System.out.println("socket closed");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUsername(){
        return username;
    }
}


