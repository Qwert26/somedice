package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link DiceDropper}.
 * 
 * @author <b>Qwert26</b>, main author
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

	@Test
	void doubleAdvantage() {
		DiceDropper underTest = new DiceDropper(new HomogeneousDiceGroup(new SingleDie(20, false), 3), 2, 0);
		assumeTrue(underTest.getDropLowest() == 2);
		assumeTrue(underTest.getDropHighest() == 0);
		Map<Map<Integer, Integer>, BigInteger> result = underTest.getAbsoluteFrequencies();
		assertNotNull(result);
		for (var outer : result.entrySet()) {
			assertNotNull(outer.getValue());
			assertNotNull(outer.getValue());
			for (var inner : outer.getKey().entrySet()) {
				assertNotNull(inner.getKey());
				assertEquals(1, inner.getValue());
			}
		}
	}

	@Test
	void doubleDisadvantage() {
		DiceDropper underTest = new DiceDropper(new HomogeneousDiceGroup(new SingleDie(20, false), 3), 0, 2);
		assumeTrue(underTest.getDropLowest() == 0);
		assumeTrue(underTest.getDropHighest() == 2);
		Map<Map<Integer, Integer>, BigInteger> result = underTest.getAbsoluteFrequencies();
		assertNotNull(result);
		for (var outer : result.entrySet()) {
			assertNotNull(outer.getValue());
			assertNotNull(outer.getValue());
			for (var inner : outer.getKey().entrySet()) {
				assertNotNull(inner.getKey());
				assertEquals(1, inner.getValue());
			}
		}
	}

	@Test
	void checkHashCode() {
		DiceDropper underTest = new DiceDropper(new HomogeneousDiceGroup(new SingleDie(20, false), 3), 1, 1);
		assertDoesNotThrow(() -> underTest.hashCode());
	}

	@Test
	void checkToString() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper underTest = new DiceDropper(source, 1, 1);
		String result = assertDoesNotThrow(() -> underTest.toString());
		assertNotNull(result);
		assertTrue(result.contains(source.toString()));
	}

	@Test
	void checkEqualsNull() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper underTest = new DiceDropper(source, 1, 1);
		assertFalse(underTest.equals(null));
	}

	@Test
	void checkEqualsItself() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper underTest = new DiceDropper(source, 1, 1);
		assertTrue(underTest.equals(underTest));
	}

	@Test
	void checkEqualsWrongClass() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper underTest = new DiceDropper(source, 1, 1);
		assertFalse(underTest.equals(new Object()));
	}

	@Test
	void checkEqualsDifferentDropHighest() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper first = new DiceDropper(source, 1, 1);
		DiceDropper second = new DiceDropper(source, 1, 10);
		assertFalse(first.equals(second));
	}

	@Test
	void checkEqualsDifferentDropLowest() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper first = new DiceDropper(source, 1, 1);
		DiceDropper second = new DiceDropper(source, 10, 1);
		assertFalse(first.equals(second));
	}

	@Test
	void checkEqualsDifferentSources() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper first = new DiceDropper(source, 1, 1);
		source = new HomogeneousDiceGroup(new SingleDie(20, false), 4);
		DiceDropper second = new DiceDropper(source, 1, 1);
		assertFalse(first.equals(second));
	}

	@Test
	void checkEquals() {
		HomogeneousDiceGroup source = new HomogeneousDiceGroup(new SingleDie(20, false), 3);
		DiceDropper first = new DiceDropper(source, 1, 1);
		DiceDropper second = new DiceDropper(source, 1, 1);
		assertTrue(first.equals(second));
	}
}