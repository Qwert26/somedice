package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * Keeps dice from a dice roll. It can keep any number of low and high rolls, as
 * long as there is no overlap between the two. For dropping values instead, use
 * the {@link DiceDropper}.
 * 
 * @author Qwert26
 * @see DiceDropper
 */
public class DiceKeeper implements IDie, IRequiresSource {
	/**
	 * The source of the dice rolls, can never be <code>null</code>.
	 */
	private IDie source;
	/**
	 * Amount of high dice to keep.
	 */
	private int keepHighest = 0;
	/**
	 * Amount of low dice to keep.
	 */
	private int keepLowest = 0;

	/**
	 * Creates a new <code>DiceKeeper</code>, which must be configured by calling
	 * its setter methods.
	 * 
	 * @see #setKeepHighest(int)
	 * @see #setKeepLowest(int)
	 * @param source The source to keep dice rolls from, must be not null.
	 * @throws NullPointerException If the given source is null.
	 */
	public DiceKeeper(IDie source) {
		setSource(source);
	}

	/**
	 * Creates a new {@code DiceKeeper}, which is already configured.
	 * 
	 * @param source      The source to keep dice rolls from, must be not null.
	 * @param keepLowest  The amount of low rolls to keep.
	 * @param keepHighest The amount of high rolls to keep.
	 * @throws NullPointerException     If the given source is null.
	 * @throws IllegalArgumentException If either {@code keepLowest} or
	 *                                  {@code keepHighest} is negative.
	 */
	public DiceKeeper(IDie source, int keepLowest, int keepHighest) {
		setSource(source);
		setKeepHighest(keepHighest);
		setKeepLowest(keepLowest);
	}

	/**
	 * Creates a new {@code DiceKeeper}, which is already configured.
	 * 
	 * @param keepLowest  The amount of low rolls to keep.
	 * @param keepHighest The amount of high rolls to keep.
	 * @param source      The source to keep dice rolls from, must be not null.
	 * @throws NullPointerException     If the given source is null.
	 * @throws IllegalArgumentException If either {@code keepLowest} or
	 *                                  {@code keepHighest} is negative.
	 */
	public DiceKeeper(int keepLowest, int keepHighest, IDie source) {
		setSource(source);
		setKeepHighest(keepHighest);
		setKeepLowest(keepLowest);
	}

	/**
	 * 
	 * @return The current amount of high rolls to keep.
	 */
	public final int getKeepHighest() {
		return keepHighest;
	}

	/**
	 * 
	 * @param keepHighest
	 * @throws IllegalArgumentException If the parameter is negative.
	 */
	public final void setKeepHighest(int keepHighest) {
		if (keepHighest < 0) {
			throw new IllegalArgumentException("Can not keep a negative amount of high rolls!");
		}
		this.keepHighest = keepHighest;
	}

	/**
	 * 
	 * @return The current amount of low rolls to keep.
	 */
	public final int getKeepLowest() {
		return keepLowest;
	}

	/**
	 * 
	 * @param keepLowest
	 * @throws IllegalArgumentException If the parameter is negative.
	 */
	public final void setKeepLowest(int keepLowest) {
		if (keepLowest < 0) {
			throw new IllegalArgumentException("Can not keep a negative amount of high rolls!");
		}
		this.keepLowest = keepLowest;
	}

	/**
	 * 
	 * @return The source, that is being used.
	 */
	public final IDie getSource() {
		return source;
	}

	/**
	 * 
	 * @param source The source from which dice are to be kept.
	 * @throws NullPointerException     if the given source is <code>null</code>.
	 * @throws IllegalArgumentException if using the non-null parameter would result
	 *                                  in an infinite loop.
	 * @see Utils#checkForCycle(IRequiresSource)
	 */
	public final void setSource(IDie source) {
		if (source instanceof IRequiresSource future) {
			IDie oldSource = this.source;
			this.source = source;
			try {
				Utils.checkForCycle(future);
			} catch (IllegalArgumentException iae) {
				this.source = oldSource;
				throw iae;
			}
		}
		this.source = Objects.requireNonNull(source, "A source must be given!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + keepHighest;
		result = prime * result + keepLowest;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		if (!(obj instanceof DiceKeeper)) {
			return false;
		}
		DiceKeeper other = (DiceKeeper) obj;
		if (keepHighest != other.keepHighest) {
			return false;
		}
		if (keepLowest != other.keepLowest) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
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
		builder.append("DiceKeeper [source=");
		builder.append(source);
		builder.append(", keepHighest=");
		builder.append(keepHighest);
		builder.append(", keepLowest=");
		builder.append(keepLowest);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @throws IllegalStateException If too little dice can be kept.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		if (keepHighest == 0 && keepLowest == 0) {
			throw new IllegalStateException("Both ends of keeping dice rolls are zero!");
		}
		Map<Map<Integer, Integer>, BigInteger> result = source.getAbsoluteFrequencies();
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<>(result.size(), 1.0f);
		for (Map.Entry<Map<Integer, Integer>, BigInteger> resultEntry : result.entrySet()) {
			// It is important, that we clone the mapping here!
			TreeMap<Integer, Integer> keySource = new TreeMap<>(resultEntry.getKey());
			TreeMap<Integer, Integer> nextKey = new TreeMap<>();
			int keep = 0;
			for (keep = keepHighest; keep > 0;) {
				final int toBeRemoved = keep;
				Map.Entry<Integer, Integer> lastEntry = keySource.lastEntry();
				if (lastEntry == null) {
					throw new IllegalStateException("Overkept while keeping highest rolls!");
				}
				nextKey.put(lastEntry.getKey(), Math.min(keep, lastEntry.getValue()));
				keySource.compute(lastEntry.getKey(), (k, v) -> toBeRemoved >= v ? null : (v - toBeRemoved));
				// The entry is needed, so this can not be put in the for-header.
				keep -= Math.min(lastEntry.getValue(), keep);
			}
			for (keep = keepLowest; keep > 0;) {
				final int toBeRemoved = keep;
				Map.Entry<Integer, Integer> firstEntry = keySource.firstEntry();
				if (firstEntry == null) {
					throw new IllegalStateException("Overkept while keeping lowest rolls!");
				}
				nextKey.compute(firstEntry.getKey(),
						(k, v) -> Math.min(toBeRemoved, firstEntry.getValue()) + (v == null ? 0 : v));
				keySource.compute(firstEntry.getKey(), (k, v) -> toBeRemoved >= v ? null : (v - toBeRemoved));
				// The entry is needed, so this can not be put in the for-header.
				keep -= Math.min(firstEntry.getValue(), keep);
			}
			ret.compute(nextKey, (k, v) -> resultEntry.getValue().add(v == null ? BigInteger.ZERO : v));
		}
		return ret;
	}
}