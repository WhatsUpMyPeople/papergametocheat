package com.example.papergame;

import com.example.papergame.gui.PaperGameScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class KeybindManager {
    public static KeyMapping openGuiKey;
    public static KeyMapping toggleRigKey;
    public static KeyMapping switchSideKey;

    public static void registerKeybinds() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.papergame.open",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.papergame"
        ));

        toggleRigKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.papergame.rig",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.papergame"
        ));

        switchSideKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.papergame.switch",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.papergame"
        ));
    }

    public static void handleKeybinds(Minecraft client) {
        while (openGuiKey.consumeClick()) {
            if (client.screen == null) {
                client.setScreen(new PaperGameScreen("Settings"));
            }
        }

        while (toggleRigKey.consumeClick()) {
            ConfigManager.data.rigEnabled = !ConfigManager.data.rigEnabled;
            ConfigManager.saveConfig();
            if (client.player != null) {
                client.player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("PaperGame Rig: " + (ConfigManager.data.rigEnabled ? "ON" : "OFF")),
                        true
                );
            }
        }

        while (switchSideKey.consumeClick()) {
            ConfigManager.data.activeSide = ConfigManager.data.activeSide.equals("A") ? "E" : "A";
            ConfigManager.saveConfig();
            if (client.player != null) {
                client.player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("PaperGame Side Switched to: " + ConfigManager.data.activeSide),
                        true
                );
            }
        }
    }
}
