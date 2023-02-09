/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.chatbroadcast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexm
 */
public class Server {
    private ServerSocket serverSocket;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
            server.iniciarServer();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        
        public Server(ServerSocket serverSocket){
            this.serverSocket=serverSocket;
        }
        
        public void iniciarServer(){
            try{
                while(!serverSocket.isClosed()){

                    Socket socket = serverSocket.accept();
                    System.out.println("Nuevo cliente conectado");
                    GestorClientes gestorClientes= new GestorClientes(socket);

                    Thread hilo = new Thread(gestorClientes);
                    hilo.start();
                }
            }catch(IOException e){
            
            }
        }
        
        public void cerrarServerSocket(){
            try{
                if(serverSocket!=null){
                    serverSocket.close();
                }
            }catch(IOException e){
            }
        }
        
        

        
    
    
}
