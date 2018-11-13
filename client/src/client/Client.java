/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import rsa.RSAUtil;
import util.io;

/**
 *
 * @author mnm
 */
public class Client {

    public static void main(String[] args) throws Exception {
        runcommand rc = new runcommand();
        io i = new io();
        String server=args[0];
        int port=8888;
        Socket socket=null;
        while(true){
            try{
            socket = new Socket(server,port);
            break;
            }catch(Exception s){
                System.out.println("Server not respond!");
                System.out.println("Wait for 1 minit to server rechable...");
                Thread.sleep(60000);
            }
        }
        try {
             
            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String serverMessage = "";
            RSAUtil ru = new RSAUtil();
            boolean login = false;
            while (true) {
                if (!login) {
                    String cm = "auth#user1,pass1";
                    cm = ru.getEncrypt(cm);
                    outStream.writeUTF(cm);
                    outStream.flush();
                }
                serverMessage = inStream.readUTF();
                serverMessage = ru.getDecrypt(serverMessage);
//                System.out.println("serverMessage" + serverMessage);
                if (serverMessage.startsWith("command")) {
                    String command = serverMessage.substring(serverMessage.indexOf("#")+1);
                    String output = "command#"+rc.runCommand(command);
//                    System.out.println(output);
                    outStream.writeUTF(ru.getEncrypt(output));
                    outStream.flush();
                } else if (serverMessage.startsWith("auth")) {
                    String auth = serverMessage.substring(serverMessage.indexOf("#"));

                    if (auth.contains("Susessful")) {
                        Timer timer = new Timer();
//                        outStream.writeUTF(ru.getEncrypt("stat#asdasdasd"));
//                        outStream.flush();
                        timer.schedule(new Status(outStream), 0, 60000);
                        System.out.println(auth);
                        login = true;
                    } else {
                        System.out.println(auth);
                        break;//System.exit(0);
                    }

                } else if (serverMessage.startsWith("upload")) {
                    System.out.println("Give New File!");
                    String name = serverMessage.substring(serverMessage.indexOf("#") + 1);
                    byte[] bytes = new byte[1600 * 1024];
                    FileOutputStream fos = new FileOutputStream(name);
                    int count;
                    if ((count = inStream.read(bytes)) > 0) {
                        fos.write(bytes, 0, count);
                    }
                    fos.close();
                    System.out.println("File " + name + "  Saved.");
                }

            }
            outStream.close();
            outStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Status extends TimerTask {

    DataOutputStream outStream;
    RSAUtil ru = new RSAUtil();

    public Status(DataOutputStream outStream) {
        this.outStream = outStream;
    }

    public String getstat() {
        stat s = new stat();
        String res = "stat# Time:" + new Date().toString() + "\t" + s.printUsage();
        return res;
    }

    public void run() {
        try {
            String data = getstat();
//            System.out.println("Run stating\t");
            outStream.writeUTF(ru.getEncrypt(data));
            outStream.flush();
        } catch (Exception d) {
            d.printStackTrace();
        }
    }
}

// And From your main() method or any other method

