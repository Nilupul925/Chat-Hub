package com.chat_hub.chat_hub.client;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.chat_hub.chat_hub.model.Message;

public class StompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    // This method is called when the STOMP session is successfully connected. It can be used to perform any necessary setup or initialization after the connection is established.
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class; // This line specifies that the payload of the incoming messages will be of type Message.
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof Message) {
                        Message message = (Message) payload;
                        System.out.println("Received message: " + message.getUser() + ": " + message.getMessage());
                    } else {
                        System.out.println("Received unexpected payload type: " + payload.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override 
    // This method is called when an exception occurs during the STOMP session. It can be used to handle errors that may arise during the communication with the WebSocket server.
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }

}
