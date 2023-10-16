package io.github.qwert26.somedice;

import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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