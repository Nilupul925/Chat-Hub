package com.chat_hub.chat_hub.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class WebsocketSessionManager {
    private final ArrayList<String> activeUserNames = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;

    public WebsocketSessionManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addUserName(String userName) {
        activeUserNames.add(userName);
    }

    public void removeUserName(String userName) {
        activeUserNames.remove(userName);
    }

    public void broadcastActiveUserNames() {
        messagingTemplate.convertAndSend("/topic/activeUsers", activeUserNames);
        System.out.println("Broadcasted active users to /topic/activeUsers: " + activeUserNames);
    }
}
