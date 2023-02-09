/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.chatbroadcast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author alexm
 */
public class Cliente {
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nick;
    private String IP;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("introduce tu nick: ");
        String nick = scanner.nextLine();
        Socket socket= new Socket("192.168.1.47", 1234);
        Cliente cliente = new Cliente(socket, nick);
        cliente.esperaMensaje();
        cliente.enviarMensaje();
        
    }
    
    public Cliente(Socket socket, String nick){
        try{
        this.socket=socket;
        this.nick=nick;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        IP = socket.getRemoteSocketAddress().toString();
        IP = IP.split(":")[0].split("/")[1];
    
        }catch(IOException e){
            cierra(socket, bufferedReader, bufferedWriter);
        }
    }
    
    public void enviarMensaje(){
        try{
            bufferedWriter.write(nick);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            
            Scanner scanner=new Scanner(System.in);
            
            while(socket.isConnected()){
                String mensaje=scanner.nextLine();
                bufferedWriter.write(nick+"-"+IP+": "+mensaje);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            cierra(socket, bufferedReader, bufferedWriter);
        }
    }
    
    public void esperaMensaje(){
        new Thread(new Runnable(){

            @Override
            public void run() {
                String mensajeRecibido;
                
                while(socket.isConnected()){
                    try{
                        mensajeRecibido= bufferedReader.readLine();
                        System.out.println(mensajeRecibido);
                    }catch(IOException e){
                        cierra(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
            
    }
    
    public void cierra(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
    
         try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
        
        }
        
    }
}
