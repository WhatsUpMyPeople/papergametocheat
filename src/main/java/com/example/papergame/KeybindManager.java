package com.example.papergame;

import com.example.papergame.gui.PaperGameScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class KeybindManager {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            net.minecraft.resources.Identifier.fromNamespaceAndPath("papergame", "controls")
    public static KeyMapping openGuiKey;
    public static KeyMapping toggleRigKey;
    public static KeyMapping switchSideKey;

    public static void registerKeybinds() {
        openGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.papergame.open",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                CATEGORY
        ));

        toggleRigKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.papergame.rig",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                CATEGORY
        ));

        switchSideKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.papergame.switch",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                CATEGORY
        ));
    }

    public static void handleKeybinds(Minecraft client) {
        while (openGuiKey.consumeClick()) {
            if (client.gui.screen() == null) {
                client.gui.setScreen(new PaperGameScreen("Settings"));
            }
        }

        while (toggleRigKey.consumeClick()) {
            ConfigManager.data.rigEnabled = !ConfigManager.data.rigEnabled;
            ConfigManager.saveConfig();
            if (client.player != null) {
                client.player.sendOverlayMessage(
                        net.minecraft.network.chat.Component.literal("PaperGame Rig: " + (ConfigManager.data.rigEnabled ? "ON" : "OFF"))
                );
            }
        }

        while (switchSideKey.consumeClick()) {
            ConfigManager.data.activeSide = ConfigManager.data.activeSide.equals("A") ? "E" : "A";
            ConfigManager.saveConfig();
            if (client.player != null) {
                client.player.sendOverlayMessage(
                        net.minecraft.network.chat.Component.literal("PaperGame Side Switched to: " + ConfigManager.data.activeSide)
                );
            }
        }
    }
}
