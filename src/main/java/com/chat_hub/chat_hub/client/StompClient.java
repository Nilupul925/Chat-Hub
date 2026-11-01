package com.chat_hub.chat_hub.client;

import org.springframework.messaging.simp.stomp.StompSession;

import lombok.Data;

@Data 
public class StompClient {
    // This field holds the STOMP session that represents the connection to the WebSocket server.
    private StompSession session;
    private String userName;

}
