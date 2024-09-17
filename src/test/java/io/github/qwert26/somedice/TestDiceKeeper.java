package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link DiceKeeper}.
 * 
 * @author Qwert26
 */
public class TestDiceKeeper {
	/**
	 * Tests, that any constructor throws an exception, when there is no source.
	 */
	@Test
	void testConstructorNull() {
		assertThrows(NullPointerException.class, () -> new DiceKeeper(null));
		assertThrows(NullPointerException.class, () -> new DiceKeeper(null, 0, 0));
		assertThrows(NullPointerException.class, () -> new DiceKeeper(0, 0, null));
	}

	/**
	 * Tests, that any constructor throws an exception, when a negative amount of
	 * low rolls should be kept.
	 */
	@Test
	void testConstructorNegativeMinimum() {
		IDie source = new SingleDie(12);
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(source, -1, 1));
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(-1, 1, source));
	}

	/**
	 * Tests, that any constructor throws an exception, when a negative amount of
	 * high rolls should be kept.
	 */
	@Test
	void testConstructorNegativeMaximum() {
		IDie source = new SingleDie(12);
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(source, 1, -1));
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(1, -1, source));
	}

	/**
	 * Tests, that the constructor does not unexpectedly changes the given source
	 * instance.
	 */
	@Test
	void testSourceIsGiven() {
		IDie source = new SingleDie(20);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source));
		assertEquals(source, keeper.getSource());
	}

	/**
	 * Tests the setter for the keeping of low rolls.
	 */
	@Test
	void testMinimumSetter() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source));
		assumeTrue(keeper.getKeepLowest() == 0);
		keeper.setKeepLowest(1);
		assertEquals(1, keeper.getKeepLowest());
	}

	/**
	 * Tests the setter for the keeping of high rolls.
	 */
	@Test
	void testMaximumSetter() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source));
		assumeTrue(keeper.getKeepHighest() == 0);
		keeper.setKeepHighest(1);
		assertEquals(1, keeper.getKeepHighest());
	}

	/**
	 * Tests, that the setter for keeping low rolls ignores negative numbers.
	 */
	@Test
	void testMinimumSetterIgnoresNegatives() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source, 1, 1));
		assumeTrue(keeper.getKeepLowest() == 1);
		assertThrows(IllegalArgumentException.class, () -> keeper.setKeepLowest(-2));
		assertEquals(1, keeper.getKeepLowest());
	}

	/**
	 * Tests, that the setter for keeping high rolls ignores negative numbers.
	 */
	@Test
	void testMaximumSetterIgnoresNegatives() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(1, 1, source));
		assumeTrue(keeper.getKeepHighest() == 1);
		assertThrows(IllegalArgumentException.class, () -> keeper.setKeepHighest(-2));
		assertEquals(1, keeper.getKeepHighest());
	}

	/**
	 * Tests some basic behavior of the toString-method:
	 * <ul>
	 * <li>It does not throw any exceptions.</li>
	 * <li>It does not return a <code>null</code>-value.</li>
	 * <li>It contains the result of toString-method of its source.</li>
	 * </ul>
	 */
	@Test
	void testToString() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = new DiceKeeper(source);
		String result = assertDoesNotThrow(() -> keeper.toString());
		assertNotNull(result);
		assertTrue(result.contains(source.toString()));
	}

	@Test
	void keepNone() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = new DiceKeeper(source, 0, 0);
		assertThrows(IllegalStateException.class, () -> keeper.getAbsoluteFrequencies());
	}

	@Test
	void overkeepHigh() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = new DiceKeeper(source, 0, 2);
		assertThrows(IllegalStateException.class, () -> keeper.getAbsoluteFrequencies());
	}

	@Test
	void overkeepLow() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = new DiceKeeper(source, 2, 0);
		assertThrows(IllegalStateException.class, () -> keeper.getAbsoluteFrequencies());
	}

	@Test
	void keepOneOneFromThree() {
		HomogeneousDiceGroup hdg = new HomogeneousDiceGroup(DiceCollection.WRATH_AND_GLORY_DIE, 3);
		DiceKeeper keeper = new DiceKeeper(hdg, 1, 1);
		Map<Map<Integer, Integer>, BigInteger> result = assertDoesNotThrow(() -> keeper.getAbsoluteFrequencies());
		assertNotNull(result);
	}

	@Test
	void checkHashCode() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = new DiceKeeper(source, 0, 0);
		final int zeroZero = assertDoesNotThrow(() -> keeper.hashCode());
		keeper.setKeepHighest(9);
		final int zeroNine = assertDoesNotThrow(() -> keeper.hashCode());
		assertNotEquals(zeroZero, zeroNine);
		keeper.setKeepLowest(9);
		final int nineNine = assertDoesNotThrow(() -> keeper.hashCode());
		assertNotEquals(zeroZero, nineNine);
		assertNotEquals(zeroNine, nineNine);
		keeper.setKeepHighest(0);
		final int nineZero = assertDoesNotThrow(() -> keeper.hashCode());
		assertNotEquals(zeroZero, nineZero);
		assertNotEquals(zeroNine, nineZero);
		assertNotEquals(nineNine, nineZero);
	}

	@Test
	void checkEqualsNull() {
		DiceKeeper keeper = new DiceKeeper(new SingleDie(20), 0, 0);
		assertFalse(keeper.equals(null));
	}

	@Test
	void checkEqualsItself() {
		DiceKeeper keeper = new DiceKeeper(new SingleDie(20), 0, 0);
		assertTrue(keeper.equals(keeper));
	}

	@Test
	void checkEqualsWrongClass() {
		DiceKeeper keeper = new DiceKeeper(new SingleDie(20), 0, 0);
		assertFalse(keeper.equals(new Object()));
	}

	@Test
	void checkEqualsDifferentKeepHighest() {
		DiceKeeper first = new DiceKeeper(new SingleDie(20), 0, 0);
		DiceKeeper second = new DiceKeeper(new SingleDie(20), 0, 1);
		assertFalse(first.equals(second));
	}

	@Test
	void checkEqualsDifferentKeepLowest() {
		DiceKeeper first = new DiceKeeper(new SingleDie(20), 0, 0);
		DiceKeeper second = new DiceKeeper(new SingleDie(20), 1, 0);
		assertFalse(first.equals(second));
	}

	@Test
	void checkEqualsDifferentSources() {
		DiceKeeper first = new DiceKeeper(new SingleDie(20), 0, 0);
		DiceKeeper second = new DiceKeeper(new SingleDie(10), 0, 0);
		assertFalse(first.equals(second));
	}

	@Test
	void checkEquals() {
		DiceKeeper first = new DiceKeeper(new SingleDie(20), 0, 0);
		DiceKeeper second = new DiceKeeper(new SingleDie(20), 0, 0);
		assertTrue(first.equals(second));
	}
}