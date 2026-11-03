package com.chat_hub.chat_hub.client;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                String userName = "TestUser"; // Replace with actual user name input
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
