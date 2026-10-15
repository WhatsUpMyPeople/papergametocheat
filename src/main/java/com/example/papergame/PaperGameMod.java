package com.example.papergame;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaperGameMod implements ClientModInitializer {
    public static final String MOD_ID = "papergame";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing PaperGame Client Mod for Minecraft 26.2");
        ConfigManager.loadConfig();
        KeybindManager.registerKeybinds();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            KeybindManager.handleKeybinds(client);
        });
    }
}
