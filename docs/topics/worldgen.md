# worldGen

```kotlin
dimension (Int) | condition* | generationChance (Double) | patchAttempts (Int) | plantAttempts (Int)
```

- Where `condition` can be:

    - ```kotlin
      biome:modid:name (biome:ResourceLocation)
      ```

    - ```kotlin
      type:name (type:BiomeDictionary.Type)
      ```

    - ```kotlin
      anywhere / everywhere
      ```

<procedure title="Examples" type="choices">
<step>

This will generate the plant in the `biomesoplenty:pasture` biome with a `100%` chance,
and in any biome with the `dry` and `sparse` BiomeDictionary Type with a `50%` chance:

```kotlin
0 | biome:biomesoplenty:pasture | 1.0 | 128 | 64
0 | type:dry | 0.5 | 1 | 8
0 | type:sparse | 0.5 | 1 | 8
```

</step>
<step>

You can reverse the condition using `!`:

```kotlin
0 | !type:savanna | 1.0 | 16 | 64
0 | !type:plains | 1.0 | 16 | 64
0 | !type:beach | 1.0 | 16 | 64
0 | !type:dry | 1.0 | 16 | 64
0 | !type:jungle | 1.0 | 16 | 64
```

</step>
<step>

Use the keyword `anywhere` or `everywhere` to generate the plant in every biome:

```kotlin
0 | anywhere | 0.5 | 8 | 32
```

</step>
</procedure>
