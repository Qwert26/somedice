package io.github.qwert26.somedice.idg;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * @author <b>Qwert26</b>, main author
 */
@DisplayName("TestIDGNormalCases")
public class TestNormalCases extends TestIndeterministicDiceGroup {
	@Test
	void checkUsingCompressor() {
		AbstractDie base = new SingleDie(4, false);
		Compressor dist = new Compressor(new HomogeneousDiceGroup(new SingleDie(10), 3));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		assertEquals(test.getCountDistribution().getAbsoluteFrequencies(), dist.getAbsoluteFrequencies());
		test = new IndeterministicDiceGroup(dist, base);
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

	@Test
	void checkEqualsConstructorWithBoolean() {
		UnfairDie first = DiceCollection.WRATH_AND_GLORY_DIE;
		UnfairDie second = new UnfairDie(FudgeDie.INSTANCE);
		assumeFalse(first.equals(second));
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(first, second, true);
		IndeterministicDiceGroup other = new IndeterministicDiceGroup(first, second, false);
		assertFalse(test.equals(other));
	}

	@Test
	void checkAbsoluteFrequenciesWithZeroes() {
		UnfairDie only = DiceCollection.WRATH_AND_GLORY_DIE;
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(only, only, true);
		Map<Map<Integer, Integer>, BigInteger> result = assertDoesNotThrow(() -> test.getAbsoluteFrequencies());
		assertNotNull(result);
		assertFalse(result.isEmpty());
	}
}