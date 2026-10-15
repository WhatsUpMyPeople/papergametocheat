# PaperGame progress (2026-10-14)

## Tested
- `NumberPairSelector` chooses a random *existing slot pair* such that A or E has the strictly higher number.
- It handles invalid numbers, ties and impossible pairings without inventing items.
- Run the standalone tests with `javac -d out src/main/java/com/example/papergame/server/NumberPairSelector.java tests/NumberPairSelectorTest.java && java -cp out NumberPairSelectorTest`.

## Not yet implemented / not verified
- Real dispenser activation interception, simultaneous redstone pairing, and atomic consumption of the selected inventory slots.
- Authoritative server configuration and client-to-server messages for P/G/H and dispenser selection.
- Java source migration to Minecraft 26.2 official mappings (previous GitHub run reported 18 compilation errors).
- Successful Fabric Gradle build and in-game testing.

**Do not install this ZIP as a finished mod.** The current GUI only simulates output and stores local settings.

## Current update
- Added `DispenserPairCoordinator` to plan paired outputs with a strict winner and revalidate inventory slots before use.
- Added 20,000 additional deterministic selection tests plus stale-inventory and impossible-pair tests.
- GitHub Actions now runs these pure-Java tests before attempting the mod build.
- These changes **do not** make the mod functional on a Fabric server: no dispenser hook, server networking, or Minecraft 26.2 source migration has been implemented or verified.
