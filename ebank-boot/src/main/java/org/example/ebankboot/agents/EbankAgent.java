package org.example.ebankboot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EbankAgent {
    private final ChatClient chatClient;

    public EbankAgent(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultSystem("Vous un assistant qui se charge de répondre aux questions de l'utilisateur en fonction du contexte fourni.\n" +
                        "\n" +
                        "Si aucun contexte n'est fourni, réponds avec : **JE NE SAIS PAS**.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(tools)
                .build();
    }

    public String chat(String query, String conversationId) {
        return chatClient.prompt()
                .user(query)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
    public Flux<String> chatStream(String query, String conversationId) {
        return chatClient.prompt()
                .user(query)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

}