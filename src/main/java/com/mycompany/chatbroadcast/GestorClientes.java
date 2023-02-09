/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatbroadcast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author alexm
 */
class GestorClientes implements Runnable{

    public static ArrayList<GestorClientes> gestoresClientes=new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter buffereWriter;
    private String nick;
    private String IP;
    
    public GestorClientes(Socket socket){
        try{
            this.socket=socket;
            buffereWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            nick =bufferedReader.readLine();
            gestoresClientes.add(this);
            IP = socket.getRemoteSocketAddress().toString();
            IP = IP.split(":")[0].split("/")[1];
            broadcast("Servidor: "+ nick+"-"+IP+" ha entrado a la sala");
            
        }catch(IOException e){
            cierra(socket, bufferedReader, buffereWriter);
        }
    }
    
    @Override
    public void run() {
        String mensajeCliente;
        
        while(socket.isConnected()){
            try{
            mensajeCliente= bufferedReader.readLine();
            broadcast(mensajeCliente);
            }catch(IOException e){
                cierra(socket, bufferedReader, buffereWriter);
                break;
            }
        }
    }
    
    public void broadcast(String mensaje){
        for(GestorClientes gestor : gestoresClientes){
            try{
                if(!gestor.nick.equals(nick)){
                    gestor.buffereWriter.write(mensaje);
                    gestor.buffereWriter.newLine();
                    gestor.buffereWriter.flush();
                }
            }catch(IOException e){
                cierra(socket, bufferedReader, buffereWriter);
            }
            
        }
    }
    
    public void eliminaGestor(){
        gestoresClientes.remove(this);
        broadcast("Server: "+nick+"-"+IP+" ha abandonado la sala");
    }
    
    public void cierra(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        eliminaGestor();
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
