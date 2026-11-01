package com.chat_hub.chat_hub.client;

import javax.swing.JFrame;

public class ClientGUI extends JFrame{
    public ClientGUI(String userName) {
        super("User: " + userName);

        setSize(1218, 685);
        setLocationRelativeTo(null);
    }
}
