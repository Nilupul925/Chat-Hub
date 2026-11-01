package com.chat_hub.chat_hub.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.chat_hub.chat_hub.model.Message;

import lombok.Data;

@Data 
public class StompClient {
    // This field holds the STOMP session that represents the connection to the WebSocket server.
    private StompSession session;
    private String userName;

    public StompClient(String userName) throws InterruptedException, ExecutionException {
        this.userName = userName;

        List<Transport> transports = new ArrayList<>();
        // This line adds a WebSocket transport to the list of transports that the SockJS client will use to connect to the server.
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockJsClient = new SockJsClient(transports);
        // This line creates a new SockJS client that will use the specified transports to connect to the WebSocket server.
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        // This line sets the message converter for the STOMP client to use Jackson for JSON serialization and deserialization.
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        StompSessionHandler sessionHandler = new StompSessionHandler(userName);
        // This line connects the STOMP client to the WebSocket server at the specified URL and
        String url = "ws://localhost:8080/ws";
        session = stompClient.connectAsync(url, sessionHandler).get();
    }

    public void sendMessage(Message message) {
        try {
            // This line sends the specified message to the "/app/message" destination on the WebSocket server.
            session.send("/app/message", message);
            System.out.println("Sent message: " + message.getUser() + ": " + message.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
