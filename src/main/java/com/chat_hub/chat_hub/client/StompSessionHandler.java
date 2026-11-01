package com.chat_hub.chat_hub.client;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class StompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    // This method is called when the STOMP session is successfully connected. It can be used to perform any necessary setup or initialization after the connection is established.
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        
    }

    @Override 
    // This method is called when an exception occurs during the STOMP session. It can be used to handle errors that may arise during the communication with the WebSocket server.
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }

}
