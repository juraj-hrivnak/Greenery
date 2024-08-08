# drops

```kotlin
modid:name * count (ResourceLocation * Int)  | chance (Double) | blockStates (List<IBlockState>)
```

- The first two parameters are required.
- The third parameter is optional. It can have an unlimited number of properties that must be comma-separated.

<procedure title="Examples" type="choices">
<step>

Adds one `minecraft:diamond` with a `100%` chance to drop when the plant has `age=3` property.

```kotlin
minecraft:diamond | 1.0 | age=3
```

</step>
<step>

Adds three `minecraft:diamond`s with a `50%` chance to drop.

```kotlin
minecraft:diamond * 3 | 0.5
```

</step>
<step>

Drops the default item for the plant with a `100%` chance when the plant has `age=3` and `single=false` properties.
For example, for cattail, the default item would be cattail. 😛

```kotlin
this | 1.0 | age=3, single=false
```

</step>
<step>

Drop the default seed item defined by forge with a `20%` chance. It should be compatible with all the mods altering default seeds. (For example [CraftTweaker](https://docs.blamejared.com/1.12/fr/Vanilla/Recipes/Seeds).)

```kotlin
seeds | 0.2
```

</step>
<step>

The parser is smart! So even this is a valid configuration:

```kotlin
minecraft  :   diamond   |1 . 0  |   age =3
```

</step>
</procedure>
