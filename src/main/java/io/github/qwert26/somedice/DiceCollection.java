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
	 * A die containing the multi-set {1, 2, 2, 3}. Together with a
	 * {@link #D4_SICHERMAN_HIGH} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2d4} exactly.
	 */
	public static final UnfairDie D4_SICHERMAN_LOW;
	/**
	 * A die containing the multi-set {1, 3, 3, 5}. Together with a
	 * {@link #D4_SICHERMAN_LOW} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2d4} exactly.
	 */
	public static final UnfairDie D4_SICHERMAN_HIGH;
	/**
	 * A die containing the multi-set {1, 2, 2, 3, 3, 4}. Together with a
	 * {@link #D6_SICHERMAN_HIGH} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2d6} exactly.
	 */
	public static final UnfairDie D6_SICHERMAN_LOW;
	/**
	 * A die containing the set {1, 3, 4, 5, 6, 8}. Together with a
	 * {@link #D6_SICHERMAN_LOW} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2d6} exactly.
	 */
	public static final UnfairDie D6_SICHERMAN_HIGH;
	/**
	 * A die containing the multi-set {1, 2, 2, 3, 3, 3, 4, 4, 5}. Together with a
	 * {@link #D9_SICHERMAN_HIGH} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2d9} exactly.
	 */
	public static final UnfairDie D9_SICHERMAN_LOW;
	/**
	 * A die containing the multi-set {1, 4, 4, 7, 7, 7, 10, 10, 13}. Together with
	 * a {@link #D9_SICHERMAN_LOW} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2d9} exactly.
	 */
	public static final UnfairDie D9_SICHERMAN_HIGH;
	/**
	 * A die containing the multi-set {1, 2, 2, 3, 3, 4, 4, 5, 5, 6}. Together with
	 * a {@link #D10_SICHERMAN_HIGH} in a {@link MixedDiceGroup}, they recreated the
	 * resulting distribution of {@code 2d10} exactly.
	 */
	public static final UnfairDie D10_SICHERMAN_LOW;
	/**
	 * A die containing the set {1, 3, 5, 6, 7, 8, 9, 10, 12, 14}. Together
	 * with a {@link #D10_SICHERMAN_LOW} in a {@link MixedDiceGroup}, they recreated
	 * the resulting distribution of {@code 2d10} exactly.
	 */
	public static final UnfairDie D10_SICHERMAN_HIGH;
	/**
	 * A "Tens D10", starting at 0 and ending at 90.
	 */
	public static final RangeDie DICE_0_TO_90_IN_10 = new RangeDie(0, 91, 10);
	/**
	 * A "Tens D10", starting at 10 and ending at 100.
	 */
	public static final RangeDie DICE_10_TO_100_IN_10 = new RangeDie(10, 101, 10);
	static {
		final BigInteger THREE = BigInteger.valueOf(3);
		final BigInteger FOUR = BigInteger.valueOf(4);
		WRATH_AND_GLORY_DIE = new UnfairDie();
		WRATH_AND_GLORY_DIE.getData().put(0, THREE);
		WRATH_AND_GLORY_DIE.getData().put(1, BigInteger.TWO);
		WRATH_AND_GLORY_DIE.getData().put(2, BigInteger.ONE);
		REROLLED_WRATH_AND_GLORY_DIE = new UnfairDie();
		REROLLED_WRATH_AND_GLORY_DIE.getData().put(0, THREE);
		REROLLED_WRATH_AND_GLORY_DIE.getData().put(1, FOUR);
		REROLLED_WRATH_AND_GLORY_DIE.getData().put(2, BigInteger.TWO);
		// Sicherman D4-Paar
		D4_SICHERMAN_LOW = new UnfairDie();
		D4_SICHERMAN_LOW.getData().put(1, BigInteger.ONE);
		D4_SICHERMAN_LOW.getData().put(2, BigInteger.TWO);
		D4_SICHERMAN_LOW.getData().put(3, BigInteger.ONE);
		D4_SICHERMAN_HIGH = new UnfairDie();
		D4_SICHERMAN_HIGH.getData().put(1, BigInteger.ONE);
		D4_SICHERMAN_HIGH.getData().put(3, BigInteger.TWO);
		D4_SICHERMAN_HIGH.getData().put(5, BigInteger.ONE);
		// Sicherman D6-Paar
		D6_SICHERMAN_LOW = new UnfairDie();
		D6_SICHERMAN_LOW.getData().put(1, BigInteger.ONE);
		D6_SICHERMAN_LOW.getData().put(2, BigInteger.TWO);
		D6_SICHERMAN_LOW.getData().put(3, BigInteger.TWO);
		D6_SICHERMAN_LOW.getData().put(4, BigInteger.ONE);
		D6_SICHERMAN_HIGH = new UnfairDie();
		D6_SICHERMAN_HIGH.getData().put(1, BigInteger.ONE);
		D6_SICHERMAN_HIGH.getData().put(3, BigInteger.ONE);
		D6_SICHERMAN_HIGH.getData().put(4, BigInteger.ONE);
		D6_SICHERMAN_HIGH.getData().put(5, BigInteger.ONE);
		D6_SICHERMAN_HIGH.getData().put(6, BigInteger.ONE);
		D6_SICHERMAN_HIGH.getData().put(8, BigInteger.ONE);
		// Sicherman D9-Paar
		D9_SICHERMAN_LOW = new UnfairDie();
		D9_SICHERMAN_LOW.getData().put(1, BigInteger.ONE);
		D9_SICHERMAN_LOW.getData().put(2, BigInteger.TWO);
		D9_SICHERMAN_LOW.getData().put(3, THREE);
		D9_SICHERMAN_LOW.getData().put(4, BigInteger.TWO);
		D9_SICHERMAN_LOW.getData().put(5, BigInteger.ONE);
		D9_SICHERMAN_HIGH = new UnfairDie();
		D9_SICHERMAN_HIGH.getData().put(1, BigInteger.ONE);
		D9_SICHERMAN_HIGH.getData().put(4, BigInteger.TWO);
		D9_SICHERMAN_HIGH.getData().put(7, THREE);
		D9_SICHERMAN_HIGH.getData().put(10, BigInteger.TWO);
		D9_SICHERMAN_HIGH.getData().put(13, BigInteger.ONE);
		// Sicherman D10-Paar
		D10_SICHERMAN_LOW = new UnfairDie();
		D10_SICHERMAN_LOW.getData().put(1, BigInteger.ONE);
		D10_SICHERMAN_LOW.getData().put(2, BigInteger.TWO);
		D10_SICHERMAN_LOW.getData().put(3, BigInteger.TWO);
		D10_SICHERMAN_LOW.getData().put(4, BigInteger.TWO);
		D10_SICHERMAN_LOW.getData().put(5, BigInteger.TWO);
		D10_SICHERMAN_LOW.getData().put(6, BigInteger.ONE);
		D10_SICHERMAN_HIGH = new UnfairDie();
		D10_SICHERMAN_HIGH.getData().put(1, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(3, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(5, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(6, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(7, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(8, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(9, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(10, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(12, BigInteger.ONE);
		D10_SICHERMAN_HIGH.getData().put(14, BigInteger.ONE);
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
