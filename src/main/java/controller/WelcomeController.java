package controller;

import dto.CodeMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import enums.CodeMessageType;
import utils.InlineButtonUtil;

import java.util.List;

public class WelcomeController {
    public CodeMessage handle(String text, Long chatId, Integer messageId, User user) {


        CodeMessage codeMessage = new CodeMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));


        codeMessage.setSendMessage(sendMessage);


        switch (text) {
            case "/start" -> {
                sendMessage.setText("<b><i>Welcome </i></b>" + user.getFirstName());
                sendMessage.setParseMode("HTML");
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(

                        InlineButtonUtil.collection(

                                InlineButtonUtil.row(InlineButtonUtil.button("Go to Menu", "menu", "\uD83D\uDCC4"))


                        )));
                codeMessage.setType(CodeMessageType.MESSAGE);
            }
            case "/help" -> {
                String msg = """
                        <b><i> Yordam oynasi.
                         Siz bu bo'tda qilish kerak bo'lgna islariz jadvalini tuzishingiz mumkin.
                        malumot uchun videoni [ YOUTUBE.com ]https://www.youtube.com/watch?v=kUxN6QfXfWc  ko'ring.
                        Yokiy manabu videoni ko'ring </i></b>""";
                sendMessage.setText(msg);
                sendMessage.setParseMode("HTML");
                sendMessage.disableWebPagePreview();
                SendVideo sendVideo = new SendVideo();
                sendVideo.setVideo(new InputFile("BAACAgIAAxkBAAICnF6Fj1geeSI9o8t3sPsyds-9yBUbAAJvBgACVpUwSAcyTHmoZHr3GAQ"));
                sendVideo.setChatId(String.valueOf(chatId));
                sendVideo.setCaption("<s>Bu video siz uhun juda muxim</s>");
                sendVideo.setParseMode("HTML");
                codeMessage.setSendVideo(sendVideo);
                codeMessage.setSendMessage(sendMessage);
                codeMessage.setType(CodeMessageType.MESSAGE_VIDEO);
            }
            case "/setting" -> {
                sendMessage.setText("""
                        ℹ️ <i>Settings</i>
                        /help-information about bot
                        /start-use bot""");
                sendMessage.setParseMode("HTML");
                codeMessage.setType(CodeMessageType.MESSAGE);
            }
            case "menu" -> {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setText("<b><i>Menu</i></b>");
                editMessageText.setChatId(String.valueOf(chatId));
                editMessageText.setMessageId(messageId);
                editMessageText.setParseMode("HTML");
                editMessageText.setReplyMarkup(menuKeyboard());
                codeMessage.setType(CodeMessageType.EDIT);
                codeMessage.setEditMessageText(editMessageText);
            }
        }

        return codeMessage;

    }
    private InlineKeyboardMarkup menuKeyboard() {
        InlineKeyboardButton button1 = InlineButtonUtil.button("\uD835\uDE46\uD835\uDE5E\uD835\uDE67\uD835\uDE5E\uD835\uDE62/\uD835\uDE3E\uD835\uDE5D\uD835\uDE5E\uD835\uDE66\uD835\uDE5E\uD835\uDE62", "/todo/menu", ":memo:");
        InlineKeyboardButton button2 = InlineButtonUtil.button("\uD835\uDE6C\uD835\uDE5A\uD835\uDE56\uD835\uDE69\uD835\uDE5D\uD835\uDE5A\uD835\uDE67", "/weather", "\uD83C\uDF24️");
        InlineKeyboardButton button3 = InlineButtonUtil.button("\uD835\uDE3E\uD835\uDE6A\uD835\uDE67\uD835\uDE67\uD835\uDE5A\uD835\uDE63\uD835\uDE58\uD835\uDE6E", "/currency/menu", "\uD83D\uDCB2");
        InlineKeyboardButton button4 = InlineButtonUtil.button("\uD835\uDE4F\uD835\uDE67\uD835\uDE56\uD835\uDE63\uD835\uDE68\uD835\uDE61\uD835\uDE56\uD835\uDE69\uD835\uDE5A\uD835\uDE67", "/translater/menu","\uD83D\uDE42");
        InlineKeyboardButton button5 = InlineButtonUtil.button("\uD835\uDE4E\uD835\uDE5A\uD835\uDE69\uD835\uDE69\uD835\uDE5E\uD835\uDE63\uD835\uDE5C", "/setting", "⚙️");
        List<List<InlineKeyboardButton>> rowList = InlineButtonUtil.collection(InlineButtonUtil.row(button1),
                InlineButtonUtil.row(button2), InlineButtonUtil.row(button3), InlineButtonUtil.row(button4), InlineButtonUtil.row(button5));

        return InlineButtonUtil.keyboard(rowList);
    }
}
