package io.github.qwert26.somedice;

import java.util.function.IntPredicate;

/**
 * An abstract die, as the code makes certain assumptions about the internal
 * workings of its subclasses, it is <code>sealed</code>.
 * 
 * @author <b>Qwert26</b>, Main-author
 * @see SingleDie
 * @see FudgeDie
 * @see UnfairDie
 * @see RangeDie
 */
public abstract sealed class AbstractDie implements IDie permits SingleDie, FudgeDie, UnfairDie, RangeDie {
	/**
	 * A new abstract die.
	 */
	public AbstractDie() {
		super();
	}

	/**
	 * A support method used by some other classes.
	 * 
	 * @return The amount of different values a die has.
	 */
	public abstract int getDistinctValues();

	/**
	 * A support method usually used as an {@link IntPredicate}, when a dice is used
	 * as a source for a {@code DiceExploder}.
	 * 
	 * @param value The currently thrown value of the dice.
	 * @return <code>true</code>, if the dice should explode on the given number.
	 *         Default implementation is, that a dice explodes on its highest
	 *         number.
	 * @see DiceExploder
	 */
	public abstract boolean explodesOn(int value);
}