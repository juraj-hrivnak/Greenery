# Changelog

## v5.0

### Greenery🌿 changelog

- Added `floating` plant type.
  - It accepts the `compatibleFluids` option to check the block's fluid state.
  - It has a `frosted` property which will be `true` when a block of `Material.ICE` is under it.
  - `isSolid` option set to `true` on these plants is advised, to recreate the vanilla behaviour of lilypads.
- Added `allowedSoils` config option for plants
  - Currently only accepts material names: `["air", "grass", "ground", "wood", "rock", "iron", "anvil", "water", "lava", "leaves", "plants", "vine", "sponge", "cloth", "fire", "sand", "circuits", 
    "carpet", "glass", "redstone_light", "tnt", "coral", "ice", "packed_ice", "snow", "crafted_snow", "cactus", "clay", "gourd", "dragon_egg", "portal", "cake", "web", "piston", "barrier", 
    "structure_void"]`
  - Material names can be uppercase too.
  - This option is backwards compatible, meaning the old values are used if this option is missing.
- Plant generators are now grouped by dimensions when calculating the `patchAttempts`.
- Migrated the build script to Kotlin DSL.

## v4.7

### Greenery🌿 changelog

- Added `maxAge` & `canGrow` config options for plants. Max age is the state in which the plant stops growing. `"canGrow": false` disables plant growth at the random tick.
- Added config option to disable plant generation in super flat worlds.

#### Breaking changes

- Renamed kelp's block state: `remaining_height` to `age`.