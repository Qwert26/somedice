package io.github.qwert26.somedice;

import java.util.*;

/**
 * A Die designed intentionally to be unfair. This can be user designed or the
 * result of a chain of manipulating actions.
 * 
 * @author Qwert26
 */
public final class UnfairDie extends AbstractDie {
	/**
	 * The internal mapping from values to frequencies.
	 */
	private final Map<Integer, Long> data;

	/**
	 * Creates a new empty die, ready to be filled with data.
	 */
	public UnfairDie() {
		super();
		data = new TreeMap<Integer, Long>();
	}

	/**
	 * 
	 * @return The internal mapping of distinct values to absolute frequencies.
	 */
	public final Map<Integer, Long> getData() {
		return data;
	}

	@Override
	public Map<Map<Integer, Integer>, Long> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, Long> ret = new HashMap<Map<Integer, Integer>, Long>(data.size(), 1.0f);
		for (Map.Entry<Integer, Long> entry : data.entrySet()) {
			ret.put(Collections.singletonMap(entry.getKey(), 1), entry.getValue());
		}
		return ret;
	}

	@Override
	public int getDistinctValues() {
		return data.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + data.hashCode();
		return result;
	}

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnfairDie [data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
}