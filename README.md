# LitematicAssist
Client-side Fabric utility mod for Minecraft 1.21.11 (Java 21). Optional: Litematica, Meteor (detected only).

## Build
```
gradle wrapper   # once, generates ./gradlew
./gradlew build  # jar in build/libs/
```
Verify versions in `gradle.properties` at https://fabricmc.net/develop first. CI: `.github/workflows/build.yml`.

## Install
Put the jar in `mods/` with Fabric Loader, Fabric API, Cloth Config (Mod Menu recommended).
Keybinds: Options > Controls > LitematicAssist (unbound by default). Config: `config/litematicassist.json`.

## Modules (this batch)
| Module | How it works | What can break it |
|---|---|---|
| Auto Mace | After a wind charge, while falling with a mace, attacks the entity already under the crosshair within 3.0 blocks. No aiming. | Cooldown not ready; target not under crosshair; server-side reach/combat checks |
| Auto Sell | In a chest GUI whose title matches config, shift-clicks configured items (keep N) with delay, optional confirm slot | Wrong title/slot config; plugin GUIs that reject shift-click; item ids |
| Auto Auction | Sends your configured listing command once for the held item | Command syntax differs per plugin |

Not included: dupe modules, Auto Build, Hunt Elytra, Ancient Debris, Get Resources, Get Mace.
