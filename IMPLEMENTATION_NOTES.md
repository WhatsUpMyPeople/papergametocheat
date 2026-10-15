# PaperGame: dispenser rig implementation status

## Completed
- Added `NumberPairSelector` to choose an existing A/E slot pair, using values 1–9 and a strict higher-number rule.
- The selector returns `null` if no valid pair exists; it does not generate or duplicate items.

## Not yet connected to Minecraft
- Server-side Fabric mod entrypoint and networking to authorize and synchronize GUI/keybind settings.
- GUI dispenser selection by world position and dimension (existing 'From hand' only selects block item types).
- Server interception of actual dispenser activation, batching both same-tick redstone triggers and consuming one item from each selected slot exactly once.
- Reliable recognition of custom item names in the actual Minecraft server API.
- Tests in the user's precise Minecraft/Fabric toolchain.

## Important source compatibility issue
The supplied Java imports use Mojang-mapped names such as `net.minecraft.client.Minecraft`, `GuiGraphics`, `KeyMapping` and `Component`, while `build.gradle` selects Yarn mappings. This must be reconciled before compiling. The declared version `26.2` and corresponding dependency versions also require verification against published artifacts.

Do not deploy this as a working dispenser modification yet. It is a safe starting point for the server implementation.

## Validation completed in this iteration
- Added standalone Java tests for both winning sides, impossible pairs, and invalid names.
- These tests run on Java 21 without Minecraft dependencies. They validate the pairing algorithm only, **not** Minecraft dispenser integration.
