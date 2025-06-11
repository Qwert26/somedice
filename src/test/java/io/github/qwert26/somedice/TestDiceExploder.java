package io.github.qwert26.somedice;

import java.util.function.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

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
		final IntPredicate explodeOn = _ -> true;
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
		final IntPredicate explodeOn = _ -> true;
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
		final IntPredicate explodeOn = _ -> true;
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
		final IntPredicate explodeOn = _ -> true;
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
		final IntPredicate explodeOn = _ -> true;
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
		final IntPredicate explodeOn = _ -> true;
		final byte explosionDepth = 1;
		DiceExploder underTest = new DiceExploder(explosionDepth, explodeOn, source);
		DiceExploder other = new DiceExploder(explosionDepth, _ -> false, source);
		assertFalse(underTest.equals(other));
	}

	/**
	 * 
	 */
	@Test
	void checkEqualsUnequalSource() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = _ -> true;
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
		final IntPredicate explodeOn = _ -> true;
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
		final IntPredicate explodeOn = _ -> true;
		DiceExploder underTest = new DiceExploder(explodeOn, source);
		DiceExploder other = new DiceExploder(source, explodeOn);
		assertTrue(underTest.equals(other));
		assertEquals(underTest.hashCode(), other.hashCode());
	}

	/**
	 * 
	 */
	@Test
	void keepPredicateThroughErrorCase() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = _ -> true;
		final byte explosionDepth = 4;
		DiceExploder underTest = new DiceExploder(explodeOn, source, explosionDepth);
		assumeTrue(underTest.getExplodeOn() == explodeOn);
		assertThrows(NullPointerException.class, () -> underTest.setExplodeOn(null));
		assertEquals(explodeOn, underTest.getExplodeOn());
	}

	/**
	 * 
	 */
	@Test
	void keepSourceThroughErrorCaseNull() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = _ -> true;
		final byte explosionDepth = 4;
		DiceExploder underTest = new DiceExploder(explodeOn, source, explosionDepth);
		assumeTrue(underTest.getSource() == source);
		assertThrows(NullPointerException.class, () -> underTest.setSource((AbstractDie) null));
		assertEquals(source, underTest.getSource());
		assertThrows(NullPointerException.class, () -> underTest.setSource((IDie) null));
		assertEquals(source, underTest.getSource());
	}

	/**
	 * 
	 */
	@Test
	void keepSourceThroughErrorCaseWrongClass() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = _ -> true;
		final byte explosionDepth = 4;
		DiceExploder underTest = new DiceExploder(explodeOn, source, explosionDepth);
		assumeTrue(underTest.getSource() == source);
		assertThrows(IllegalArgumentException.class, () -> underTest.setSource(new HomogeneousDiceGroup(source)));
		assertEquals(source, underTest.getSource());
	}

	/**
	 * 
	 */
	@Test
	void keepDepthThroughErrorCase() {
		final AbstractDie source = new SingleDie(12);
		final IntPredicate explodeOn = _ -> true;
		final byte explosionDepth = 4;
		DiceExploder underTest = new DiceExploder(explodeOn, source, explosionDepth);
		assumeTrue(underTest.getExplosionDepth() == explosionDepth);
		assertThrows(IllegalArgumentException.class, () -> underTest.setExplosionDepth((byte) -5));
		assertEquals(explosionDepth, underTest.getExplosionDepth());
	}

	@Test
	@Disabled
	void checkResult() {

	}
}