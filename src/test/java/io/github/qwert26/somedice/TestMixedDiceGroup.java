package io.github.qwert26.somedice;

import java.util.Map;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Class under test is {@link MixedDiceGroup}.
 * 
 * @author Qwert26
 */
class TestMixedDiceGroup {
	@Test
	void disallowsNull() {
		assertThrows(IllegalArgumentException.class, () -> new MixedDiceGroup((IDie[]) null), "Null was allowed!");
	}

	@Test
	void disallowsEmptySource() {
		assertThrows(IllegalArgumentException.class, () -> new MixedDiceGroup(new IDie[0]), "Empty was allowed!");
	}

	@Test
	void disallowsNullSource() {
		assertThrows(IllegalArgumentException.class, () -> new MixedDiceGroup(new IDie[] { null }),
				"Null in source-array was allowed!");
	}

	@ParameterizedTest
	@MethodSource("com.github.qwert26.somedice.TestSingleDie#commonDieSizes")
	void testIdentiyBehavior(int size) {
		SingleDie truth = new SingleDie(size);
		MixedDiceGroup underTest = new MixedDiceGroup(truth);
		Map<Map<Integer, Integer>, Long> expected, actual;
		expected = truth.getAbsoluteFrequencies();
		actual = underTest.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, Long> expectedEntry : expected.entrySet()) {
			assertTrue(actual.containsKey(expectedEntry.getKey()), "Mixed Dice Group removed a key!");
			assertEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()),
					"Mixed Dice Group modified a frequency!");
		}
		for (Map.Entry<Map<Integer, Integer>, Long> actualEntry : actual.entrySet()) {
			assertTrue(expected.containsKey(actualEntry.getKey()), "Mixed Dice Group added a key!");
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@ParameterizedTest
	@MethodSource("com.github.qwert26.somedice.TestSingleDie#commonDieSizes")
	void testIdenticalBehavior(int size) {
		SingleDie baseDie = new SingleDie(size);
		MixedDiceGroup underTest = new MixedDiceGroup(baseDie);
		HomogenousDiceGroup truth = new HomogenousDiceGroup(baseDie);
		assumeFalse(underTest.equals(truth), "Homogenous dice group has become a mixed dice group...");
		assumeFalse(truth.equals(underTest), "Mixed dice group has become a homogenous dice group...");
		Map<Map<Integer, Integer>, Long> expected, actual;
		expected = truth.getAbsoluteFrequencies();
		actual = underTest.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, Long> expectedEntry : expected.entrySet()) {
			assertTrue(actual.containsKey(expectedEntry.getKey()), "Mixed Dice Group removed a key!");
			assertEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()),
					"Mixed Dice Group modified a frequency!");
		}
		for (Map.Entry<Map<Integer, Integer>, Long> actualEntry : actual.entrySet()) {
			assertTrue(expected.containsKey(actualEntry.getKey()), "Mixed Dice Group added a key!");
		}
	}
}
