package com.antilamer.thingTracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${app.rabbitmq.host}")
    private String rabbitmqHost;

    @Value("${app.rabbitmq.port}")
    private Integer rabbitmqPort;

    @Value("${app.rabbitmq.login}")
    private String rabbitmqLogin;

    @Value("${app.rabbitmq.password}")
    private String rabbitmqPassword;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/user-messages-subscriber");

        // RabbitMQ
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost(rabbitmqHost)
                .setRelayPort(rabbitmqPort)
                .setClientLogin(rabbitmqLogin)
                .setClientPasscode(rabbitmqPassword);
    }

}
