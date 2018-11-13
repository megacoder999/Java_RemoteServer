/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import rsa.RSAUtil;
import util.io;

/**
 *
 * @author mnm
 */
public class Server extends Thread {

    /**
     * @param args the command line arguments
     */
    Socket serverClient;
    int clientNo;
    boolean isAlive = false;
    String user;
    String pass;
    io i = new io();
    RSAUtil ru = new RSAUtil();
    List<String> statlist = new ArrayList<String>();

    Server(Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;
        for (int i = 0; i < 10; i++) {
            users.add("user" + i);
            passs.add("pass" + i);
        }
    }
    DataOutputStream outStream;

    public void run() {
        try {
            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            outStream = new DataOutputStream(serverClient.getOutputStream());
            String clientMessage = "", serverMessage = "";
            while (!clientMessage.equals("bye")) {
                clientMessage = inStream.readUTF();
                clientMessage = ru.getDecrypt(clientMessage);
//                System.out.println("client message"+clientMessage);
                if (clientMessage.startsWith("stat")) {
                    statlist.add(clientMessage.substring(clientMessage.indexOf("#") + 1));
//                    System.out.println(statlist.size());
                } else if (clientMessage.startsWith("command")) {
                    System.out.println(clientMessage.substring(clientMessage.indexOf("#") + 1));
                } else if (clientMessage.startsWith("auth")) {
                    clientMessage = clientMessage.substring(clientMessage.indexOf("#") + 1);
                    String u = clientMessage.substring(0, clientMessage.indexOf(","));
                    String p = clientMessage.substring(clientMessage.indexOf(",") + 1);
                    if (authentication(u, p)) {
                        user = u;
                        pass = p;
                        isAlive = true;
//                        System.out.println(u+"\t"+p);
                        outStream.writeUTF(ru.getEncrypt("auth#Susessful Authentication"));
                        outStream.flush();
                    } else {
                        outStream.writeUTF(ru.getEncrypt("auth#Authentication FAiled"));
                        outStream.flush();
                        isAlive = false;
                        break;
                    }
                }

            }
            inStream.close();
            outStream.close();
            serverClient.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
        } finally {
            isAlive = false;
            System.out.println("Client -" + clientNo + " exit!! ");
        }
    }

    public boolean authentication(String u, String pass) {
        for (String s : users) {
            if (s.equals(u)) {
                for (String p : passs) {
                    if (p.equals(pass)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void showStat() {
        for (String stat : statlist) {
            System.out.println("Client ID#" + clientNo + "\t" + stat);
        }
    }

    public void sendFile(String path) {
        try {
            File f = new File(path);
            File myFile = new File(path);
            byte[] mybytearray = new byte[(int) myFile.length()];
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            outStream.writeUTF(ru.getEncrypt("upload#" + f.getName()));
            outStream.flush();
            outStream.write(mybytearray, 0, mybytearray.length);
            outStream.flush();
            System.out.println("File " + f.getName() + " Was Sended...");
        } catch (Exception s) {
            s.printStackTrace();
        }
    }

    public void runCommand(String command) {
        try {

            command = ru.getEncrypt("command#" + command);
            outStream.writeUTF(command);
            outStream.flush();
        } catch (Exception s) {

        }
    }

    List<String> users = new ArrayList<>();
    List<String> passs = new ArrayList<>();

}
