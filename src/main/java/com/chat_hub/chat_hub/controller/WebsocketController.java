package com.chat_hub.chat_hub.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chat_hub.chat_hub.model.Message;
import com.chat_hub.chat_hub.service.WebsocketSessionManager;

@Controller
public class WebsocketController {
    // This field is used to send messages to clients connected to the WebSocket server.
    private final SimpMessagingTemplate messagingTemplate;
    // This field is used to manage active WebSocket sessions and broadcast active user names to clients.
    private final WebsocketSessionManager sessionManager;

    public WebsocketController(SimpMessagingTemplate messagingTemplate, WebsocketSessionManager sessionManager) {
        this.messagingTemplate = messagingTemplate;
        this.sessionManager = sessionManager;
    }

    @MessageMapping ("/message") // This annotation maps incoming messages to this method. When a message is sent to the "/app/message" destination, this method will be invoked.
    public void handleMessage(Message message) {
        //This for debugging purposes
        System.out.println("Received message user: " + message.getUser() + ": " + message.getMessage());

        messagingTemplate.convertAndSend("/topic/messages", message); // This line sends the received message to all clients subscribed to the "/topic/messages" destination.
        System.out.println("Sent message to /topic/messages: " + message.getUser() + ": " + message.getMessage());
    }

    @MessageMapping ("/connect") // This annotation maps incoming messages to this method. When a message is sent to the "/app/connect" destination, this method will be invoked.
    public void connectuser(String userName) {
        sessionManager.addUserName(userName); // This line adds the user name to the list of active user names.
        sessionManager.broadcastActiveUserNames(); // This line broadcasts the updated list of active user names to all clients subscribed to the "/topic/activeUsers" destination.
        System.out.println(userName + " connected");
    }

    @MessageMapping ("/disconnect") // This annotation maps incoming messages to this method. When a message is sent to the "/app/disconnect" destination, this method will be invoked.
    public void disconnectuser(String userName) {
        sessionManager.removeUserName(userName); // This line removes the user name from the list of active user names.
        sessionManager.broadcastActiveUserNames(); // This line broadcasts the updated list of active user names to all clients subscribed to the "/topic/activeUsers" destination.
        System.out.println(userName + " disconnected");
    }
}
