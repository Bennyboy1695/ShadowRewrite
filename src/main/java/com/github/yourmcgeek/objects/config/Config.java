package com.github.yourmcgeek.objects.config;

public class Config {
    private String prefix = "/";
    private String botOwnerId = "changeme";
    private String token = "changeme";
    private String color = "0xRRRGGGBBB";
    private String supportId = "changeme";
    private String supportCategoryId = "changeme";

    public Config() {}

    public Config(String prefix, String color, String token, String botOwnerId, String supportId, String supportCategoryId) {
        this.prefix = prefix;
        this.color = color;
        this.token = token;
        this.botOwnerId = botOwnerId;
        this.supportId = supportId;
        this.supportCategoryId = supportCategoryId;
    }

    @Override
    public String toString() {
        return "Config{" +
                "prefix='" + prefix + '\'' +
                ", botOwnerId=" + botOwnerId + + '\'' +
                ", token='" + token + '\'' +
                ", color='" + color + '\'' +
                ", supportId='" + supportId + '\'' +
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

    public String getColor() {
        return color;
    }

    public String getSupportId() {
        return supportId;
    }

    public String setSupportId(String supportId) {
        return this.supportId = supportId;
    }

    public void setSupportCategoryId(String supportCategoryId) {
        this.supportCategoryId = supportCategoryId;
    }

    public String getSupportCategoryId() {
        return supportCategoryId;
    }
}