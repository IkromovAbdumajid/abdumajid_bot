package com.company;

import controller.*;
import dto.CodeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import servise.FileInfoService;

public class MyTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTelegramBot.class);
    private final WelcomeController welcomeController;
    private final TodoController todoController;
    private final WeatherController weatherController;
    private final CurrencyControler currencyControler;
    private final Translater translater;
    private final FileInfoService fileInfoService;

    public MyTelegramBot() {
        this.fileInfoService = new FileInfoService();

        this.weatherController = new WeatherController();

        this.welcomeController = new WelcomeController();

        this.currencyControler = new CurrencyControler();
        this.translater = new Translater();

        this.todoController = new TodoController();

    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        try {
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                User user = callbackQuery.getFrom();
                String data = callbackQuery.getData();
                message = callbackQuery.getMessage();
                LOGGER.info("messageId: " + message.getMessageId() + "  User_Name  " + user.getFirstName() + " User_username  " + user.getUserName() + "  message: " + data);
                if (data != null) {
                    if (data.equals("menu") || data.startsWith("/setting")) {
                        this.sendMsg(this.welcomeController.handle(data, message.getChatId(), message.getMessageId(), user));
                    }
                    if (data.startsWith("/todo")) {
                        this.sendMsg(this.todoController.handle(data, message.getChatId(), message.getMessageId()));
                    }
                    if (data.startsWith("/weather")) {
                        sendMsg(weatherController.start(update.getCallbackQuery()));
                    }
                    if (data.startsWith("/currency")) {
                        this.sendMsg(currencyControler.handle(data, message.getChatId(), message.getMessageId()));
                    }
                    if (data.startsWith("/translater")) {
                        this.sendMsg(translater.handle(data, message.getChatId(), message.getMessageId()));
                    }
                }
            } else if (message != null) {
                String text = message.getText();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                Integer messageId = message.getMessageId();
                User user = message.getFrom();
                LOGGER.info("messageId: " + messageId + "  User_Name  " + user.getUserName() + " User_username  " + user.getUserName() + "  message: " + text);
                if (text != null) {
                    if (text.equals("/start") || text.equals("/help") || text.equals("/setting") ) {
                        this.sendMsg(this.welcomeController.handle(text, message.getChatId(), messageId, user));
                    }
                    else if (this.todoController.getTodoItemStep().containsKey(message.getChatId()) || text.startsWith("/todo_")) {
                        this.sendMsg(this.todoController.handle(text, message.getChatId(), message.getMessageId()));
                    }
                    else if (this.translater.getTodoItemStep().containsKey(message.getChatId())) {
                        this.sendMsg(this.translater.handle(text, message.getChatId(), message.getMessageId()));
                    }
                    else {
                        sendMessage.setText("Mavjud Emas");
                        this.sendMsg(sendMessage);
                    }
                } else {
                    this.sendMsg(this.fileInfoService.getFileInfo(message));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMsg(SendMessage sendMessage) {

        try {

            execute(sendMessage);

        } catch (TelegramApiException e) {

            e.printStackTrace();

        }

    }

    public void sendMsg(CodeMessage codeMessage) {
        try {

                switch (codeMessage.getType()) {

                    case MESSAGE:
                        execute(codeMessage.getSendMessage());
                        break;
                    case EDIT:
                        execute(codeMessage.getEditMessageText());
                        break;
                    case MESSAGE_VIDEO:
                        execute(codeMessage.getSendMessage());
                        execute(codeMessage.getSendVideo());
                        break;
                    default:
                        break;
                }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "@ikromov_bot";
    }

    @Override
    public String getBotToken() {
        return "5096607759:AAHtLA60WaeZgke_HzkmAPEREVSsdC0qyUg";
    }

}
