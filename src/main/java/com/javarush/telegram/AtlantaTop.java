package com.javarush.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class AtlantaTop extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "Bot_botov_bot"; //TODO: добавь имя бота в кавычках
    public static final String TELEGRAM_BOT_TOKEN = "1342_932"; //TODO: добавь токен бота в кавычках
    public static final String OPEN_AI_TOKEN = ""; //TODO: добавь токен ChatGPT в кавычках

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMod = null;
    public AtlantaTop() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        //TODO: основной функционал бота будем писать здесь

        String message = getMessageText();

        if (message.equals("/start")) {
            currentMod = DialogMode.MAIN;
            sendPhotoMessage("/mainayln");
            String text = loadMessage("/main");
            sendTextMessage(text);
            showMainMenu("Начало", "/start",
                    "Автотест", "/avtotest",
                    "Теория", "/javateoria",
                    "Задачки", "/javazadachi");
            return;
        }
        // АВТОТЕСТ
        if (message.equals("/avtotest")) {
            currentMod = DialogMode.AVTOTEST;
            sendPhotoMessage("/avtotest");
            String text = loadMessage("avtotext");
            sendTextMessage(text);
            return;
        }
        if (currentMod == DialogMode.AVTOTEST) {
            String prompt = loadPrompt("avtotestprm");
            Message msg = sendTextMessage("Атлант ищет нужную книгу в библиотеке...*Подожди*");
            String answer = chatGPT.sendMessage(prompt, message);
            updateTextMessage(msg, answer);
            return;
        }
        // ТЕОРИЯ
        if (message.equals("/javateoria")) {
            currentMod = DialogMode.TEORIA;
            sendPhotoMessage("teoria");
            String text = loadMessage("teoria");
            sendTextMessage(text);
            return;
        }
        if (currentMod == DialogMode.TEORIA) {
            String prompt = loadPrompt("teoriajava");
            Message msg = sendTextMessage("Атлант ищет нужную книгу в библиотеке...*ПОДОЖДИ*");
            String answer = chatGPT.sendMessage(prompt, message);
            updateTextMessage(msg, answer);
            return;
        }
        // ЗАДАЧИ
        if (message.equals("/javazadachi")) {
            currentMod = DialogMode.ZADACHI;
            sendPhotoMessage("/zadachi");
            String text = loadMessage("/zadachki");
            sendTextMessage(text);
            sendTextButtonsMessage("*ВЫБЕРИ СВОЮ МОЩЬ*",
                    "Легкий уровень", "data_easy",
                    "Средний уровень", "data_mid",
                    "Сложный уровень", "data_hard");
            return;
        }
        if (currentMod == DialogMode.ZADACHI) {
            String query = getCallbackQueryButtonKey();
            if (query.equals("data_easy")) {
                sendPhotoMessage("/zadachi11");
                sendTextMessage("*попроси ДАВИДА отправить задачу: дай задачу*");
                String prompt = loadPrompt("promptzad1");
                chatGPT.setPrompt(prompt);
                return;
            }
            if (query.equals("data_mid")) {
                sendPhotoMessage("/zadachi22");
                sendTextMessage("*попроси СЦИПИОНА отправить задачу: дай задачу*");
                String prompt = loadPrompt("promptzad2");
                chatGPT.setPrompt(prompt);
                return;
            }
            if (query.equals("data_hard")) {
                sendPhotoMessage("/zadachi33");
                sendTextMessage("*попроси АИДА отправить задачу: дай задачу*");
                String prompt = loadPrompt("promptzad3");
                chatGPT.setPrompt(prompt);
                return;
            }
            String answer = chatGPT.addMessage(message);
            sendTextMessage(answer);
            return;
        }
        // Собеседование
        if (message.equals("/sobes")) {
            currentMod = DialogMode.SOBES;
            sendPhotoMessage("mainayln");
            String text = loadMessage("sobest");
            sendTextMessage(text);
            return;
        }
        if (currentMod == DialogMode.SOBES) {
            String prompt = loadPrompt("sobes");
            Message msg = sendTextMessage("Атлант ищет нужную книгу в библиотеке...*ПОДОЖДИ*");
            String answer = chatGPT.sendMessage(prompt, message);
            updateTextMessage(msg, answer);
            return;
        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new AtlantaTop());
    }
}
