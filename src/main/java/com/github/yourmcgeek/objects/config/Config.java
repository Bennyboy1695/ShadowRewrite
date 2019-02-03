package com.github.yourmcgeek.objects.config;

public class Config {
    public Config() {}

    private String prefix = "";
    private String botOwnerId = "";
    private String token = "";
    private int color = 0;
    private String name = "";


    public String getPrefix() {
        return this.prefix;
    }

    public String getBotOwnerId() {
        return this.botOwnerId;
    }

    public String getToken() {
        return this.token;
    }

    public int getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }
}
