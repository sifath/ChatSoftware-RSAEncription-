/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASIFUL ISLAM
 */
import javax.swing.JFrame;

public class clientTest {
    
    public static void main(String[] args) {
        
    Client ob=new Client("127.0.0.1");
    ob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ob.startRunning();
    }
}