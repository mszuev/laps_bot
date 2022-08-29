package ru.mzuev.laps_bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.operator}")
    private String operator;

    public List<String> getOperators(){
        return new ArrayList<>(Arrays.asList(operator.split(",")));
    }
}