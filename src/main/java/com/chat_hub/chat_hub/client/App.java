package com.chat_hub.chat_hub.client;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                JOptionPane.showMessageDialog(null, "Welcome to Chat Hub!","Chat Hub", JOptionPane.INFORMATION_MESSAGE);
                String userName = JOptionPane.showInputDialog(null, "Enter your username (Max: 20 characters):", "Chat Hub", JOptionPane.QUESTION_MESSAGE);
                if(userName == null || userName.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                if(userName.length() > 20){
                    JOptionPane.showMessageDialog(null, "Username cannot exceed 20 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                ClientGUI clientGUI = null;
                try {
                    clientGUI = new ClientGUI(userName);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                clientGUI.setVisible(true);
            }
        });
    }
}
