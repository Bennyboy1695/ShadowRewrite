package com.github.yourmcgeek.objects.util;

public class FormatUtil {

    public static String filterEveryone(String input) {
        return input.replace("@everyone", "@\u0435veryone")
                .replaceAll("@here", "@h\u0435re"); // cyrillic c
    }

}