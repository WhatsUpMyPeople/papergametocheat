import com.example.papergame.server.DispenserPairCoordinator;
import java.util.Random;

public class DispenserPairCoordinatorTest {
    public static void main(String[] args) {
        Random r = new Random(1234);
        for (int i = 0; i < 10000; i++) {
            var p = DispenserPairCoordinator.plan(new int[]{1, 5, 8}, new int[]{2, 6, 9}, true, r);
            if (!DispenserPairCoordinator.stillValid(p, new int[]{1, 5, 8}, new int[]{2, 6, 9}, true))
                throw new AssertionError("right win plan invalid");
            var q = DispenserPairCoordinator.plan(new int[]{1, 5, 8}, new int[]{2, 6, 9}, false, r);
            if (!DispenserPairCoordinator.stillValid(q, new int[]{1, 5, 8}, new int[]{2, 6, 9}, false))
                throw new AssertionError("left win plan invalid");
        }
        var p = DispenserPairCoordinator.plan(new int[]{1}, new int[]{9}, true, r);
        if (DispenserPairCoordinator.stillValid(p, new int[]{1}, new int[]{8}, true))
            throw new AssertionError("stale plan accepted");
        if (DispenserPairCoordinator.plan(new int[]{9}, new int[]{1}, true, r) != null)
            throw new AssertionError("impossible win accepted");
        if (DispenserPairCoordinator.plan(new int[]{5}, new int[]{5}, true, r) != null)
            throw new AssertionError("tie accepted");
        System.out.println("PASS: 10000 plans per side, stale inventory, impossible pairs and ties");
    }
}
