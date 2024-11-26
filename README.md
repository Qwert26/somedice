# SomeDice

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
It effectively computes the Cartesian product of all individual results, which makes it rather expensive to run.

### Indeterministic Dice Group (IDG)

An IDG computes the case, where one die determines the number of thrown dice inside a homogeneous group.
It can compute things like "(3d10)d4".
Essentially, it computes multiple HDGs and aggregates their results, multiplied with the absolute frequency of the current amount.

## Exploding a Die

Exploding a die means, in general, when its maximum number is shown, it gets thrown again and the results are being aggregated.
In reality, a die can explode potentially infinite times, resulting in absurdly large numbers.
Programs can not handle "Infinity", so each exploding die has a hard limit beyond no explosion will take place.
But as an exchange, we can handle any explosion criteria, such as "explode on even numbers, bigger than half of the maximum value".

## Manipulation

After generating a "compounded" base, it can be manipulated in multiple ways:

### Dropping Dice

This reduces the result by a fixed number of dice, overlapping the result of multiple bases.

### Keeping Dice

This retains a fixed number of dice from a result, discarding all others.
It also overlaps the result of multiple bases.

### Compressing

Compressing a detailed result set down to a single number with a summed up absolute frequency.
It can also be put back into an unfair die, as a base for a new line of grouping and manipulation.