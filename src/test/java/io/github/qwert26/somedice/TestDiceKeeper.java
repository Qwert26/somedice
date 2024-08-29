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
	@Test
	public void testConstructorNull() {
		assertThrows(NullPointerException.class, () -> new DiceKeeper(null));
		assertThrows(NullPointerException.class, () -> new DiceKeeper(null, 0, 0));
		assertThrows(NullPointerException.class, () -> new DiceKeeper(0, 0, null));
	}

	@Test
	public void testConstructorNegativeMinimum() {
		IDie source = new SingleDie(12);
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(source, -1, 1));
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(-1, 1, source));
	}

	@Test
	public void testConstructorNegativeMaximum() {
		IDie source = new SingleDie(12);
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(source, 1, -1));
		assertThrows(IllegalArgumentException.class, () -> new DiceKeeper(1, -1, source));
	}

	@Test
	public void testSourceIsGiven() {
		IDie source = new SingleDie(20);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source));
		assertEquals(source, keeper.getSource());
	}

	@Test
	public void testMinimumSetter() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source));
		assumeTrue(keeper.getKeepLowest() == 0);
		keeper.setKeepLowest(1);
		assertEquals(1, keeper.getKeepLowest());
	}

	@Test
	public void testMaximumSetter() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source));
		assumeTrue(keeper.getKeepHighest() == 0);
		keeper.setKeepHighest(1);
		assertEquals(1, keeper.getKeepHighest());
	}

	@Test
	public void testMinimumSetterIgnoresNegatives() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(source, 1, 1));
		assumeTrue(keeper.getKeepLowest() == 1);
		assertThrows(IllegalArgumentException.class, () -> keeper.setKeepLowest(-2));
		assertEquals(1, keeper.getKeepLowest());
	}

	@Test
	public void testMaximumSetterIgnoresNegatives() {
		IDie source = new SingleDie(12);
		DiceKeeper keeper = assertDoesNotThrow(() -> new DiceKeeper(1, 1, source));
		assumeTrue(keeper.getKeepHighest() == 1);
		assertThrows(IllegalArgumentException.class, () -> keeper.setKeepHighest(-2));
		assertEquals(1, keeper.getKeepHighest());
	}
}