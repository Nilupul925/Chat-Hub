package com.chat_hub.chat_hub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration // This annotation indicates that this class contains configuration settings for the application.
@EnableWebSocketMessageBroker // This annotation enables WebSocket message handling, backed by a message broker.
public class WebsocketConfig implements org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer {

    // This method registers the STOMP endpoints that the clients will use to connect to the WebSocket server.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    // This method configures the message broker that will be used to route messages from one client to another.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

}
