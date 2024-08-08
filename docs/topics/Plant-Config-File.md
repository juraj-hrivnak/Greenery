# Plant Config File

A _plant config file_ (`.json`) is a file that Greenery uses to register and configure a plant.

## Properties

`maxAge`
: The max age of this plant. (The max age is the state in which the plant stops growing.)
: Type: `Int`
: The default value depends on the plant type.

`canGrow`
: Whether this plant can grow on the random tick.
: Type: `Boolean`
: Default: `true`

[`worldGen`](worldgen.md)
: The world generation options of this plant.
: Type: `List<String>`
: The default values is an empty list.

[`drops`](drops.md)
: The drops of this plant.
: Type: `List<String>`
: The default values is an empty list.

`allowedSoils`
: The allowed soils of this plant.
: Currently only accepts material names: `air`, `grass`, `ground`, `wood`, `rock`, `iron`, `anvil`, `water`, `lava`, `leaves`, `plants`, `vine`, `sponge`, `cloth`, `fire`, `sand`, `circuits`,  `carpet`, `glass`, `redstone_light`, `tnt`, `coral`, `ice`, `packed_ice`, `snow`, `crafted_snow`, `cactus`, `clay`, `gourd`, `dragon_egg`, `portal`, `cake`, `web`, `piston`, `barrier`,  `structure_void`
: Type: `List<String>`
: The default value depends on the plant type.

`compatibleFluids`
: The compatible fluids of this plant.
: Type: `List<String>`
: The default values is an empty list.

`hasTintIndex`
: Whether this plant uses the biomes' grass color tint index.
: Type: `Boolean`
: Default: `false`

`hasOffset`
: Whether this plant has a slight _y level_ offset. (Like Vanilla grass blocks.)
: Type: `Boolean`
: Default: `true`

`isSolid`
: Whether this plant is solid or not.
: Type: `Boolean`
: Default: `false`

`isHarmful`
: Whether this plant is harmful (or prickly) or not.
: Type: `Boolean`
: Default: `false`

## Example

For example, we will create a config file in the `floating` subdirectory named `example_plant.json`
to create a lily pad which will generate only in `wet`, `swamp` or `lush` biome dictionary types.

<code-block lang="json" collapsed-title="plants/floating/example_plant.json" collapsible="true">
{
    &quot;worldGen&quot;: [
        &quot;0 | type:wet | 0.5 | 16 | 64&quot;,
        &quot;0 | type:swamp | 0.5 | 16 | 64&quot;,
        &quot;0 | type:lush | 0.5 | 16 | 64&quot;
    ],
    &quot;drops&quot;: [
        &quot;minecraft:diamond | 0.2&quot;
    ],
    &quot;compatibleFluids&quot;: [
        &quot;water&quot;
    ],
    &quot;hasOffset&quot;: false,
    &quot;isSolid&quot;: true
}
</code-block>

<seealso style="cards">
   <category ref="related">
       <a href="Creating-Custom-Plants.md"/>
   </category>
</seealso>