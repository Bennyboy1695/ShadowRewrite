package com.github.yourmcgeek;

import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws Exception {
        final ShadowRewrite bot = new ShadowRewrite();
        final ExecutorService console = Executors.newSingleThreadScheduledExecutor();

        console.submit(() -> {
            final Scanner input = new Scanner(System.in);

            String cmd;
            do {
                cmd = input.nextLine();
                switch (cmd) {
                    default:
                        System.out.println("Invalid command!");
                }
            } while (!cmd.equalsIgnoreCase("exit"));

            bot.shutdown();
           System.exit(0);
        });
    }
}
