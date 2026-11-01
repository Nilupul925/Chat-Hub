package com.chat_hub.chat_hub.client;

import java.util.concurrent.ExecutionException;

public class ClientGUI {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        StompClient stompClient = new StompClient("TestUser");
    }
}
