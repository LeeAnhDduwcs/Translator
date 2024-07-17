package main.translator;

import javazoom.jl.player.Player;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class TextToSpeech {
    public static void setupSpeaker() {
        try {
            String urlStr = "https://translate.google.com/translate_tts?ie=UTF-8&" +
                    "tl=" + "en" +
                    "&client=tw-ob&q=" + URLEncoder.encode("text", StandardCharsets.UTF_8);
            URL url = new URL(urlStr);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestMethod("GET");
            urlc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in setup voice!");
        }
    }
    public static void speak(String text, String lang) {
        try {
            String urlStr = "https://translate.google.com/translate_tts?ie=UTF-8&" +
                    "tl=" + lang +
                    "&client=tw-ob&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8);
            URL url = new URL(urlStr);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestMethod("GET");
            InputStream audio = urlc.getInputStream();
            new Player(audio).play();
            urlc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in getting voice!");
        }
    }
}
