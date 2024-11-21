package io.github.qwert26.somedice;

import java.math.BigInteger;

/**
 * A collection of various dice, ready to be used.
 * 
 * @author <b>Qwert26</b>, main author
 */
public final class DiceCollection {
	/**
	 * A die from the Warhammer 40k TTRPG "Wrath and Glory". It is basically a d6,
	 * but with the following multi-set of numbers: {0, 0, 0, 1, 1, 2}.
	 */
	public static final UnfairDie WRATH_AND_GLORY_DIE;
	/**
	 * "Wrathing" a {@link DiceCollection#WRATH_AND_GLORY_DIE} is re-rolling it in
	 * case of a failure: It is still a d6, but the re-rolling causes its multi-set
	 * to change to: {0, 0, 0, 1, 1, 1, 1, 2, 2}.
	 */
	public static final UnfairDie REROLLED_WRATH_AND_GLORY_DIE;
	/**
	 * A die containing the multi-set {1, 2, 2, 3, 3, 4}. Together with a
	 * {@link #SICHERMAN_HIGH} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2D6} exactly.
	 */
	public static final UnfairDie SICHERMAN_LOW;
	/**
	 * A die containing the set {1, 3, 4, 5, 6, 8}. Together with a
	 * {@link #SICHERMAN_LOW} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2D6} exactly.
	 */
	public static final UnfairDie SICHERMAN_HIGH;
	/**
	 * A "Tens D10", starting at 0 and ending at 90.
	 */
	public static final RangeDie DICE_0_TO_90_IN_10 = new RangeDie(0, 91, 10);
	/**
	 * A "Tens D10", starting at 10 and ending at 100.
	 */
	public static final RangeDie DICE_10_TO_100_IN_10 = new RangeDie(10, 101, 10);
	static {
		WRATH_AND_GLORY_DIE = new UnfairDie();
		WRATH_AND_GLORY_DIE.getData().put(0, BigInteger.valueOf(3));
		WRATH_AND_GLORY_DIE.getData().put(1, BigInteger.TWO);
		WRATH_AND_GLORY_DIE.getData().put(2, BigInteger.ONE);
		REROLLED_WRATH_AND_GLORY_DIE = new UnfairDie();
		REROLLED_WRATH_AND_GLORY_DIE.getData().put(0, BigInteger.valueOf(3));
		REROLLED_WRATH_AND_GLORY_DIE.getData().put(1, BigInteger.valueOf(4));
		REROLLED_WRATH_AND_GLORY_DIE.getData().put(2, BigInteger.TWO);
		SICHERMAN_LOW = new UnfairDie();
		SICHERMAN_LOW.getData().put(1, BigInteger.ONE);
		SICHERMAN_LOW.getData().put(2, BigInteger.TWO);
		SICHERMAN_LOW.getData().put(3, BigInteger.TWO);
		SICHERMAN_LOW.getData().put(4, BigInteger.ONE);
		SICHERMAN_HIGH = new UnfairDie();
		SICHERMAN_HIGH.getData().put(1, BigInteger.ONE);
		SICHERMAN_HIGH.getData().put(3, BigInteger.ONE);
		SICHERMAN_HIGH.getData().put(4, BigInteger.ONE);
		SICHERMAN_HIGH.getData().put(5, BigInteger.ONE);
		SICHERMAN_HIGH.getData().put(6, BigInteger.ONE);
		SICHERMAN_HIGH.getData().put(8, BigInteger.ONE);
	}

	/**
	 * No instances allowed.
	 * 
	 * @throws UnsupportedOperationException Always.
	 */
	private DiceCollection() {
		super();
		throw new UnsupportedOperationException("No instances!");
	}
}
