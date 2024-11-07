package io.github.qwert26.somedice;

import java.util.function.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 
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
}