package io.github.qwert26.somedice;

import java.util.function.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link DiceExploder}, currently WIP.
 * 
 * @author <b>Qwert26</b>, main author
 */
public class TestDiceExploder {
	/**
	 * 
	 */
	@Test
	void errorWithoutSource() {
		final AbstractDie source = null;
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = 4;
		assertThrows(NullPointerException.class, () -> new DiceExploder(source, explodeOn));
		assertThrows(NullPointerException.class, () -> new DiceExploder(source, explodeOn, explosionDepth));
		assertThrows(NullPointerException.class, () -> new DiceExploder(source, explosionDepth, explodeOn));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explosionDepth, source, explodeOn));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explodeOn, source));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explodeOn, source, explosionDepth));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explodeOn, explosionDepth, source));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explosionDepth, explodeOn, source));
	}

	/**
	 * 
	 */
	@Test
	void errorWithoutPredicate() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = null;
		final byte explosionDepth = 2;
		assertThrows(NullPointerException.class, () -> new DiceExploder(source, explodeOn));
		assertThrows(NullPointerException.class, () -> new DiceExploder(source, explodeOn, explosionDepth));
		assertThrows(NullPointerException.class, () -> new DiceExploder(source, explosionDepth, explodeOn));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explosionDepth, source, explodeOn));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explodeOn, source));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explodeOn, source, explosionDepth));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explodeOn, explosionDepth, source));
		assertThrows(NullPointerException.class, () -> new DiceExploder(explosionDepth, explodeOn, source));
	}

	/**
	 * 
	 */
	@Test
	void errorWithNegativeDepth() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = -4;
		assertThrows(IllegalArgumentException.class, () -> new DiceExploder(source, explodeOn, explosionDepth));
		assertThrows(IllegalArgumentException.class, () -> new DiceExploder(source, explosionDepth, explodeOn));
		assertThrows(IllegalArgumentException.class, () -> new DiceExploder(explosionDepth, source, explodeOn));
		assertThrows(IllegalArgumentException.class, () -> new DiceExploder(explodeOn, source, explosionDepth));
		assertThrows(IllegalArgumentException.class, () -> new DiceExploder(explodeOn, explosionDepth, source));
		assertThrows(IllegalArgumentException.class, () -> new DiceExploder(explosionDepth, explodeOn, source));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsNull() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = 1;
		DiceExploder underTest = new DiceExploder(source, explodeOn, explosionDepth);
		assertFalse(underTest.equals(null));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsItself() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = 1;
		DiceExploder underTest = new DiceExploder(source, explosionDepth, explodeOn);
		assertTrue(underTest.equals(underTest));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsWrongClass() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = 1;
		DiceExploder underTest = new DiceExploder(explosionDepth, source, explodeOn);
		Object other = new HomogeneousDiceGroup(source, explosionDepth);
		assertFalse(underTest.equals(other));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsUnequalPredicate() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = 1;
		DiceExploder underTest = new DiceExploder(explosionDepth, explodeOn, source);
		DiceExploder other = new DiceExploder(explosionDepth, x -> false, source);
		assertFalse(underTest.equals(other));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsUnequalSource() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = 1;
		DiceExploder underTest = new DiceExploder(explodeOn, explosionDepth, source);
		DiceExploder other = new DiceExploder(explodeOn, explosionDepth, new SingleDie(20));
		assertFalse(underTest.equals(other));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsUnequalDepth() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		final byte explosionDepth = 1;
		DiceExploder underTest = new DiceExploder(explodeOn, source, explosionDepth);
		DiceExploder other = new DiceExploder(explodeOn, source, (byte) (explosionDepth + 1));
		assertFalse(underTest.equals(other));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsIdentical() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = x -> true;
		DiceExploder underTest = new DiceExploder(explodeOn, source);
		DiceExploder other = new DiceExploder(source, explodeOn);
		assertTrue(underTest.equals(other));
		assertEquals(underTest.hashCode(), other.hashCode());
	}
}