package org.example.ebankboot.controllers;

import org.example.ebankboot.agents.EbankAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EbankChatBotController {
    @Autowired
    private EbankAgent ebankAgent;


    @GetMapping(value = "/chat",produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "default") String conversationId) {
        return ebankAgent.chat(query, conversationId);
    }
    @GetMapping(value = "/chatStream", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> chatStream(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "default") String conversationId) {
        return ebankAgent.chatStream(query, conversationId);
    }

}
