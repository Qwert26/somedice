package io.github.qwert26.somedice.idg;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@DisplayName("TestIDGErrorCases")
public class TestErrorCases extends TestIndeterministicDiceGroup {
	@Test
	void anyConstructorFailsOnMissingBase() {
		UnfairDie distribution = DiceCollection.WRATH_AND_GLORY_DIE;
		AbstractDie base = null;
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(base, distribution));
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(distribution, base));
	}

	@Test
	void anyConstructorFailsOnMissingUnfairDistribution() {
		UnfairDie distribution = null;
		SingleDie base = new SingleDie(6);
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(base, distribution));
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(distribution, base));
	}

	@Test
	void anyConstructorFailsOnMissingCompressorDistribution() {
		Compressor distribution = null;
		SingleDie base = new SingleDie(8);
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(base, distribution));
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(distribution, base));
	}

	/**
	 * The tested constructor should actually not be used.
	 */
	@SuppressWarnings("deprecation")
	@Test
	void constructorWithTwoUnfairsAlwaysFails() {
		final UnfairDie firstNull = null, secondNull = null;
		final UnfairDie firstNotNull = DiceCollection.WRATH_AND_GLORY_DIE, secondNotNull = firstNotNull;
		assertThrows(Exception.class, () -> new IndeterministicDiceGroup(firstNull, secondNull));
		assertThrows(Exception.class, () -> new IndeterministicDiceGroup(firstNull, secondNotNull));
		assertThrows(Exception.class, () -> new IndeterministicDiceGroup(firstNotNull, secondNull));
		assertThrows(Exception.class, () -> new IndeterministicDiceGroup(firstNotNull, secondNotNull));
	}

	@Test
	void constructorForOnlyUnfair() {
		final UnfairDie invalid = null, valid = DiceCollection.WRATH_AND_GLORY_DIE;
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(valid, invalid, true));
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(valid, invalid, false));
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(invalid, valid, true));
		assertThrows(NullPointerException.class, () -> new IndeterministicDiceGroup(invalid, valid, false));
	}

	@Test
	void baseSetterDoesNotChangeCurrentBase() {
		AbstractDie base = new SingleDie(10);
		UnfairDie dist = DiceCollection.WRATH_AND_GLORY_DIE;
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(base, dist);
		assumeTrue(test.getBaseDie() == base);
		assertThrows(NullPointerException.class, () -> test.setBaseDie(null));
		assertSame(base, test.getBaseDie());
	}

	@Test
	void distributionSetterDoesNotChangeCurrentDistribution() {
		AbstractDie base = new SingleDie(10);
		UnfairDie dist = DiceCollection.WRATH_AND_GLORY_DIE;
		IndeterministicDiceGroup test = new IndeterministicDiceGroup(dist, base);
		assumeTrue(test.getCountDistribution() == dist);
		assertThrows(NullPointerException.class, () -> test.setCountDistribution((UnfairDie) null));
		assertSame(dist, test.getCountDistribution());
		assertThrows(NullPointerException.class, () -> test.setCountDistribution((Compressor) null));
		assertSame(dist, test.getCountDistribution());
	}
}
