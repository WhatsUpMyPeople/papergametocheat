package com.example.papergame.gui;

import com.example.papergame.ConfigManager;
import com.example.papergame.simulation.GameManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;

public class PaperGameScreen extends Screen {
    private final String currentTab;
    private EditBox blockAInput;
    private EditBox blockEInput;
    private String feedbackMessage = "";

    public PaperGameScreen(String tab) {
        super(Component.literal("PaperGame - rig settings"));
        this.currentTab = tab;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = 40;

        this.addRenderableWidget(Button.builder(Component.literal("Settings"), b -> this.minecraft.setScreen(new PaperGameScreen("Settings")))
                .bounds(centerX - 154, startY, 72, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("PaperGame"), b -> this.minecraft.setScreen(new PaperGameScreen("PaperGame")))
                .bounds(centerX - 78, startY, 72, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("50/50"), b -> this.minecraft.setScreen(new PaperGameScreen("50/50")))
                .bounds(centerX + 2, startY, 72, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("BlackJack"), b -> this.minecraft.setScreen(new PaperGameScreen("BlackJack")))
                .bounds(centerX + 82, startY, 72, 20).build());

        startY += 35;

        if (currentTab.equals("Settings")) {
            this.addRenderableWidget(Button.builder(Component.literal("Rig: " + (ConfigManager.data.rigEnabled ? "ON" : "OFF")), b -> {
                ConfigManager.data.rigEnabled = !ConfigManager.data.rigEnabled;
                ConfigManager.saveConfig();
                b.setMessage(Component.literal("Rig: " + (ConfigManager.data.rigEnabled ? "ON" : "OFF")));
            }).bounds(centerX - 100, startY, 200, 20).build());

            startY += 30;

            blockAInput = new EditBox(this.font, centerX - 100, startY, 130, 20, Component.literal("Block A"));
            blockAInput.setValue(ConfigManager.data.blockA);
            this.addRenderableWidget(blockAInput);

            this.addRenderableWidget(Button.builder(Component.literal("A From hand"), b -> {
                if (this.minecraft != null && this.minecraft.player != null) {
                    ItemStack held = this.minecraft.player.getMainHandItem();
                    if (held.getItem() instanceof BlockItem blockItem) {
                        String id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(blockItem).toString();
                        blockAInput.setValue(id);
                        ConfigManager.data.blockA = id;
                        ConfigManager.saveConfig();
                        feedbackMessage = "Updated Block A from hand.";
                    } else {
                        feedbackMessage = "Error: Main hand item is not a block!";
                    }
                }
            }).bounds(centerX + 35, startY, 65, 20).build());

            startY += 30;

            blockEInput = new EditBox(this.font, centerX - 100, startY, 130, 20, Component.literal("Block E"));
            blockEInput.setValue(ConfigManager.data.blockE);
            this.addRenderableWidget(blockEInput);

            this.addRenderableWidget(Button.builder(Component.literal("E From hand"), b -> {
                if (this.minecraft != null && this.minecraft.player != null) {
                    ItemStack held = this.minecraft.player.getMainHandItem();
                    if (held.getItem() instanceof BlockItem blockItem) {
                        String id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(blockItem).toString();
                        blockEInput.setValue(id);
                        ConfigManager.data.blockE = id;
                        ConfigManager.saveConfig();
                        feedbackMessage = "Updated Block E from hand.";
                    } else {
                        feedbackMessage = "Error: Main hand item is not a block!";
                    }
                }
            }).bounds(centerX + 35, startY, 65, 20).build());

            startY += 35;

            this.addRenderableWidget(Button.builder(Component.literal("Switch Side: [" + ConfigManager.data.activeSide + "]"), b -> {
                ConfigManager.data.activeSide = ConfigManager.data.activeSide.equals("A") ? "E" : "A";
                ConfigManager.saveConfig();
                b.setMessage(Component.literal("Switch Side: [" + ConfigManager.data.activeSide + "]"));
            }).bounds(centerX - 100, startY, 200, 20).build());

        } else if (currentTab.equals("50/50")) {
            this.addRenderableWidget(Button.builder(Component.literal("Simulate Bet (Win 100)"), b -> {
                GameManager.playFiftyFifty(true, 100);
                feedbackMessage = "Simulation Bet Won! Balance: " + ConfigManager.data.simulationBalance;
            }).bounds(centerX - 100, startY, 200, 20).build());

            startY += 30;
            this.addRenderableWidget(Button.builder(Component.literal("Reset Simulation Stats"), b -> {
                GameManager.resetStats();
                feedbackMessage = "Simulation balance reset to 1000.0";
            }).bounds(centerX - 100, startY, 200, 20).build());

        } else if (currentTab.equals("BlackJack")) {
            this.addRenderableWidget(Button.builder(Component.literal("Play Blackjack Sim Hand"), b -> {
                boolean won = GameManager.playBlackjackSimulation(50);
                feedbackMessage = won ? "Blackjack Sim: Won 50!" : "Blackjack Sim: Lost 50!";
            }).bounds(centerX - 100, startY, 200, 20).build());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        String dispenserText = String.format("Dispensers [1-9] -> Left: %d | Right: %d (Active Side: %s)", 
                GameManager.leftDispenserValue,
                GameManager.rightDispenserValue,
                ConfigManager.data.activeSide);
        guiGraphics.drawCenteredString(this.font, dispenserText, this.width / 2, 65, 0xFFAA00);

        if (!feedbackMessage.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, feedbackMessage, this.width / 2, this.height - 40, 0xFF5555);
        }

        if (currentTab.equals("50/50") || currentTab.equals("BlackJack")) {
            String stats = String.format("Simulation Balance: %.2f | Wins: %d | Losses: %d",
                    ConfigManager.data.simulationBalance, ConfigManager.data.totalWins, ConfigManager.data.totalLosses);
            guiGraphics.drawCenteredString(this.font, stats, this.width / 2, 95, 0x55FF55);
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        if (blockAInput != null) {
            ConfigManager.data.blockA = blockAInput.getValue();
        }
        if (blockEInput != null) {
            ConfigManager.data.blockE = blockEInput.getValue();
        }
        ConfigManager.saveConfig();
        super.removed();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
