package controller;

import dto.TranslaterItem;
import enums.TranslaterType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import utils.InlineButtonUtil;
import java.io.*;
import java.util.*;

public class Translater {

    private Map<Long, TranslaterItem> traslaterItemStep = new HashMap<>();

    public SendMessage handle(String text, Long chatId, Integer messageId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        if (text.startsWith("/translater")) {
            String[] text1 = text.split("/");
            switch (text1[2]) {
                case "menu":

                    sendMessage.setText("<b><i>Translater Menu</i></b>");

                    sendMessage.setChatId(String.valueOf(chatId));
                    sendMessage.setParseMode("HTML");
                    sendMessage.setReplyMarkup(menuKeyboard());
                    break;
                case "eng_rus":
                case "rus_eng":
                    sendMessage.setChatId(String.valueOf(chatId));
                    sendMessage.setText("*Send word* ");
                    sendMessage.setParseMode("Markdown");
                    TranslaterItem translaterItem = new TranslaterItem();
                    translaterItem.setId(String.valueOf(messageId));
                    translaterItem.setUserId(chatId);
                    translaterItem.setType(TranslaterType.ENG_RUS);

                    this.traslaterItemStep.put(chatId, translaterItem);
                    break;
                case "uzb_kir":
                case "kir_uzb":
                    sendMessage.setChatId(String.valueOf(chatId));
                    sendMessage.setText("*Send word* ");
                    sendMessage.setParseMode("Markdown");
                    translaterItem = new TranslaterItem();
                    translaterItem.setId(String.valueOf(messageId));
                    translaterItem.setUserId(chatId);
                    translaterItem.setType(TranslaterType.UZB_KIR);
                    this.traslaterItemStep.put(chatId, translaterItem);
                    break;
            }
        } else {
            TranslaterItem todoItem = this.traslaterItemStep.get(chatId);

            sendMessage.setParseMode("Markdown");

            if (todoItem.getType().equals(TranslaterType.ENG_RUS)) {
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setParseMode("HTML");
                String rus = findWordRus(text);
                String eng = findWordEng(text);
                if (rus == null && eng == null) {
                    sendMessage.setText("\uD835\uDDE7\uD835\uDDF5\uD835\uDDF6\uD835\uDE00 \uD835\uDE04\uD835\uDDFC\uD835\uDDFF\uD835\uDDF1 \uD835\uDDEE\uD835\uDDFF\uD835\uDDF2 \uD835\uDDFB\uD835\uDDFC\uD835\uDE01 \uD835\uDE01\uD835\uDDFF\uD835\uDDEE\uD835\uDDFB\uD835\uDE00\uD835\uDDF9\uD835\uDDEE\uD835\uDE01\uD835\uDDF2\uD835\uDDF1: " + text);
                } else if (eng == null) {
                    sendMessage.setText("На русском: " + rus);
                } else if (rus == null) {
                    sendMessage.setText("\uD835\uDE40\uD835\uDE63\uD835\uDE5C\uD835\uDE61\uD835\uDE5E\uD835\uDE68\uD835\uDE5D: " + eng);
                }
            } else if (todoItem.getType().equals(TranslaterType.UZB_KIR)) {
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setParseMode("HTML");
                sendMessage.setText(uzb_kir("\uD835\uDCD0\uD835\uDCF7\uD835\uDCFC\uD835\uDD00\uD835\uDCEE\uD835\uDCFB: "+text));
            }
            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(
                    InlineButtonUtil.collection(
                            InlineButtonUtil.row(InlineButtonUtil.button("\uD835\uDCD1\uD835\uDCEA\uD835\uDCEC\uD835\uDCF4 \uD835\uDCFD\uD835\uDCF8 \uD835\uDCDC\uD835\uDCEE\uD835\uDCF7\uD835\uDCFE", "/translater/menu", ":back:"))
                    )));
        }
        return sendMessage;
    }
    private InlineKeyboardMarkup menuKeyboard() {
        InlineKeyboardButton button3 = InlineButtonUtil.button("Ｅｎｇ－Ｒｕｓ", "/translater/eng_rus", "\uD83D\uDE00️");
        InlineKeyboardButton button4 = InlineButtonUtil.button("Ｒｕｓ－Ｅｎｇ", "/translater/rus_eng", "\uD83D\uDE00️");
        InlineKeyboardButton button5 = InlineButtonUtil.button("Ｋｉｒ－Ｕｚｂ", "/translater/kir_uzb", "\uD83D\uDE00️");
        InlineKeyboardButton button6 = InlineButtonUtil.button("Ｕｚｂ－Ｋｉｒ", "/translater/uzb_kir", "\uD83D\uDE00️");
        InlineKeyboardButton button7 = InlineButtonUtil.button("\uD835\uDCD1\uD835\uDCEA\uD835\uDCEC\uD835\uDCF4 \uD835\uDCE3\uD835\uDCF8 \uD835\uDCDC\uD835\uDCEE\uD835\uDCF7\uD835\uDCFE", "menu", ":back:");

        List<List<InlineKeyboardButton>> rowList = InlineButtonUtil.collection(
                 InlineButtonUtil.row(button3, button4), InlineButtonUtil.row(button5, button6), InlineButtonUtil.row(button7));

        return InlineButtonUtil.keyboard(rowList);
    }

    public String findWordRus(String eng) {
        String s = null;
        FileReader fileReader;
        try {
            fileReader = new FileReader("end_rus.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String uzb = bufferedReader.readLine();
            while (uzb != null) {
                String[] strings = uzb.split("—");
                if(strings.length>1) {
                    String[] strings1 = strings[0].split(" ");
                    if (strings1[0].equals(eng)) {
                        s = strings[1];
                        bufferedReader.close();
                    }
                }
                uzb = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String findWordEng(String rus) {
        String s = null;
        FileReader fileReader;
        try {
            fileReader = new FileReader("end_rus.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String uzb = bufferedReader.readLine();
            while (uzb != null) {
                String[] strings = uzb.split("—");
                if(strings.length>1) {
                    String[] strings1 = strings[1].split(" ");
                    if (strings1[1].equals(rus)) {
                        s = strings[0];
                        bufferedReader.close();
                    }
                }
                uzb = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String uzb_kir(String text){
        String [] array = text.split("");
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ы', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "i", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.toCharArray().length; i++) {
            for (int k = 0; k<abcLat.length-5; k++) {
                if (text.charAt(i) == abcCyr[k]) {
                    builder.append(abcLat[k]);
                }else if(Objects.equals(array[i], abcLat[k])){
                    builder.append(abcCyr[k]);
                }
            }
        }
        return builder.toString();
    }


    public Map<Long, TranslaterItem> getTodoItemStep() {
        return traslaterItemStep;
    }


}
