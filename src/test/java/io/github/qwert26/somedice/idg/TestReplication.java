package io.github.qwert26.somedice.idg;

import java.math.BigInteger;

import io.github.qwert26.somedice.*;

/**
 * Tests the {@link IndeterministicDiceGroup} to be able to replicate a
 * {@link HomogenousDiceGroup}.
 */
public class TestReplication extends TestIndeterministicDiceGroup {
	private static final UnfairDie createUnfairDice(int count) {
		UnfairDie ret = new UnfairDie();
		ret.getData().put(count, BigInteger.ONE);
		return ret;
	}

	protected AbstractDie baseDie;

	/**
	 * 
	 */
	public TestReplication(int count, AbstractDie baseDie) {
		super(createUnfairDice(count));
		this.baseDie = baseDie;
	}
}