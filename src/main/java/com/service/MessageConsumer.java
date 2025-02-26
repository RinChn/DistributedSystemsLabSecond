package com.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {
    @RabbitListener(queues = "filmQueue")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
    }
}

