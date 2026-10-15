package com.example.papergame.simulation;

import com.example.papergame.ConfigManager;
import java.util.Random;

public class GameManager {
    private static final Random RANDOM = new Random();
    
    public static int leftDispenserValue = 1;
    public static int rightDispenserValue = 9;

    public static void randomizeDispensers() {
        int val1 = RANDOM.nextInt(9) + 1;
        int val2 = RANDOM.nextInt(9) + 1;
        
        while (val1 == val2) {
            val2 = RANDOM.nextInt(9) + 1;
        }

        // Enforce rule: Right side is always the higher number, taking into account activeSide switch
        int min = Math.min(val1, val2);
        int max = Math.max(val1, val2);

        if (ConfigManager.data.activeSide.equals("A")) {
            leftDispenserValue = min;
            rightDispenserValue = max;
        } else {
            // Swapped side configuration
            leftDispenserValue = max;
            rightDispenserValue = min;
        }
    }

    public static void playFiftyFifty(boolean playerChoice, double amount) {
        randomizeDispensers();
        if (ConfigManager.data.simulationBalance < amount) return;
        boolean outcome = RANDOM.nextBoolean();
        if (playerChoice == outcome) {
            ConfigManager.data.simulationBalance += amount;
            ConfigManager.data.totalWins++;
        } else {
            ConfigManager.data.simulationBalance -= amount;
            ConfigManager.data.totalLosses++;
        }
        ConfigManager.saveConfig();
    }

    public static boolean playBlackjackSimulation(double betAmount) {
        randomizeDispensers();
        if (ConfigManager.data.simulationBalance < betAmount) return false;
    boolean won = RANDOM.nextBoolean();
        if (won) {
            ConfigManager.data.simulationBalance += betAmount * 1.5;
            ConfigManager.data.totalWins++;
        } else {
            ConfigManager.data.simulationBalance -= betAmount;
            ConfigManager.data.totalLosses++;
        }
        ConfigManager.saveConfig();
        return won;
    }

    public static void resetStats() {
        ConfigManager.data.simulationBalance = 1000.0;
        ConfigManager.data.totalWins = 0;
        ConfigManager.data.totalLosses = 0;
        randomizeDispensers();
        ConfigManager.saveConfig();
    }
}
