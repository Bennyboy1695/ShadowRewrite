package com.github.yourmcgeek.objects.config;

public class Config {
    private String prefix = "";
    private String botOwnerId = "";
    private String token = "";
    private int color = 0;

    public Config() {
    }

    public Config(String prefix, int color, String token, String botOwnerId) {
    }

    @Override
    public String toString() {
        return "Config{" +
                "prefix='" + prefix + '\'' +
                ", botOwnerId=" + botOwnerId +
                ", token='" + token+ '\'' +
                ", color='" + color+ '\'' +
                '}';
    }

    public String getPrefix() {
        return prefix;
    }

    public String getBotOwnerId() {
        return botOwnerId;
    }

    public String getToken() {
        return token;
    }

    public int getColor() {
        return color;
    }

}