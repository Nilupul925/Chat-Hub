package com.chat_hub.chat_hub.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    // This field is used to send messages to clients connected to the WebSocket server.
    private final SimpMessagingTemplate messagingTemplate;

    public WebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping ("/message") // This annotation maps incoming messages to this method. When a message is sent to the "/app/message" destination, this method will be invoked.
    public void handleMessage(Message message) {
        System.out.println("Received message user: " + message.getUser() + ": " + message.getMessage());

        messagingTemplate.convertAndSend("/topic/messages", message); // This line sends the received message to all clients subscribed to the "/topic/messages" destination.
        System.out.println("Sent message to /topic/messages: " + message.getUser() + ": " + message.getMessage());
    }
}
