package com.chat_hub.chat_hub.client;

import java.util.ArrayList;

import com.chat_hub.chat_hub.model.Message;

public interface MessageListener {
    void onMessageReceived(Message message);
    void onActiveUsersUpdated(ArrayList<String> activeUsers);
}
