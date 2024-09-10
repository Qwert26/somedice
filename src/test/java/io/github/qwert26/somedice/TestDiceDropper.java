package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link DiceDropper}.
 * 
 * @author Qwert26
 */
@Tag("Integration")
public class TestDiceDropper {
	/**
	 * Tests that any constructor should throw an exception, if the source is
	 * missing.
	 */
	@Test
	void constructorDisallowsNoSource() {
		assertThrows(NullPointerException.class, () -> new DiceDropper(null));
		assertThrows(NullPointerException.class, () -> new DiceDropper(null, 0, 0));
		assertThrows(NullPointerException.class, () -> new DiceDropper(0, 0, null));
	}

	/**
	 * Tests that any constructor should throw an exception, if the number of
	 * dropped dice is negative on either end.
	 */
	@Test
	void constructorDisallowsNegativeDrops() {
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(FudgeDie.INSTANCE, 0, -1));
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(FudgeDie.INSTANCE, -1, 0));
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(0, -1, FudgeDie.INSTANCE));
		assertThrows(IllegalArgumentException.class, () -> new DiceDropper(-1, 0, FudgeDie.INSTANCE));
	}

	/**
	 * Tests that the setter for dropping values throw an exception when receiving a
	 * negative number.
	 */
	@Test
	void disallowsNegativeValues() {
		DiceDropper underTest = new DiceDropper(FudgeDie.INSTANCE);
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropHighest(-2));
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropLowest(-2));
	}

	/**
	 * Tests, that the constructor does not unexpectedly change the source-instance.
	 */
	@Test
	void keepsSourceUnfairDie() {
		DiceDropper underTest = new DiceDropper(DiceCollection.WRATH_AND_GLORY_DIE);
		assertEquals(DiceCollection.WRATH_AND_GLORY_DIE, underTest.getSource());
	}

	/**
	 * Tests, that the constructor does not unexpectedly change the source-instance.
	 */
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

	/**
	 * Tests, that the constructor does not unexpectedly change the source-instance.
	 */
	@Test
	void keepsSourceSingleDie() {
		DiceDropper underTest = new DiceDropper(new SingleDie(20));
		assertEquals(new SingleDie(20), underTest.getSource());
	}

	/**
	 * Tests, that the setters do not change the current dropping-settings, when
	 * throwing an exception.
	 */
	@Test
	void keepsValues() {
		DiceDropper underTest = new DiceDropper(DiceCollection.WRATH_AND_GLORY_DIE, 1, 1);
		assumeTrue(underTest.getDropHighest() == 1);
		assumeTrue(underTest.getDropLowest() == 1);
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropHighest(-3));
		assertThrows(IllegalArgumentException.class, () -> underTest.setDropLowest(-3));
		assertEquals(1, underTest.getDropHighest());
		assertEquals(1, underTest.getDropLowest());
	}

	/**
	 * Tests, that the request for absolute frequencies results in an exception,
	 * when too many low values are being dropped.
	 */
	@Test
	void overdroppingHigh() {
		DiceDropper underTest = new DiceDropper(0, 2, DiceCollection.WRATH_AND_GLORY_DIE);
		assumeTrue(underTest.getDropLowest() == 0);
		assumeTrue(underTest.getDropHighest() > 1);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies());
	}

	/**
	 * Tests, that the request for absolute frequencies results in an exception,
	 * when too many high values are being dropped.
	 */
	@Test
	void overdroppingLow() {
		DiceDropper underTest = new DiceDropper(2, 0, DiceCollection.WRATH_AND_GLORY_DIE);
		assumeTrue(underTest.getDropLowest() > 1);
		assumeTrue(underTest.getDropHighest() == 0);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies());
	}

	/**
	 * Tests, that the request for absolute frequencies without any dropped dice,
	 * should not modify the absolute frequencies of its source.
	 */
	@Test
	void noDropping() {
		DiceDropper underTest = new DiceDropper(DiceCollection.WRATH_AND_GLORY_DIE, 0, 0);
		assumeTrue(underTest.getDropHighest() == 0);
		assumeTrue(underTest.getDropLowest() == 0);
		Map<Map<Integer, Integer>, BigInteger> result = assertDoesNotThrow(() -> underTest.getAbsoluteFrequencies());
		assertNotNull(result);
		assertEquals(DiceCollection.WRATH_AND_GLORY_DIE.getAbsoluteFrequencies(), result);
	}

	/**
	 * Tests, that dropping enough low and high dice WITHOUT going over the limit,
	 * should also result in an exception being thrown from
	 * {@link DiceDropper#getAbsoluteFrequencies()}.
	 */
	@Test
	void overdroppingOnPoint() {
		HomogeneousDiceGroup hdg = new HomogeneousDiceGroup(DiceCollection.WRATH_AND_GLORY_DIE, 2);
		DiceDropper underTest = new DiceDropper(hdg, 1, 1);
		assumeTrue(underTest.getDropHighest() == 1);
		assumeTrue(underTest.getDropLowest() == 1);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies());
	}
}