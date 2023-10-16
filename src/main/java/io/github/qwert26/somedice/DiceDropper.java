package io.github.qwert26.somedice;

import java.util.*;

/**
 * Drops dice from a roll, can drop any number of low and high rolls, as long as
 * there are dice still left over to create a frequency mapping.
 * 
 * @author Qwert26
 */
public class DiceDropper implements IDie {
	private final IDie source;
	private int dropLowest;
	private int dropHighest;

	/**
	 * 
	 * @param source The source to filter, it must not be <code>null</code>.
	 * @throws NullPointerException if the source is <code>null</code>.
	 */
	public DiceDropper(IDie source) {
		this.source = Objects.requireNonNull(source, "A source must be given");
		dropHighest = dropLowest = 0;
	}

	public final int getDropLowest() {
		return dropLowest;
	}

	/**
	 * 
	 * @param dropLowest
	 * @throws IllegalArgumentException if the parameter is negative.
	 */
	public final void setDropLowest(int dropLowest) {
		if (dropLowest < 0) {
			throw new IllegalArgumentException("Can not drop a negative amount of low numbers!");
		}
		this.dropLowest = dropLowest;
	}

	public final int getDropHighest() {
		return dropHighest;
	}

	/**
	 * 
	 * @param dropHighest
	 * @throws IllegalArgumentException if the parameter is negative.
	 */
	public final void setDropHighest(int dropHighest) {
		if (dropHighest < 0) {
			throw new IllegalArgumentException("Can not drop a negative amount of high numbers!");
		}
		this.dropHighest = dropHighest;
	}

	public final IDie getSource() {
		return source;
	}

	/**
	 * @throws IllegalStateException if the source "rolled not enough dice".
	 */
	@Override
	public Map<Map<Integer, Integer>, Long> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, Long> base = source.getAbsoluteFrequencies();
		Map<Map<Integer, Integer>, Long> ret = new HashMap<Map<Integer, Integer>, Long>(base.size(), 1.0f);
		for (Map.Entry<Map<Integer, Integer>, Long> entry : base.entrySet()) {
			TreeMap<Integer, Integer> nextKey = new TreeMap<Integer, Integer>(entry.getKey());
			int drop;
			for (drop = dropLowest; drop > 0; drop--) {
				if (nextKey.size() == 0) {
					throw new IllegalStateException("Overfiltered while dropping lowest rolls!");
				}
				nextKey.compute(nextKey.firstKey(), (k, v) -> v == 1 ? null : (v - 1));
			}
			for (drop = dropHighest; drop > 0; drop--) {
				if (nextKey.size() == 0) {
					throw new IllegalStateException("Overfiltered while dropping highest rolls!");
				}
				nextKey.compute(nextKey.lastKey(), (k, v) -> v == 1 ? null : (v - 1));
			}
			if (nextKey.size() == 0) {
				throw new IllegalStateException("Overfiltered after dropping lowest and highest rolls!");
			}
			ret.compute(nextKey, (k, v) -> v == null ? entry.getValue() : (v + entry.getValue()));
		}
		return ret;
	}
}