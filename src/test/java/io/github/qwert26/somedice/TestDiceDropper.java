package io.github.qwert26.somedice;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Class under test is {@link DiceDropper}.
 * 
 * @author Qwert26
 */
class TestDiceDropper {
	@Test
	void disallowsNull() {
		assertThrows(NullPointerException.class, () -> new DiceDropper(null), "Dice filter allows null!");
	}

	@Test
	void disallowsNegativDropLowest() {
		DiceDropper underTest = new DiceDropper(new SingleDie(2));
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropLowest(-1),
				"Dice filter allows a drop of less than zero!");
	}

	@Test
	void disallowsNegativDropHighest() {
		DiceDropper underTest = new DiceDropper(new SingleDie(2));
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropHighest(-1),
				"Dice filter allows a drop of less than zero!");
	}

	@Test
	void disallowsOverfilteringHigh() {
		DiceDropper underTest = new DiceDropper(new SingleDie(4));
		underTest.setDropHighest(2);
		underTest.setDropLowest(0);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies(),
				"Dice filter overfiltered!");
	}

	@Test
	void disallowsOverfilteringLow() {
		DiceDropper underTest = new DiceDropper(new SingleDie(4));
		underTest.setDropHighest(0);
		underTest.setDropLowest(2);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies(),
				"Dice filter overfiltered!");
	}

	@Test
	void disallowsEmptyMapsForKeysViaHigh() {
		DiceDropper underTest = new DiceDropper(new SingleDie(4));
		underTest.setDropHighest(1);
		underTest.setDropLowest(0);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies(),
				"Dice filter overfiltered!");
	}

	@Test
	void disallowsEmptyMapsForKeysViaLow() {
		DiceDropper underTest = new DiceDropper(new SingleDie(4));
		underTest.setDropHighest(0);
		underTest.setDropLowest(1);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies(),
				"Dice filter overfiltered!");
	}

	@Test
	void keepsSource() {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		assertEquals(FudgeDie.INSTANCE, underTest.getSource(), "Source is not truthful!");
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropHighest(int value) {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		underTest.setDropHighest(value);
		assertEquals(value, underTest.getDropHighest());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropLowest(int value) {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		underTest.setDropLowest(value);
		assertEquals(value, underTest.getDropLowest());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropHighestAfterInsertion(int value) {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		underTest.setDropHighest(value);
		assumeTrue(value == underTest.getDropHighest());
		assumeTrue(() -> {
			boolean ret = false;
			try {
				underTest.setDropHighest(-1);
			} catch (Throwable t) {
				ret = true;
			}
			return ret;
		});
		assertEquals(value, underTest.getDropHighest());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropLowestAfterInsertion(int value) {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		underTest.setDropLowest(value);
		assumeTrue(value == underTest.getDropLowest());
		assumeTrue(() -> {
			boolean ret = false;
			try {
				underTest.setDropLowest(-1);
			} catch (Throwable t) {
				ret = true;
			}
			return ret;
		});
		assertEquals(value, underTest.getDropLowest());
	}

	@Test
	void checkSingleAdvantage() {
		final int size = 20;
		SingleDie d20 = new SingleDie(size, false);
		HomogenousDiceGroup multiDice = new HomogenousDiceGroup(d20, 2);
		DiceDropper underTest = new DiceDropper(multiDice);
		underTest.setDropHighest(0);
		underTest.setDropLowest(1);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(size, result.size());
		for (Map.Entry<Map<Integer, Integer>, Long> resultEntry : result.entrySet()) {
			Map<Integer, Integer> valueCount = resultEntry.getKey();
			assertEquals(1, valueCount.size(), "There should have been exactly one number!");
			Map.Entry<Integer, Integer> single = valueCount.entrySet().iterator().next();
			assertEquals(1, single.getValue(), "Dropping one die from two dice should produce only a single number!");
			long expected = single.getKey() * 2 - 1;
			assertEquals(expected, resultEntry.getValue(), "Theoretical distribution is unequal to actual one!");
		}
	}

	@Test
	void checkSingleDisadvantage() {
		final int size = 20;
		SingleDie d20 = new SingleDie(size, false);
		HomogenousDiceGroup multiDice = new HomogenousDiceGroup(d20, 2);
		DiceDropper underTest = new DiceDropper(multiDice);
		underTest.setDropHighest(1);
		underTest.setDropLowest(0);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(size, result.size());
		for (Map.Entry<Map<Integer, Integer>, Long> resultEntry : result.entrySet()) {
			Map<Integer, Integer> valueCount = resultEntry.getKey();
			assertEquals(1, valueCount.size(), "There should have been exactly one number!");
			Map.Entry<Integer, Integer> single = valueCount.entrySet().iterator().next();
			assertEquals(1, single.getValue(), "Dropping one die from two dice should produce only a single number!");
			long expected = (size - single.getKey() + 1) * 2 - 1;
			assertEquals(expected, resultEntry.getValue(), "Theoretical distribution is unequal to actual one!");
		}
	}

	/**
	 * @see OEIS:A003215
	 */
	@Test
	void checkDoubleAdvantage() {
		final int size = 20;
		SingleDie d20 = new SingleDie(size, false);
		HomogenousDiceGroup multiDice = new HomogenousDiceGroup(d20, 3);
		DiceDropper underTest = new DiceDropper(multiDice);
		underTest.setDropHighest(0);
		underTest.setDropLowest(2);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(size, result.size());
		for (Map.Entry<Map<Integer, Integer>, Long> resultEntry : result.entrySet()) {
			Map<Integer, Integer> valueCount = resultEntry.getKey();
			assertEquals(1, valueCount.size(), "There should have been exactly one number!");
			Map.Entry<Integer, Integer> single = valueCount.entrySet().iterator().next();
			assertEquals(1, single.getValue(), "Dropping two dice from three should produce only a single number!");
			long expected = 3 * single.getKey() * (single.getKey() - 1) + 1;
			assertEquals(expected, resultEntry.getValue(), "Theoretical distribution is unequal to actual one!");
		}
	}

	/**
	 * @see OEIS:A003215
	 */
	@Test
	void checkDoubleDisadvantage() {
		final int size = 20;
		SingleDie d20 = new SingleDie(size, false);
		HomogenousDiceGroup multiDice = new HomogenousDiceGroup(d20, 3);
		DiceDropper underTest = new DiceDropper(multiDice);
		underTest.setDropHighest(2);
		underTest.setDropLowest(0);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(size, result.size());
		for (Map.Entry<Map<Integer, Integer>, Long> resultEntry : result.entrySet()) {
			Map<Integer, Integer> valueCount = resultEntry.getKey();
			assertEquals(1, valueCount.size(), "There should have been exactly one number!");
			Map.Entry<Integer, Integer> single = valueCount.entrySet().iterator().next();
			assertEquals(1, single.getValue(), "Dropping two dice from three should produce only a single number!");
			long expected = 3 * (size - single.getKey()) * (size - single.getKey() + 1) + 1;
			assertEquals(expected, resultEntry.getValue(), "Theoretical distribution is unequal to actual one!");
		}
	}
}