package com.github.yourmcgeek.shadowrewrite.utils;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private ShadowRewrite main;

    public Util(ShadowRewrite main) {
        this.main = main;
    }

    public static long timeToMillis(long year, long month, long week, long day, long hour, long min, long sec) {
        return (year*31536000000L) + (month*2628000000L) + TimeUnit.DAYS.toMillis(week*7) + TimeUnit.DAYS.toMillis(day) + TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(min) + TimeUnit.SECONDS.toMillis(sec);
    }

    public static long stringToMillisConverter(CharSequence text){
        Pattern pattern = Pattern.compile("([0-9]+w)?([0-9]+d)?([0-9]+h)?([0-9]+m)?([0-9]+s)?", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()){
            return timeToMillis(0L,0L,
                    (matcher.group(1) != null ? Long.valueOf(matcher.group(1).replace("w", "")) : 0L),
                    (matcher.group(2) != null ? Long.valueOf(matcher.group(2).replace("d", "")) : 0L),
                    (matcher.group(3) != null ? Long.valueOf(matcher.group(3).replace("h", "")) : 0L),
                    (matcher.group(4) != null ? Long.valueOf(matcher.group(4).replace("m", "")) : 0L),
                    (matcher.group(5) != null ? Long.valueOf(matcher.group(5).replace("s", "")) : 0L));
        }
        return 0L;
    }

    public static JSONObject isValidUsername(String username) {
        int responseCode = 0;
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
            URL obj = new URL(url);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (responseCode == 204 || responseCode == 400)
            return null;
        return null;
    }

}
