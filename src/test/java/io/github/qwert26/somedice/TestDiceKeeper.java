package io.github.qwert26.somedice;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/**
 * Class under test is {@link DiceKeeper}.
 */
class TestDiceKeeper {
	@Test
	void disallowsNull() {
		assertThrows(NullPointerException.class, () -> new DiceKeeper(null));
	}

	@Test
	void disallowsExecution() {
		DiceKeeper underTest = new DiceKeeper(FudgeDie.INSTANCE);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies());
	}

	@Test
	void disallowsNegativKeepLowest() {
		DiceKeeper underTest = new DiceKeeper(new SingleDie(2));
		assertThrows(IllegalArgumentException.class, () -> underTest.setKeepLowest(-1),
				"Dice filter allows a drop of less than zero!");
	}

	@Test
	void disallowsNegativKeepHighest() {
		DiceKeeper underTest = new DiceKeeper(new SingleDie(2));
		assertThrows(IllegalArgumentException.class, () -> underTest.setKeepHighest(-1),
				"Dice filter allows a drop of less than zero!");
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropHighest(int value) {
		DiceKeeper underTest = new DiceKeeper(FudgeDie.INSTANCE);
		underTest.setKeepHighest(value);
		assertEquals(value, underTest.getKeepHighest());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropLowest(int value) {
		DiceKeeper underTest = new DiceKeeper(FudgeDie.INSTANCE);
		underTest.setKeepLowest(value);
		assertEquals(value, underTest.getKeepLowest());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropHighestAfterInsertion(int value) {
		DiceKeeper underTest = new DiceKeeper(FudgeDie.INSTANCE);
		underTest.setKeepHighest(value);
		assumeTrue(value == underTest.getKeepHighest());
		assumeTrue(() -> {
			boolean ret = false;
			try {
				underTest.setKeepHighest(-1);
			} catch (Throwable t) {
				ret = true;
			}
			return ret;
		});
		assertEquals(value, underTest.getKeepHighest());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void keepsValuesDropLowestAfterInsertion(int value) {
		DiceKeeper underTest = new DiceKeeper(FudgeDie.INSTANCE);
		underTest.setKeepLowest(value);
		assumeTrue(value == underTest.getKeepLowest());
		assumeTrue(() -> {
			boolean ret = false;
			try {
				underTest.setKeepLowest(-1);
			} catch (Throwable t) {
				ret = true;
			}
			return ret;
		});
		assertEquals(value, underTest.getKeepLowest());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void canImmitateDropperAdvantage() {
		SingleDie d20 = new SingleDie(20);
		HomogenousDiceGroup group = new HomogenousDiceGroup(d20, 2);
		DiceDropper truth = new DiceDropper(group);
		DiceKeeper underTest = new DiceKeeper(group);
		assumeFalse(truth.equals(underTest), "DiceKeeper has become a sub-class of DiceDropper!");
		assumeFalse(underTest.equals(truth), "DiceDropper has become a sub-class of DiceKeeper!");
		truth.setDropLowest(1);
		underTest.setKeepHighest(1);
		Map<Map<Integer, Integer>, Long> resultTruth = truth.getAbsoluteFrequencies();
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(resultTruth, result);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void canImmitateDropperDisadvantage() {
		SingleDie d20 = new SingleDie(20);
		HomogenousDiceGroup group = new HomogenousDiceGroup(d20, 2);
		DiceDropper truth = new DiceDropper(group);
		DiceKeeper underTest = new DiceKeeper(group);
		assumeFalse(truth.equals(underTest), "DiceKeeper has become a sub-class of DiceDropper!");
		assumeFalse(underTest.equals(truth), "DiceDropper has become a sub-class of DiceKeeper!");
		truth.setDropHighest(1);
		underTest.setKeepLowest(1);
		Map<Map<Integer, Integer>, Long> resultTruth = truth.getAbsoluteFrequencies();
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(resultTruth, result);
	}
}