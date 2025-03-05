# Changelog

## v9.0


> [!WARNING]  
> This update might require you to update the Fluidlogged API to v3.0.0.

- Added support for Fluidlogged API 3.0.0
- Fixed crash when `maxAge` of EmergentPlants was lower than 3.

## v8.0

- Merged pull request by @MasterEnderman in [#31](https://github.com/juraj-hrivnak/Greenery/pull/31).
  - Added a new config option for disable printing debug messages to the log file
  - Defining drops now allows:
    - using metadata -> use `<resource_location>:<meta>`; example `minecraft:wool:5`,
    - using oredicts -> use `ore:<oredict_name>`; example `ore:plankWood`.

- Added "Override Other Bonemeal Events" config.
- Added `isReplaceable` JSON config.
- Implemented a safer way of loading and unloading generators. (Fixes [#30](https://github.com/juraj-hrivnak/Greenery/issues/30).)

## v7.0

### Greenery🌿 changelog

- Implemented improved `soil` configuration.
- The `allowedSoils` config was deprecated. (Still works for backward compatibility.)

Syntax:

```
{
  "soil": [
    "material:<material>",
    "block:<resource_location>",
    "block:<resource_location> | <blockstates>..."
  ]
}
```

Example:

```
{
  "soil": [
    "material:grass",
    "block:minecraft:sand",
    "block:minecraft:stone | variant=granite"
  ]
}
```

## v6.0

### Greenery🌿 changelog

- Added support for arbitrary block generation.
  - Greenery can now generate non-Greenery blocks in the world and when using bonemeal based on configuration.
  - To add configuration for these blocks create JSON files in the `greenery/blocks` directory.
  - Properties of this new configuration are:
    - `blocks` - A list of blocks you want to generate; syntax: `<resource_location>:[<meta>]`; example: `minecraft:cactus`
    - `worldGen` and `allowedSoils` - The same as Greenery plants.
  - Example configuration file for a cactus:
    ```json
    {
      "blocks": [
        "minecraft:cactus",
        "minecraft:cactus",
        "minecraft:cactus"
      ],
      "worldGen": [
        "0 | type:dry | 1.0 | 16 | 32"
      ],
      "allowedSoils": [
        "sand"
      ]
    }
    ```

Thanks to xkforce for commissioning this features!

## v5.1

### Greenery🌿 changelog

- Allowed items in `toolShears` OreDict to be used on upland plants as shears.

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