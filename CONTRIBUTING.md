# Contributing to Greenery

When creating a new plant class, these things are important to know:

Handle blockstates only in following way:
```kt
private val topProperty: PropertyBool = PropertyBool.create("top")
override fun createPlantContainer() = plantContainer(ageProperty, topProperty,)

init
{
    initBlockState()
    defaultState = blockState.baseState
        .withProperty(ageProperty, 0)
        .withProperty(topProperty, true)
}
```

Sections of code in plant classes should be divided using these comments:

```
// -- BLOCK STATE --

(...block state code here)

// -- BLOCK --

(...block initialization & logic code here)
```
