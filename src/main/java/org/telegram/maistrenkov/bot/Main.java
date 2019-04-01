package org.telegram.maistrenkov.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("Running...");
        try {
            ApiContextInitializer.init();
            TelegramLongPollingBot bot;
            if (System.getProperty("https.proxyHost") != null && System.getProperty("https.proxyPort") != null) {
                if (System.getProperty("https.proxyUser") != null && System.getProperty("https.proxyPassword") != null)
                    Authenticator.setDefault(new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(System.getProperty("https.proxyUser"), 
                                                              System.getProperty("https.proxyPassword").toCharArray());
                        }
                    });
                DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);          
                botOptions.setProxyHost(System.getProperty("https.proxyHost"));
                botOptions.setProxyPort(Integer.valueOf(System.getProperty("https.proxyPort")));
                botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
                bot = new Bot(botOptions);
            } else 
                bot = new Bot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    static class Bot extends TelegramLongPollingBot {
        public Bot() {
            
        }
    
        public Bot(DefaultBotOptions options) {
            super(options);
        }
    
        public void onUpdateReceived(Update update) {
            Message msg = update.getMessage();
            if (msg != null && msg.hasText()) {
                
                System.out.println("Message [" + msg.getMessageId() + "] " + msg.getText() + " from " + msg.getFrom().getFirstName() + " [" + msg.getChatId() + "]");
    
                SendMessage sendMsg = new SendMessage();
                sendMsg.enableMarkdown(true);
                sendMsg.setChatId(msg.getChatId());
                sendMsg.setReplyToMessageId(msg.getMessageId());
                sendMsg.setText("I got it");
                try {
                    execute(sendMsg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    
        public String getBotUsername() { return System.getProperty("bot.username"); }
        public String getBotToken() { return System.getProperty("bot.token"); }
    }
    
}
