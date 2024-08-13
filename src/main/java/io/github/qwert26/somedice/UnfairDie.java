package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * A Die designed intentionally to be unfair. This can be user designed or the
 * result of a chain of manipulating actions.
 * 
 * @author Qwert26
 * @see Compressor#toUnfairDie()
 */
public final class UnfairDie extends AbstractDie {
	/**
	 * The internal mapping from values to frequencies.
	 */
	private final Map<Integer, BigInteger> data;

	/**
	 * Creates a new empty die, ready to be filled with data.
	 */
	public UnfairDie() {
		super();
		data = new TreeMap<Integer, BigInteger>();
	}

	/**
	 * @implSpec Returns the actual object, as {@code UnfairDie} are expected to
	 *           operate via side-effects.
	 * @return The internal mapping of distinct values to absolute frequencies.
	 */
	public final Map<Integer, BigInteger> getData() {
		return data;
	}

	/**
	 * @implNote Uses singleton-maps as its keys, as those are unmodifiable.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>(data.size(), 1.0f);
		for (Map.Entry<Integer, BigInteger> entry : data.entrySet()) {
			ret.put(Collections.singletonMap(entry.getKey(), 1), entry.getValue());
		}
		return ret;
	}

	/**
	 * Delegates to the <code>size()</code>-method of the underlying map.
	 * 
	 * @see Map#size()
	 */
	@Override
	public int getDistinctValues() {
		return data.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + data.hashCode();
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
		if (!(obj instanceof UnfairDie)) {
			return false;
		}
		UnfairDie other = (UnfairDie) obj;
		if (!data.equals(other.data)) {
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
		builder.append("UnfairDie [data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
}