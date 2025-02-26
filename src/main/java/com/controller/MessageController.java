package com.controller;

import com.service.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/message")
@CrossOrigin(origins = "http://localhost:5500")
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    private final MessageProducer messageProducer;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        messageProducer.sendMessage(message);
        return ResponseEntity.ok(message);
    }
}
