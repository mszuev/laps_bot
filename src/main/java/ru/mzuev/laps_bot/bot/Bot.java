package ru.mzuev.laps_bot.bot;

import com.profesorfalken.jpowershell.PowerShell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.laps_bot.config.BotConfig;
import ru.mzuev.laps_bot.service.SendBotMessage;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    private String hostName;
    private String userName;
    private String answer;
    private BotConfig config;
    private SendBotMessage sendBotMessage;
    private List<String> operators;


    public Bot(BotConfig config) {
        this.config = config;
        this.sendBotMessage = new SendBotMessage(this);
        operators = config.getOperators();
    }


    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            userName = update.getMessage().getChat().getUserName();
            if (update.getMessage().getText().equals("/start")) {
                sendBotMessage.sendMessage(update, "Привет");
                log.info(userName + " pressed START");
            } else {
                if (operators.contains(userName)){
                    hostName = update.getMessage().getText();
                    log.info(userName + " requested pass hostname: " + hostName);
                    String command = "Get-ADComputer -Identity " + hostName + " -Properties * | Select-Object ms-Mcs-AdmPwd";
                    answer = PowerShell.executeSingleCommand(command).getCommandOutput();

                    if (answer.length() > 30 && answer.length() < 50) {
                        sendBotMessage.sendMessage(update, answer.substring(31));
                    } else {
                        sendBotMessage.sendMessage(update, "Not found or password is empty in AD");
                    }
                } else {
                    sendBotMessage.sendMessage(update, "No access");
                    log.warn(userName + " trying to get a pass");
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
}