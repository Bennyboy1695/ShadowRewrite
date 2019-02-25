package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ShutdownListener extends ListenerAdapter {

    private ShadowRewrite main;

    public ShutdownListener(ShadowRewrite main) {
        this.main = main;
    }

    public void onShutdown(ShutdownEvent event) {
        main.saveMessages();
    }
}