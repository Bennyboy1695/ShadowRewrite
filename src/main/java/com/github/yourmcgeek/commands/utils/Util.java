package com.github.yourmcgeek.commands.utils;

import com.github.yourmcgeek.ShadowRewrite;

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

}
