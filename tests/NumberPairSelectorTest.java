import com.example.papergame.server.NumberPairSelector;
import java.util.Random;

public class NumberPairSelectorTest {
    public static void main(String[] args) {
        Random random = new Random(42);
        for (int i = 0; i < 10000; i++) {
            var right = NumberPairSelector.select(new int[]{1,4,7}, new int[]{2,5,9}, false, random);
            if (right == null || right.eNumber() <= right.aNumber()) throw new AssertionError("E should win");
            var left = NumberPairSelector.select(new int[]{1,4,7}, new int[]{2,5,9}, true, random);
            if (left == null || left.aNumber() <= left.eNumber()) throw new AssertionError("A should win");
        }
        if (NumberPairSelector.select(new int[]{9}, new int[]{1}, false, random) != null)
            throw new AssertionError("Impossible rig must return null");
        if (NumberPairSelector.select(new int[]{0,10}, new int[]{1}, true, random) != null)
            throw new AssertionError("Invalid names must be ignored");
        if (NumberPairSelector.select(new int[]{5}, new int[]{5}, true, random) != null)
            throw new AssertionError("Ties cannot win");
        var selected = NumberPairSelector.select(new int[]{0,2}, new int[]{8,0}, false, random);
        if (selected == null || selected.aSlot() != 1 || selected.eSlot() != 0)
            throw new AssertionError("Must select actual occupied slots");
        System.out.println("PASS: 10000 iterations, both sides, ties, invalid names, actual slot indexes");
    }
}
