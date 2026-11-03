package com.chat_hub.chat_hub.client;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.chat_hub.chat_hub.model.Message;

public class StompSessionHandler extends StompSessionHandlerAdapter {
    private String userName;
    private MessageListener messageListener;

    public StompSessionHandler(MessageListener messageListener, String userName) {
        this.messageListener = messageListener;
        this.userName = userName;
    }

    @Override
    // This method is called when the STOMP session is successfully connected. It
    // can be used to perform any necessary setup or initialization after the
    // connection is established.
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Connected to WebSocket server as user: " + userName);

        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class; // This line specifies that the payload of the incoming messages will be of type
                                      // Message.
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof Message) {
                        
                        Message message = (Message) payload;
                        messageListener.onMessageReceived(message);
                        System.out.println("Received message: " + message.getUser() + ": " + message.getMessage());
                    } else {
                        System.out.println("Received unexpected payload type: " + payload.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Client Subscribe to /topic/messages");

        session.subscribe("/topic/activeUsers", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return new ArrayList<String>().getClass(); // This line specifies that the payload of the incoming messages will be of type
                                        // ArrayList.
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof ArrayList) {
                        @SuppressWarnings("unchecked")
                        ArrayList<String> activeUsers = (ArrayList<String>) payload;
                        messageListener.onActiveUsersUpdated(activeUsers);
                        System.out.println("Received active users update: " + activeUsers);
                    } else {
                        System.out.println("Received unexpected payload type: " + payload.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Subscribed to /topic/activeUsers");

        session.send("/app/connect", userName); // This line sends a message to the "/app/connect" destination on the WebSocket server, indicating that the user has connected.
        session.send("/app/request-activeUsers", ""); // This line sends a message to the "/app/request-activeUsers" destination on the WebSocket server, requesting the list of active users.
    }

    @Override
    // This method is called when an exception occurs during the STOMP session. It
    // can be used to handle errors that may arise during the communication with the
    // WebSocket server.
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }

}
