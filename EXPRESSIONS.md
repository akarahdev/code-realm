# Expressions
Expressions in this mod are created with `/expr`.
They are just like `%var`, `%index`, and others, but are easier to express
and easier to read.

## Basic Values
The available basic values include:
- Variables (e.g `%default kills`)
- Numbers (e.g `0.7`, `-0.4`)
- Strings (e.g `"This is a string"`)
- Text (e.g `$"Texts have a dollar sign!"`)

## Operations
You can perform various operations on these:
- `+` to add numbers, or concatenate strings. 
Note that when your output is a `String` or `Text`, all values are 
treated as `String` and `Text`.
- `-` to subtract numbers
- `*` to multiply numbers
- `/` to divide numbers
- `x[y]` to index into a list
- `x.y` to index into a dictionary (WIP)
- `x..y` to generate a random number between `x` and `y` (WIP)
- `|x|` to round `x` down (WIP)

## Value Precedence
The expression item will attempt to guess the item you want based on your input:
- If it has no strings, it is a `Number`
- If it has a string constant but no text constant, it is a `String`
- If it has a text constant, it is a `Styled Text`

## Examples
- `%default kills + 14` compiles to a `Number` of `%math(%var(%default kills)+14)`
- `%default + $" has joined!"` compiles to a `Text` of `%default has joined!`