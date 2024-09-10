package io.github.qwert26.somedice;

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
}