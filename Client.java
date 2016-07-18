
/**
 *
 * @author ASIFUL ISLAM
 */


import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
    
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String serverIP;
    private Socket connection;
    
    
    //constructor
    public Client(String host){
        super("Instant Massenger Client");
        serverIP=host;
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
        add(new JScrollPane(chatWindow),BorderLayout.CENTER);
        setSize(600,300);
        setVisible(true);
        
    }
    
    //connect to server
    public void startRunning(){
        
        try {
            connectToServer();
            setupStreams();
            whileChatting();
            
        } catch (EOFException eof) {
            showMessage("\n FRIEND terminated connection");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            closeConnection();
        }
        
    }
    
    private void connectToServer() throws IOException{
        
        showMessage("Attempting for connection ... \n");
        connection=new Socket(InetAddress.getByName(serverIP),6789);
        showMessage("Connected to:"+connection.getInetAddress().getHostName());
        
    }
    
    
    //set up streams to send and receive data
    private void setupStreams() throws IOException{
        
        output =new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        
        input=new ObjectInputStream(connection.getInputStream());
        showMessage("\n Stream is redy for communication");
        
    }
    
    
    private void whileChatting() throws IOException{
        //sendMessage(" You are now connected ");
        //String message=new String();
        sendMessage(" You are now connected to Friend\n");
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
    
    
    //close the streams and sockets
    private void closeConnection(){
        showMessage("\n Closing Connection ....\n");
        ableToType(false);
        
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    
    //send messges to server
    private void sendMessage(String message){
        try {
            output.writeObject("FRIEND - "+message);
            output.flush();
            showMessage("\nFRIEND - "+message);
            
        } catch (IOException ioe) {
            chatWindow.append("\n ERROR: Check your connection");
        }
    }
    
    
    private void showMessage(final String text){
        
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        chatWindow.append(text);
                    }
                }
        );
    }
    
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
