package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * A die which produces numbers from an interval, usually with a step size of
 * one, but other step sizes are also possible.
 * 
 * @author <b>Qwert26</b>, main author
 */
public final class RangeDie extends AbstractDie {
	/**
	 * The start value, it is inclusive.
	 */
	private int start;
	/**
	 * The end value, it is exclusive.
	 */
	private int end;
	/**
	 * The step size, it is always greater than zero.
	 */
	private int step;

	/**
	 * Creates a new {@code RangeDie}.
	 * 
	 * @param start The start value, it is inclusive
	 * @param end   The end value, it is exclusive.
	 * @throws IllegalArgumentException if start is greater than or equal to end.
	 */
	public RangeDie(int start, int end) {
		super();
		setRange(start, end);
		step = 1;
	}

	/**
	 * Creates a new {@code RangeDie}.
	 * 
	 * @param start The start value, it is inclusive
	 * @param end   The end value, it is exclusive.
	 * @param step  The step size, which must be positive.
	 * @throws IllegalArgumentException if start is greater than or equal to end or
	 *                                  step is not positive.
	 */
	public RangeDie(int start, int end, int step) {
		super();
		setRange(start, end);
		setStep(step);
	}

	/**
	 * 
	 * @return the current start value.
	 */
	public final int getStart() {
		return start;
	}

	/**
	 * 
	 * @param start the new start value of the range.
	 * @throws IllegalArgumentException if the new start value is greater than or
	 *                                  equal to the current end value.
	 */
	public final void setStart(int start) {
		if (start >= end) {
			throw new IllegalArgumentException("start can not be greater than end.");
		}
		this.start = start;
	}

	/**
	 * 
	 * @return the current end value.
	 */
	public final int getEnd() {
		return end;
	}

	/**
	 * 
	 * @param end The new end value of the range.
	 * @throws IllegalArgumentException if the new end value is less than or equal
	 *                                  to the current start value.
	 */
	public final void setEnd(int end) {
		if (start >= end) {
			throw new IllegalArgumentException("start can not be greater than end.");
		}
		this.end = end;
	}

	/**
	 * 
	 * @param start the new start value, it must be less than the new end value.
	 * @param end   the new end value, it must be greater than the new start value.
	 * @throws IllegalArgumentException if the new range is not allowed.
	 */
	public final void setRange(int start, int end) {
		if (start < end) {
			this.start = start;
			this.end = end;
		} else {
			throw new IllegalArgumentException("start can not be greater than end.");
		}
	}

	/**
	 * 
	 * @return the current step size.
	 */
	public final int getStep() {
		return step;
	}

	/**
	 * 
	 * @param step
	 * @throws IllegalArgumentException if steps are not positive.
	 */
	public final void setStep(int step) {
		if (step <= 0) {
			throw new IllegalArgumentException("steps must be positive.");
		}
		this.step = step;
	}

	/**
	 * Implements the default rule for exploding
	 * 
	 * @param value
	 * @return <code>true</code>, if the rolled number equals its maximum, <b>not
	 *         the end-value<b>.
	 * @see DiceExploder
	 */
	public final boolean explodesOn(int value) {
		int diff = end - start;
		if ((diff % step) == 0) {
			diff -= step;
		} else {
			diff -= diff % step;
		}
		return value == (start + diff);
	}

	/**
	 * @implNote Uses singleton-maps as its keys, as those are unmodifiable.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>(getDistinctValues(),
				1.0f);
		for (int value = start; value < end; value += step) {
			ret.put(Collections.singletonMap(value, 1), BigInteger.ONE);
		}
		return ret;
	}

	/**
	 * A <code>RangeDie</code> calculates its distinct values with the formula
	 * <code>ceil((end - start) / step)</code>.
	 */
	@Override
	public int getDistinctValues() {
		return (int) Math.ceil((end - start) / (float) step);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
		result = prime * result + step;
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
		if (!(obj instanceof RangeDie)) {
			return false;
		}
		RangeDie other = (RangeDie) obj;
		if (end != other.end) {
			return false;
		}
		if (start != other.start) {
			return false;
		}
		if (step != other.step) {
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
		builder.append("RangeDie [start=");
		builder.append(start);
		builder.append(", end=");
		builder.append(end);
		builder.append(", step=");
		builder.append(step);
		builder.append("]");
		return builder.toString();
	}
}
