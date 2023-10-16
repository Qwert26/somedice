package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class under test is {@link HomogenousDiceGroup}.
 * 
 * @author Qwert26
 */
class TestHomogenousDiceGroup {
	@Test
	void disallowsNull() {
		assertThrows(NullPointerException.class, () -> new HomogenousDiceGroup(null), "Null was allowed!");
	}

	@Test
	void dissallowsZero() {
		SingleDie someDie = new SingleDie(4);
		assertThrows(IllegalArgumentException.class, () -> new HomogenousDiceGroup(someDie, 0), "Zero was allowed!");
	}

	@Test
	void testIdentityBehavior() {
		FudgeDie truth = FudgeDie.INSTANCE;
		HomogenousDiceGroup underTest = new HomogenousDiceGroup(truth);
		Map<Map<Integer, Integer>, Long> expected, actual;
		expected = truth.getAbsoluteFrequencies();
		actual = underTest.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, Long> expectedEntry : expected.entrySet()) {
			assertTrue(actual.containsKey(expectedEntry.getKey()), "Homogenous Dice Group removed a key!");
			assertEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()),
					"Homogenous Dice Group modified a frequency!");
		}
		for (Map.Entry<Map<Integer, Integer>, Long> actualEntry : actual.entrySet()) {
			assertTrue(expected.containsKey(actualEntry.getKey()), "Homogenous Dice Group added a key!");
		}
	}

	@ParameterizedTest
	@MethodSource("com.github.qwert26.somedice.TestSingleDie#commonDieSizes")
	void testIdentityBehavior(int size) {
		SingleDie truth = new SingleDie(size);
		HomogenousDiceGroup underTest = new HomogenousDiceGroup(truth);
		Map<Map<Integer, Integer>, Long> expected, actual;
		expected = truth.getAbsoluteFrequencies();
		actual = underTest.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, Long> expectedEntry : expected.entrySet()) {
			assertTrue(actual.containsKey(expectedEntry.getKey()), "Homogenous Dice Group removed a key!");
			assertEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()),
					"Homogenous Dice Group modified a frequency!");
		}
		for (Map.Entry<Map<Integer, Integer>, Long> actualEntry : actual.entrySet()) {
			assertTrue(expected.containsKey(actualEntry.getKey()), "Homogenous Dice Group added a key!");
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { 2, 3, 4, 5, 6, 7, 8, 9, 10 })
	void testConstraints(int diceCount) {
		SingleDie coin = new SingleDie(2, true);
		HomogenousDiceGroup underTest = new HomogenousDiceGroup(coin, diceCount);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, Long> entry : result.entrySet()) {
			Map<Integer, Integer> valueCounts = entry.getKey();
			assertTrue(valueCounts.size() <= diceCount, "Homogenous Dice group added one or more dice!");
			int totalDice = 0;
			for (Map.Entry<Integer, Integer> entryVC : valueCounts.entrySet()) {
				totalDice += entryVC.getValue();
			}
			assertEquals(diceCount, totalDice, "Homogenous Dice group added or removed one or multiple dice!");
		}
	}

	@Test
	void checkIdeompotency() {
		SingleDie coin = new SingleDie(2, false);
		HomogenousDiceGroup dieCount = new HomogenousDiceGroup(coin, 1);
		HomogenousDiceGroup countDie = new HomogenousDiceGroup(1, coin);
		assertEquals(dieCount.hashCode(), countDie.hashCode(), "Homogenous dice groups can not be equal!");
		assertTrue(dieCount.equals(countDie), "Homogenous dice groups are not equal!");
	}

	public final static long currentTotalValue(Map<Integer, Integer> valueCount) {
		long ret = 0;
		for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
			ret += entry.getKey() * entry.getValue();
		}
		return ret;
	}
}
