package com.example.papergame.server;

import java.util.Random;

/**
 * Selects an ordered pair of existing numbered items without creating items.
 * This class has no Minecraft dependencies and can be tested independently.
 * The caller MUST consume both selected slots on the server in one coordinated
 * operation; calling this alone does not change dispenser behavior.
 */
public final class NumberPairSelector {
    private NumberPairSelector() {}

    public record Pair(int aSlot, int eSlot, int aNumber, int eNumber) {}

    /**
     * Each array entry represents a physical inventory slot; 0 denotes an
     * empty slot or an item without an exact valid custom name from 1 to 9.
     * Every eligible slot pair is equally likely. Returns null if impossible.
     */
    public static Pair select(int[] aSlots, int[] eSlots, boolean aWins, Random random) {
        if (aSlots == null || eSlots == null || random == null)
            throw new IllegalArgumentException("Missing selection input");
        Pair chosen = null;
        int matches = 0;
        for (int a = 0; a < aSlots.length; a++) {
            int av = aSlots[a];
            if (av < 1 || av > 9) continue;
            for (int e = 0; e < eSlots.length; e++) {
                int ev = eSlots[e];
                if (ev < 1 || ev > 9) continue;
                if ((aWins && av > ev) || (!aWins && ev > av)) {
                    matches++;
                    if (random.nextInt(matches) == 0) chosen = new Pair(a, e, av, ev);
                }
            }
        }
        return chosen;
    }
}
