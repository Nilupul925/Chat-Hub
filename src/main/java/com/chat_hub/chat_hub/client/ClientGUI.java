package com.chat_hub.chat_hub.client;

import java.util.concurrent.ExecutionException;

import com.chat_hub.chat_hub.model.Message;

public class ClientGUI {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        StompClient stompClient = new StompClient("TestUser");

        stompClient.sendMessage(new Message("TestUser", "Hello I am connected!"));
        stompClient.disconnectUser("TestUser");
    }
}
