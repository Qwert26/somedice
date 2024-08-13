package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * Represents a single fair die, with numbers starting either at zero or one.
 * The die is "dense", meaning it does not skip numbers.
 * 
 * @author Christian Schuerhoff
 */
public final class SingleDie extends AbstractDie {
	/**
	 * If <code>true</code> the range of numbers starts with <code>0</code>, making
	 * it an "array-die". Otherwise it starts with <code>1</code>, making it a
	 * "game-die".
	 */
	private boolean startAt0;
	/**
	 * The highest value of the die, or alternatively the amount of different
	 * numbers it can produce.
	 */
	private int maximum;

	/**
	 * Creates die that either has numbers from <code>0</code> to
	 * <code>size-1</code> or from <code>1</code> to <code>size</code>.
	 * 
	 * @param startAt0 When <code>true</code>, this die starts at zero, otherwise it
	 *                 starts at one.
	 * @param maximum  The amount of different numbers to produce, must be greater
	 *                 than 1.
	 * @throws IllegalArgumentException If the given maximum is less than two.
	 */
	public SingleDie(boolean startAt0, int maximum) {
		setMaximum(maximum);
		setStartAt0(startAt0);
	}

	/**
	 * Creates die that either has numbers from <code>0</code> to
	 * <code>size-1</code> or from <code>1</code> to <code>size</code>.
	 * 
	 * @param maximum  The amount of different numbers to produce, must be greater
	 *                 than 1.
	 * @param startAt0 When <code>true</code>, this die starts at zero, otherwise it
	 *                 starts at one.
	 * @throws IllegalArgumentException If the given maximum is less than two.
	 */
	public SingleDie(int maximum, boolean startAt0) {
		setMaximum(maximum);
		setStartAt0(startAt0);
	}

	/**
	 * Creates a "game-die", whose numbers start at <code>1</code> and end at
	 * <code>maximum</code>.
	 * 
	 * @param maximum The amount of different numbers to produce, must be greater
	 *                than 1.
	 * @throws IllegalArgumentException If the given maximum is less than two.
	 */
	public SingleDie(int maximum) {
		this(maximum, false);
	}

	/**
	 * 
	 * @return <code>true</code>, if the die is starting with a zero.
	 *         <code>false</code>, if it is starting with a one.
	 */
	public final boolean isStartAt0() {
		return startAt0;
	}

	/**
	 * 
	 * @param startAt0
	 */
	public final void setStartAt0(boolean startAt0) {
		this.startAt0 = startAt0;
	}

	/**
	 * 
	 * @return The current maximum.
	 * @see #getDistinctValues()
	 */
	public final int getMaximum() {
		return maximum;
	}

	/**
	 * 
	 * @param maximum The new maximum, which should be greater than one.
	 * @throws IllegalArgumentException if the new maximum is less than
	 *                                  <code>2</code>.
	 */
	public final void setMaximum(int maximum) {
		if (maximum < 2) {
			throw new IllegalArgumentException("The maximum must be at least 2");
		}
		this.maximum = maximum;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maximum;
		result = prime * result + (startAt0 ? 1231 : 1237);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SingleDie)) {
			return false;
		}
		SingleDie other = (SingleDie) obj;
		if (maximum != other.maximum) {
			return false;
		}
		if (startAt0 != other.startAt0) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SingleDie [startAt0=");
		builder.append(startAt0);
		builder.append(", maximum=");
		builder.append(maximum);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * All entries in the returned map have the form <code>{{X=1}=1}</code>, where X
	 * is a number from the allowed range, specified by {@link #startAt0} and
	 * {@link #maximum}.
	 * 
	 * @implNote Uses singleton-maps as its keys, as those are unmodifiable.
	 */
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		HashMap<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>(maximum, 1.0f);
		for (int i = 1; i < maximum; i++) {
			ret.put(Collections.singletonMap(i, 1), BigInteger.ONE);
		}
		if (startAt0) {
			ret.put(Collections.singletonMap(0, 1), BigInteger.ONE);
		} else {
			ret.put(Collections.singletonMap(maximum, 1), BigInteger.ONE);
		}
		return ret;
	}

	/**
	 * @see #getMaximum()
	 */
	@Override
	public int getDistinctValues() {
		return maximum;
	}
}