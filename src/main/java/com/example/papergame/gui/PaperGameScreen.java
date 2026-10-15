package com.example.papergame.gui;

import com.example.papergame.ConfigManager;
import com.example.papergame.simulation.GameManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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

        this.addRenderableWidget(Button.builder(Component.literal("Settings"), b -> this.minecraft.gui.setScreen(new PaperGameScreen("Settings")))
                .bounds(centerX - 154, startY, 72, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("PaperGame"), b -> this.minecraft.gui.setScreen(new PaperGameScreen("PaperGame")))
                .bounds(centerX - 78, startY, 72, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("50/50"), b -> this.minecraft.gui.setScreen(new PaperGameScreen("50/50")))
                .bounds(centerX + 2, startY, 72, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("BlackJack"), b -> this.minecraft.gui.setScreen(new PaperGameScreen("BlackJack")))
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
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, delta);
        centeredText(guiGraphics, this.title.getString(), 20, 0xFFFFFFFF);

        String dispenserText = String.format("Dispensers [1-9] -> Left: %d | Right: %d (Active Side: %s)", 
                GameManager.leftDispenserValue,
                GameManager.rightDispenserValue,
                ConfigManager.data.activeSide);
        centeredText(guiGraphics, dispenserText, 65, 0xFFFFAA00);

        if (!feedbackMessage.isEmpty()) {
            centeredText(guiGraphics, feedbackMessage, this.height - 40, 0xFFFF5555);
        }

        if (currentTab.equals("50/50") || currentTab.equals("BlackJack")) {
            String stats = String.format("Simulation Balance: %.2f | Wins: %d | Losses: %d",
                    ConfigManager.data.simulationBalance, ConfigManager.data.totalWins, ConfigManager.data.totalLosses);
            centeredText(guiGraphics, stats, 95, 0xFF55FF55);
        }

    }

    private void centeredText(GuiGraphicsExtractor graphics, String value, int y, int color) {
        graphics.text(this.font, value, (this.width - this.font.width(value)) / 2, y, color, true);
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
