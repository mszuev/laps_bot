package ru.mzuev.laps_bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mzuev.laps_bot.bot.Bot;

@Slf4j
@Service
public class SendBotMessage {

    private final Bot bot;

    public SendBotMessage(Bot bot) {
        this.bot = bot;
    }

    public void sendMessage(Update update, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}