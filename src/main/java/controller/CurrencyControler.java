package controller;

import com.google.gson.Gson;
import dto.CodeMessage;
import dto.CurrencyDto;
import enums.CodeMessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import utils.InlineButtonUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class CurrencyControler {
    public CodeMessage handle(String text, Long chatId, Integer messageId) {

        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        EditMessageText editMessageText = new EditMessageText();
        if (text.startsWith("/currency")) {

            String[] text1 = text.split("/");
            switch (text1[2]) {
                case "menu" -> {
                    editMessageText.setText("<b><i>Currnecy Menu</i></b>");
                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setMessageId(messageId);
                    editMessageText.setParseMode("HTML");
                    editMessageText.setReplyMarkup(menuKeyboard());
                    codeMessage.setType(CodeMessageType.EDIT);
                    codeMessage.setEditMessageText(editMessageText);
                    return codeMessage;
                }
                case "today", "yesterday" -> check(codeMessage, editMessageText, chatId, messageId);
            }
            }
            return codeMessage;
    }

    private void check(CodeMessage codeMessage,EditMessageText editMessageText,Long chatId, Integer messageId){
            try {
                editMessageText.setChatId(String.valueOf(chatId));

                editMessageText.setMessageId(messageId);
                URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/USD/now/");
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                Gson gson = new Gson();

                CurrencyDto[] userArray = gson.fromJson(String.valueOf(stringBuilder), CurrencyDto[].class);
                for (CurrencyDto currensyDTO : userArray) {
                    editMessageText.setText("id: " + currensyDTO.getId()
                            + "\nCode: " + currensyDTO.getCode()
                            + "\nCcy: " + currensyDTO.getCcy()
                            + "\nCcyNm_RU: " + currensyDTO.getCcyNm_RU()
                            + "\nCcyNm_UZ: " + currensyDTO.getCcyNm_UZ()
                            + "\nCcyNm_UZC: " + currensyDTO.getCcyNm_UZC()
                            + "\nCcyNm_EN: " + currensyDTO.getCcyNm_EN()
                            + "\nNominal: " + currensyDTO.getNominal()
                            + "\nRate: " + currensyDTO.getRate()
                            + "\nDiff: " + currensyDTO.getDiff()
                            + "\nDate: " + currensyDTO.getDate());
                }
                editMessageText.setReplyMarkup(InlineButtonUtil.keyboard(

                        InlineButtonUtil.collection(

                                InlineButtonUtil.row(InlineButtonUtil.button("Back to the Menu", "menu",":back:"))

                        )));


                codeMessage.setEditMessageText(editMessageText);

                codeMessage.setType(CodeMessageType.EDIT);
            } catch (Exception e) {
                e.printStackTrace();

        }
    }

    private InlineKeyboardMarkup menuKeyboard() {
        InlineKeyboardButton button1 = InlineButtonUtil.button("\uD835\uDE3E\uD835\uDE6A\uD835\uDE67\uD835\uDE68 \uD835\uDE6B\uD835\uDE56\uD835\uDE61\uD835\uDE6A\uD835\uDE69\uD835\uDE5A \uD835\uDE5E\uD835\uDE63 \uD835\uDE69\uD835\uDE64\uD835\uDE59\uD835\uDE56\uD835\uDE6E", "/currency/today", "\uD83D\uDCB2");
        InlineKeyboardButton button2 = InlineButtonUtil.button("\uD835\uDE3E\uD835\uDE6A\uD835\uDE67\uD835\uDE68 \uD835\uDE6B\uD835\uDE56\uD835\uDE61\uD835\uDE6A\uD835\uDE69\uD835\uDE5A \uD835\uDE5E\uD835\uDE63 \uD835\uDE69\uD835\uDE64\uD835\uDE59\uD835\uDE56\uD835\uDE6E", "/currency/yesterday", "\uD83D\uDDD2️");
        InlineKeyboardButton button3 = InlineButtonUtil.button("Back To Menu", "menu", ":back:");

        List<List<InlineKeyboardButton>> rowList = InlineButtonUtil.collection(
                InlineButtonUtil.row(button1, button2), InlineButtonUtil.row(button3));

        return InlineButtonUtil.keyboard(rowList);
    }
}
