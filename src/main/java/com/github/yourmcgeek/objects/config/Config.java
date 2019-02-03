package com.github.yourmcgeek.objects.config;

public class Config {
    public Apis apis;
    public discord discord;

    public static class Apis {
        public String google;
    }

    public static class discord {

        public String prefix;
        public String botOwnerId;
        public String token;
        public int color;
        public String name;
        public Game game;

        public static class Game {
            public String streamUrl;
            public String name;
            public int type;
        }

    }
}
