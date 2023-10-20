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
