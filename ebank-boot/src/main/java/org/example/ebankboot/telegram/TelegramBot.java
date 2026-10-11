package org.example.ebankboot.telegram;

import org.example.ebankboot.agents.EbankAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@ConditionalOnExpression("'${telegram.bot.token:}'.matches('^[0-9]+:.*')")
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final String botToken;
    private final EbankAgent ebankAgent;
    private final TelegramClient telegramClient;

    public TelegramBot(@Value("${telegram.bot.token:}") String botToken, EbankAgent ebankAgent) {
        this.botToken = botToken;
        this.ebankAgent = ebankAgent;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            log.info("Received message from chatId {}: {}", chatId, messageText);

            try {
                // Call EbankAgent AI with chatId as the conversationId for conversational memory
                String response = ebankAgent.chat(messageText, String.valueOf(chatId));

                SendMessage message = SendMessage.builder()
                        .chatId(chatId)
                        .text(response != null ? response : "Désolé, aucune réponse générée.")
                        .build();

                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                log.error("Telegram API error while sending message to chatId {}", chatId, e);
            } catch (Exception e) {
                log.error("Error processing update for chatId {}", chatId, e);
                try {
                    SendMessage errorMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Une erreur est survenue lors du traitement de votre demande.")
                            .build();
                    telegramClient.execute(errorMessage);
                } catch (Exception ex) {
                    log.error("Failed to send error notification to chatId {}", chatId, ex);
                }
            }
        }
    }
}


