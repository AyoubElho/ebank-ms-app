package org.example.ebankboot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import org.springframework.ai.openai.http.okhttp.OpenAiHttpClientBuilderCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EbankBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBootApplication.class, args);
    }

    /**
     * Interceptor to strip "reasoning_content" from outgoing request payloads to Groq/OpenAI compatible APIs.
     * This fixes HTTP 400 when multi-turn chat or ToolCallingAdvisor replays assistant messages containing reasoning.
     */
    @Bean
    public OpenAiHttpClientBuilderCustomizer groqReasoningSanitizerCustomizer(ObjectMapper objectMapper) {
        return builder -> builder.interceptor(chain -> {
            Request request = chain.request();
            if (request.body() != null && request.url().encodedPath().contains("/chat/completions")) {
                try {
                    Buffer buffer = new Buffer();
                    request.body().writeTo(buffer);
                    String bodyString = buffer.readUtf8();

                    JsonNode rootNode = objectMapper.readTree(bodyString);
                    if (rootNode.has("messages") && rootNode.get("messages").isArray()) {
                        ArrayNode messagesNode = (ArrayNode) rootNode.get("messages");
                        boolean modified = false;
                        for (JsonNode messageNode : messagesNode) {
                            if (messageNode instanceof ObjectNode objNode && objNode.has("reasoning_content")) {
                                objNode.remove("reasoning_content");
                                modified = true;
                            }
                        }
                        if (modified) {
                            String newBody = objectMapper.writeValueAsString(rootNode);
                            RequestBody newRequestBody = RequestBody.create(newBody, request.body().contentType());
                            request = request.newBuilder()
                                    .method(request.method(), newRequestBody)
                                    .build();
                        }
                    }
                } catch (Exception e) {
                    // In case of parsing error, proceed with original request
                }
            }
            return chain.proceed(request);
        });
    }

}
