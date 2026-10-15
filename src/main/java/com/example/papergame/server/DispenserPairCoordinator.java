package com.example.papergame.server;

import java.util.Objects;
import java.util.Random;

/**
 * Server-side planning primitive for a pair of dispensers.
 * This is independent of Minecraft and does NOT intercept redstone events.
 * Both inventories must be read and the plan committed atomically by the
 * Minecraft server integration before either dispenser emits an item.
 */
public final class DispenserPairCoordinator {
    private DispenserPairCoordinator() {}

    public record Plan(int leftSlot, int rightSlot, int leftNumber, int rightNumber) {}

    /** Returns null when no strictly ordered pair exists in current inventories. */
    public static Plan plan(int[] leftSlots, int[] rightSlots, boolean rightWins, Random random) {
        Objects.requireNonNull(leftSlots, "leftSlots");
        Objects.requireNonNull(rightSlots, "rightSlots");
        Objects.requireNonNull(random, "random");
        NumberPairSelector.Pair pair = NumberPairSelector.select(leftSlots, rightSlots, !rightWins, random);
        return pair == null ? null : new Plan(pair.aSlot(), pair.eSlot(), pair.aNumber(), pair.eNumber());
    }

    /** Revalidate the selected slots before consuming anything, preventing stale plans. */
    public static boolean stillValid(Plan plan, int[] leftSlots, int[] rightSlots, boolean rightWins) {
        if (plan == null || leftSlots == null || rightSlots == null) return false;
        if (plan.leftSlot() < 0 || plan.rightSlot() < 0 ||
            plan.leftSlot() >= leftSlots.length || plan.rightSlot() >= rightSlots.length) return false;
        int left = leftSlots[plan.leftSlot()];
        int right = rightSlots[plan.rightSlot()];
        return left == plan.leftNumber() && right == plan.rightNumber() &&
            left >= 1 && left <= 9 && right >= 1 && right <= 9 &&
            (rightWins ? right > left : left > right);
    }
}
