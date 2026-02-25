![PsiTweaker logo](src/main/resources/logo.png)
# PsiTweaker
A [Psi](https://psi.vazkii.net/) addon that makes some things configurable

## Config Options
All options take an expression as a string.
Expressions use [EvalEx](https://github.com/ezylang/EvalEx), see the [reference](https://ezylang.github.io/EvalEx/references/references.html) for more information.

All expressions default to `base` which leaves the value unmodified.

- **capacity**: player psi capacity
- **regen**: psi regenerated per tick
- **cast.regen_cd**: psi regeneration cooldown after cast in ticks
- **damage.regen_cd**: psi regeneration freeze when taking damage in ticks
- **damage.psi_loss**: psi lost when taking damage
- **overflow.damage.amount**: damage taken from overflow
- **bullet.cost_modifier**: spell cost modifier of spell bullet types

## Variables
| variable    | available in                                                              | type                                      | description                           |
|-------------|---------------------------------------------------------------------------|-------------------------------------------|---------------------------------------|
| base        | all                                                                       | number                                    | the unmodified value                  |
| health      | capacity, regen, damage.regen_cd, damage.psi_loss, overflow.damage.amount | number                                    | current player health                 |
| maxhealth   | capacity, regen, damage.regen_cd, damage.psi_loss, overflow.damage.amount | number                                    | max player health                     |
| armor       | capacity, regen, damage.regen_cd, damage.psi_loss, overflow.damage.amount | number                                    | player armor value                    |
| loopcasting | capacity, regen, damage.regen_cd, damage.psi_loss, overflow.damage.amount | bool                                      | is the player currently loopcasting   |
| psi         | regen, damage.regen_cd, damage.psi_loss, overflow.damage.amount           | number                                    | current psi of player                 |
| maxpsi      | regen, damage.regen_cd, damage.psi_loss, overflow.damage.amount           | number                                    | psi capacity of player                |
| damage      | damage.regen_cd                                                           | number                                    | damage taken                          |
| slot        | cast.regen_cd                                                             | string? (InteractionHand / EquipmentSlot) | the slot the spell was cast from      |
| from        | cast.regen_cd                                                             | "hand" / "armor"                          | whether the spell was cast from armor |
| item        | cast.regen_cd                                                             | string (item ID)                          | the casting item used                 |
| type        | bullet.cost_modifier                                                      | string (bullet type)                      | the spell bullet type                 |
