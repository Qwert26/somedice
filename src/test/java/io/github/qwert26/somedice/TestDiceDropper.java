package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link DiceDropper}.
 * 
 * @author Qwert26
 */
public class TestDiceDropper {
	@Test
	void constructorDisallowsNoSource() {
		assertThrows(NullPointerException.class, () -> new DiceDropper(null));
		assertThrows(NullPointerException.class, () -> new DiceDropper(null, 0, 0));
		assertThrows(NullPointerException.class, () -> new DiceDropper(0, 0, null));
	}

	@Test
	void constructorDisallowsNegativeDrops() {
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(FudgeDie.INSTANCE, 0, -1));
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(FudgeDie.INSTANCE, -1, 0));
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(0, -1, FudgeDie.INSTANCE));
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(-1, 0, FudgeDie.INSTANCE));
	}

	@Test
	void disallowsNegativeValues() {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropHighest(-2));
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropLowest(-2));
	}

	@Test
	void keepsSourceUnfairDie() {
		DiceDropper underTest = new DiceDropper(DiceCollection.WRATH_AND_GLORY_DIE);
		assertEquals(DiceCollection.WRATH_AND_GLORY_DIE, underTest.getSource());
	}

	@Test
	void keepsSourceFudgeDie() {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		assertEquals(FudgeDie.INSTANCE, underTest.getSource());
	}

	@Test
	void keepsSourceRangeDie() {
		DiceDropper underTest = new DiceDropper(DiceCollection.DICE_0_TO_90_IN_10);
		assertEquals(DiceCollection.DICE_0_TO_90_IN_10, underTest.getSource());
	}

	@Test
	void keepsSourceSingleDie() {
		DiceDropper underTest = new DiceDropper(new SingleDie(20));
		assertEquals(new SingleDie(20), underTest.getSource());
	}

	@Test
	void keepsValues() {
		DiceDropper underTest = new DiceDropper(DiceCollection.WRATH_AND_GLORY_DIE, 1, 1);
		assumeTrue(underTest.getDropHighest() == 1);
		assumeTrue(underTest.getDropLowest() == 1);
		try {
			underTest.setDropHighest(-3);
			fail("Expected an excpeption, got none!");
		} catch (IllegalArgumentException iae) {
		}
		try {
			underTest.setDropLowest(-3);
			fail("Expected an excpeption, got none!");
		} catch (IllegalArgumentException iae) {
		}
		assertEquals(1, underTest.getDropHighest());
		assertEquals(1, underTest.getDropLowest());
	}
}