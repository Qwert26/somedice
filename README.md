# Somedice
SomeDice pulls its inspiration from [AnyDice by Jasper Flick](https://anydice.com/ "Dice Probability Calculator").
The goal is to programmatically analyze dice rolls, but using absolute frequencies instead of relative ones.
## Base die
There are four types of base die:
* SingleDie
	* Single dice produce a continuous line of numbers, usually represented in text form like this: "d20".
	This line can either start at zero or at one.
* RangeDie
	* A range die produces numbers with a certain step size. An example of this dice is the Tens-d10.
* FudgeDie, also known as a Fate die.
* UnfairDie

Each type of die is source of equally distributed numbers, the only exception being the unfair die.
## Dice Groups
There are three types of dice groups:
* Homogeneous
* Mixed
* Indeterministic

### Homogeneous Dice Group (HDG)
A HDG represents multiple identical dice thrown together, usually written like this: "3d6".
Its results are not simulated but rather calculated by using the multi-nomial function.
For further processing, the results are not added together and are kept separate.
### Mixed Dice Group (MDG)
A MDG represents multiple different dice being thrown together.
It can calculate groups with a description like "three d6s together with a d8".
It effectively computes the cartesian product of all individual results, which makes it rather expensive to run.
### Indeterministic Dice Group (IDG)
An IDG computes the case, when one die determines the number of thrown dice inside a group.
It can compute things like "(3d10)d4".