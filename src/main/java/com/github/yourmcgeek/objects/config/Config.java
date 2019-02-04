package com.github.yourmcgeek.objects.config;

public class Config {
    private String prefix = "/";
    private String botOwnerId = "changeme";
    private String token = "changeme";
    private String supportId = "changeme";
    private String supportCategoryId = "changeme";
    private long guildID = 0L;
    private long logChannelID = 0L;
    private int colorRed = 0;
    private int colorBlue = 0;
    private int colorGreen = 0;

    public Config() {}

    public Config(String prefix, String token, String botOwnerId, String supportId, String supportCategoryId
    , long guildID, long logChannelID, int colorRed, int colorBlue, int colorGreen) {
        this.prefix = prefix;
        this.token = token;
        this.botOwnerId = botOwnerId;
        this.supportId = supportId;
        this.supportCategoryId = supportCategoryId;
        this.guildID = guildID;
        this.logChannelID = logChannelID;
        this.colorBlue = colorBlue;
        this.colorGreen = colorGreen;
        this.colorRed = colorRed;
    }

    @Override
    public String toString() {
        return "Config{" +
                "prefix='" + prefix + '\'' +
                ", botOwnerId=" + botOwnerId + + '\'' +
                ", token='" + token + '\'' +
                ", supportId='" + supportId + '\'' +
                ", guildId='" + guildID + '\'' +
                ", logChannelId='" + logChannelID + '\'' +
                ", colorRed='" + colorRed + '\'' +
                ", colorBlue='" + colorBlue + '\'' +
                ", colorGreen='" + colorGreen + '\'' +
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

    public String getSupportId() {
        return supportId;
    }

    public String getSupportCategoryId() {
        return supportCategoryId;
    }

    public long getGuildID() {
        return guildID;
    }

    public long getLogChannelID() {
        return logChannelID;
    }

    public int getColorRed() {
        return colorRed;
    }

    public int getColorBlue() {
        return colorBlue;
    }

    public int getColorGreen() {
        return colorGreen;
    }
}