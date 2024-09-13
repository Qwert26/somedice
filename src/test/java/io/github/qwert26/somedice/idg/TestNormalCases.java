package io.github.qwert26.somedice.idg;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * 
 */
@DisplayName("TestIDGNormalCases")
public class TestNormalCases extends TestIndeterministicDiceGroup {
	@Test
	void checkUsingCompressor() {
		AbstractDie base = new SingleDie(4, false);
		Compressor dist = new Compressor(new HomogeneousDiceGroup(new SingleDie(10), 3));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		assertEquals(test.getCountDistribution().getAbsoluteFrequencies(), dist.getAbsoluteFrequencies());
	}

	@Test
	void checkToString() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		String result = assertDoesNotThrow(() -> test.toString());
		assertNotNull(result);
		assertTrue(result.contains(base.toString()));
		assertTrue(result.contains(dist.toString()));
	}

	@Test
	void checkHashCode() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		assertDoesNotThrow(() -> test.hashCode());
	}

	@Test
	void checkEqualsNull() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		assertFalse(test.equals(null));
	}

	@Test
	void checkEqualsItself() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		assertTrue(test.equals(test));
	}

	@Test
	void checkEqualsWrongClass() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		assertFalse(test.equals(new Object()));
	}

	@Test
	void checkEqualsWrongBase() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		IndeterministicDiceGroup other = new IndeterministicDiceGroup(new SingleDie(6, true), dist);
		assertFalse(test.equals(other));
	}

	@Test
	void checkEqualsWrongDistribution() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		IndeterministicDiceGroup other = new IndeterministicDiceGroup(base, DiceCollection.WRATH_AND_GLORY_DIE);
		assertFalse(test.equals(other));
	}

	@Test
	void checkEquals() {
		AbstractDie base = new SingleDie(4, false);
		UnfairDie dist = new UnfairDie(new SingleDie(6));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		IndeterministicDiceGroup other = new IndeterministicDiceGroup(base, dist);
		assertTrue(test.equals(other));
	}
}