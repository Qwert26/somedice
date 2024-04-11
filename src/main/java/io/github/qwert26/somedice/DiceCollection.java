package io.github.qwert26.somedice;

import java.math.BigInteger;

/**
 * A collection of various dice, ready to be used.
 * 
 * @author Qwert26
 */
public final class DiceCollection {
	/**
	 * A die from the Warhammer 40k TTRPG "Wrath and Glory". It is basically a d6,
	 * but with the following multi-set of numbers: {0, 0, 0, 1, 1, 2}.
	 */
	public static final UnfairDie WRATH_AND_GLORY_DIE;
	/**
	 * A "Tens D10", starting at 0.
	 */
	public static final RangeDie DICE_0_TO_90_IN_10 = new RangeDie(0, 91, 10);
	/**
	 * A "Tens D10", starting at 10.
	 */
	public static final RangeDie DICE_10_TO_100_IN_10 = new RangeDie(10, 101, 10);
	static {
		WRATH_AND_GLORY_DIE = new UnfairDie();
		WRATH_AND_GLORY_DIE.getData().put(0, BigInteger.valueOf(3));
		WRATH_AND_GLORY_DIE.getData().put(1, BigInteger.TWO);
		WRATH_AND_GLORY_DIE.getData().put(2, BigInteger.ONE);
	}

	/**
	 * 
	 */
	private DiceCollection() {
		super();
	}
}
