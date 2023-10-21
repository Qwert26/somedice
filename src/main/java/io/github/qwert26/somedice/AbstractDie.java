package io.github.qwert26.somedice;

/**
 * An abstract die, as the code makes certain assumptions about the internal
 * workings of its subclasses, it is sealed.
 * 
 * @see SingleDie
 * @see FudgeDie
 * @see UnfairDie
 * @see RangeDie
 * 
 * @author Qwert26
 */
public abstract sealed class AbstractDie implements IDie permits SingleDie, FudgeDie, UnfairDie, RangeDie {
	/**
	 * A new abstract die.
	 */
	public AbstractDie() {
		super();
	}

	/**
	 * 
	 * @return The amount of different values a die has.
	 */
	public abstract int getDistinctValues();
}
