package controller;
import com.google.gson.Gson;
import dto.ObHavoDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import utils.InlineButtonUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class WeatherController {
    public SendMessage start(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(getWeather());
        sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(

                InlineButtonUtil.collection(

                        InlineButtonUtil.row(InlineButtonUtil.button("Back to Menu", "menu",":back:"))

                )));

        sendMessage.setChatId(userId.toString());
        return sendMessage;
    }
    private String getWeather() {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Tashkent&APPID=99359aa7c82d931dc451734dea583180");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream stream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            Gson gson = new Gson();
            ObHavoDTO obHavoDTO = gson.fromJson(stringBuilder.toString(), ObHavoDTO.class);
            return "\n\uD83C\uDF06" + " \uD835\uDE4E\uD835\uDE5D\uD835\uDE56\uD835\uDE5D\uD835\uDE56\uD835\uDE67: " + obHavoDTO.getName() +
                    "\n\uD83C\uDF21" + " \uD835\uDE4F\uD835\uDE5A\uD835\uDE62\uD835\uDE65\uD835\uDE5A\uD835\uDE67\uD835\uDE56\uD835\uDE69\uD835\uDE6A\uD835\uDE67\uD835\uDE56: " + obHavoDTO.getMain().getTemp() + " Kelvin" +
                    "\n\uD83D\uDCA8" + " \uD835\uDE4E\uD835\uDE5D\uD835\uDE56\uD835\uDE62\uD835\uDE64\uD835\uDE61 \uD835\uDE69\uD835\uDE5A\uD835\uDE6F\uD835\uDE61\uD835\uDE5E\uD835\uDE5C\uD835\uDE5E: " + obHavoDTO.getWind().getSpeed() + " m/s" +
                    checkHowMain(obHavoDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Molodes";
    }
    public String checkHowMain(ObHavoDTO obHavoDTO) {
        return switch (obHavoDTO.getWeather()[0].getMain()) {
            case "Snow" -> "\n❄️" + " \uD835\uDE4A\uD835\uDE57 \uD835\uDE5D\uD835\uDE56\uD835\uDE6B\uD835\uDE64 \uD835\uDE68\uD835\uDE5D\uD835\uDE56\uD835\uDE67\uD835\uDE64\uD835\uDE5E\uD835\uDE69\uD835\uDE5E: " + obHavoDTO.getWeather()[0].getMain();
            case "Cloud" -> "\n☁️" + " \uD835\uDE4A\uD835\uDE57 \uD835\uDE5D\uD835\uDE56\uD835\uDE6B\uD835\uDE64 \uD835\uDE68\uD835\uDE5D\uD835\uDE56\uD835\uDE67\uD835\uDE64\uD835\uDE5E\uD835\uDE69\uD835\uDE5E: " + obHavoDTO.getWeather()[0].getMain();
            case "Fog" -> "\n\uD83C\uDF2B️" + " \uD835\uDE4A\uD835\uDE57 \uD835\uDE5D\uD835\uDE56\uD835\uDE6B\uD835\uDE64 \uD835\uDE68\uD835\uDE5D\uD835\uDE56\uD835\uDE67\uD835\uDE64\uD835\uDE5E\uD835\uDE69\uD835\uDE5E: " + obHavoDTO.getWeather()[0].getMain();
            default -> "\n☀️" + " \uD835\uDE4A\uD835\uDE57 \uD835\uDE5D\uD835\uDE56\uD835\uDE6B\uD835\uDE64 \uD835\uDE68\uD835\uDE5D\uD835\uDE56\uD835\uDE67\uD835\uDE64\uD835\uDE5E\uD835\uDE69\uD835\uDE5E: " + obHavoDTO.getWeather()[0].getMain();
        };
    }
}
