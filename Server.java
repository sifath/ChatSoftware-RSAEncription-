/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASIFUL ISLAM
 */

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Server extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    /*The java.net.ServerSocket class is used by server
    applications to obtain a port and listen for client requests */
    private ServerSocket server;
    //Sockets provide the communication mechanism between two computers using TCP
    private Socket connection;
    
    
    //constructor
    public Server(){
        super("Instant Messenger Server");
        userText=new JTextField();
        userText.setEditable(false);
        
        userText.addActionListener(
                
        new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendMessage(event.getActionCommand());
                userText.setText("");
            }
        }
        );
        
        add(userText,BorderLayout.NORTH);
        chatWindow=new JTextArea();
        add(new JScrollPane(chatWindow));
        //add(chatWindow);
        setSize(600,300);
        setVisible(true);
        
    }
    
    
    //Set up and run server
    public void startRunning(){
        try {
            server=new ServerSocket(6789,100);
            while (true) {                
                try {
                    waitForConnection();
                    setupStream();
                    whileChatting();
                } catch (EOFException eOFException) {
                    showMessage("\n SIFATH ended the Connection ! ");
                }finally{
                    closeConnection();
                }
            }
            
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
    
    //wait for connection an then display th info
    private  void waitForConnection() throws IOException{
        showMessage("Waitng for someone connection ....\n");
        connection=server.accept();
        showMessage(" Now Connected to "+connection.getInetAddress().getHostName());
    }
    
    
    //get stream to send and receive data
    private void setupStream() throws IOException{
        output=new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input=new ObjectInputStream(connection.getInputStream());
        showMessage("\n Stream are now set up! \n");
    }
    
    //during the conversation
    private void whileChatting() throws IOException{
        sendMessage(" You are now connected to SIFATH\n");
        String message=new String();
        ableToType(true);
        
        do{
            try {
                
                message =(String) input.readObject();
                showMessage("\n"+message);
                
            } catch (ClassNotFoundException classNotFoundException) {
                
                showMessage("\n Mysterious data send by the user! ");
                
            }
        }while(!message.equals("FRIEND - END"));
        
    }
    
    //close stream and socket after completing chattinf
    private void closeConnection(){
        showMessage("\n Closing Connection ....\n");
        //ableToType(false);
        
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    //send a message to client
    private void sendMessage(String message){
        try {
            output.writeObject("SIFATH - "+message);
            output.flush();
            showMessage("\nSIFATH - "+message);
            
        } catch (IOException ioe) {
            chatWindow.append("\n ERROR: Check your connection");
        }
    }
    
    //updates chatWindow
    private void showMessage(final String text){
        
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        chatWindow.append(text);
                    }
                }
        );
    }
    
    
    
    //Give the permission to type or not
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        userText.setEditable(tof);
                    }
                }
        );
    }
    
}
