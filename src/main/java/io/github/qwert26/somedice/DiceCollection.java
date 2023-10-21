package io.github.qwert26.somedice;

/**
 * A collection of various dice, ready to be used.
 * 
 * @author Qwert26
 */
public final class DiceCollection {
	/**
	 * A die from the Warhammer 40k TTRPG "Wrath and Glory".
	 */
	public static final UnfairDie WRATH_AND_GLORY_DIE;
	public static final RangeDie DICE_0_TO_90_IN_10 = new RangeDie(0, 91, 10);
	public static final RangeDie DICE_10_TO_100_IN_10 = new RangeDie(10, 101, 10);
	static {
		WRATH_AND_GLORY_DIE = new UnfairDie();
		WRATH_AND_GLORY_DIE.getData().put(0, 3L);
		WRATH_AND_GLORY_DIE.getData().put(1, 2L);
		WRATH_AND_GLORY_DIE.getData().put(2, 1L);
	}

	/**
	 * 
	 */
	private DiceCollection() {
		super();
	}
}
