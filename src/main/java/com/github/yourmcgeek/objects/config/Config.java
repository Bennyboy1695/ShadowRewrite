package com.github.yourmcgeek.objects.config;

import java.util.ArrayList;
import java.util.Arrays;

public class Config {
    private String prefix = "/";
    private String botOwnerId = "changeme";
    private String token = "changeme";
    private String supportId = "changeme";
    private String supportCategoryId = "changeme";
    private String guildID = "";
    private String logChannelID = "";
    private int colorRed = 0;
    private int colorBlue = 0;
    private int colorGreen = 0;
    private ArrayList<String> blacklistFiles = new ArrayList<>();
    private String linkingURL = "Change this to the proper \"Linking Accounts\" wiki page url";
    private String crashURL = "Change this to the proper \"Crash Reports\" wiki page url";
    private String tqURL = "Change this to the proper \"Claiming\" wiki page url";
    private String claimURL = "Change this to the proper \"Tiqaulity Tile Entity Claiming\" wiki page url";
    private String clURL = "Change this to the proper \"Chunk Loading\" wiki page url";
    private String restartURL = "Change this to the proper \"Restart and Wipe\" wiki page url";
    private String wikiURL = "Change this to the proper \"Wiki\" direct url";
    private String crateURL = "Change this to the proper \"Crates\" wiki page url";
    private String relocateURL = "Change this to the proper \"Relocation\" wiki page url";

    public Config() {}

    public Config(String prefix, String token, String botOwnerId, String supportId, String supportCategoryId
    , String guildID, String logChannelID, int colorRed, int colorBlue, int colorGreen, ArrayList<String> blacklistFiles,
                  String linkingURL, String crashURL, String tqURL, String claimURL, String clURL,
                  String restartURL, String wikiURL, String crateURL, String relocateURL) {
        this.prefix = prefix;
        this.token = token;
        this.botOwnerId = botOwnerId;
        this.supportId = supportId;
        this.supportCategoryId = supportCategoryId;
        this.guildID = guildID;
        this.logChannelID = logChannelID;
        this.colorRed = colorRed;
        this.colorGreen = colorGreen;
        this.colorBlue = colorBlue;
        this.blacklistFiles = blacklistFiles;
        this.linkingURL = linkingURL;
        this.crashURL = crashURL;
        this.clURL = clURL;
        this.tqURL = tqURL;
        this.claimURL = claimURL;
        this.restartURL = restartURL;
        this.wikiURL = wikiURL;
        this.crateURL = crateURL;
        this.relocateURL = relocateURL;
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
                ", colorGreen='" + colorGreen + '\'' +
                ", colorBlue='" + colorBlue + '\'' +
                ", crashURL='" + crashURL + '\'' +
                ", linkingURL='" + linkingURL + '\'' +
                ", tqURL='" + tqURL + '\'' +
                ", claimURL='" + claimURL + '\'' +
                ", chunkURL='" + clURL + '\'' +
                ", restartURL='" + restartURL + '\'' +
                ", wikiURL='" + wikiURL + '\'' +
                ", crateURL='" + crateURL + '\'' +
                ", relocateURL='" + relocateURL + '\'' +
                ", blacklistFiles: ['" + Arrays.toString(blacklistFiles.toArray()) + "']"
                + "}";
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

    public String getGuildID() {
        return guildID;
    }

    public String getLogChannelID() {
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

    public ArrayList<String> getBlacklistFiles() {
        return blacklistFiles;
    }


    public String getCrashURL() {
        return crashURL;
    }

    public String getLinkingURL() {
        return linkingURL;
    }

    public String getTQURL() {
        return tqURL;
    }

    public String getClaimURL() {
        return claimURL;
    }

    public String getCLURL() {
        return clURL;
    }

    public String getRestartURL() {
        return restartURL;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public String getCrateURL() {
        return crateURL;
    }

    public String getRelocateURL() {
        return relocateURL;
    }
}