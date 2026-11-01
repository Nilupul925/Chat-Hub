package com.chat_hub.chat_hub.client;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                String userName = "TestUser"; // Replace with actual user name input
                ClientGUI clientGUI = new ClientGUI(userName);
                clientGUI.setVisible(true);
            }
        });
    }
}
