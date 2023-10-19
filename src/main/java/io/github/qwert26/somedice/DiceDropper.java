package io.github.qwert26.somedice;

import java.util.*;

/**
 * Drops dice from a roll. It can drop any number of low and high rolls, as long
 * as there are dice still left over to create a frequency mapping. For keeping
 * dice instead, use the <code>DiceKeeper</code>.
 * 
 * @author Qwert26
 * @see DiceKeeper
 */
public class DiceDropper implements IDie {
	/**
	 * The source to drop dice from, can never be <code>null</code> or
	 * <code>this</code>.
	 */
	private final IDie source;
	/**
	 * Amount of lowest values to drop.
	 */
	private int dropLowest;
	/**
	 * Amount of highest values to drop.
	 */
	private int dropHighest;

	/**
	 * Creates a new <code>DiceDropper</code>, which does not drop any dice at all.
	 * To start dropping, call its setter methods.
	 * 
	 * @see #setDropHighest(int)
	 * @see #setDropLowest(int)
	 * @param source The source to filter, it must not be <code>null</code>.
	 * @throws NullPointerException if the source is <code>null</code>.
	 */
	public DiceDropper(IDie source) {
		this.source = Objects.requireNonNull(source, "A source must be given");
		dropHighest = dropLowest = 0;
	}

	/**
	 * 
	 * @return The current amount of low values to drop.
	 */
	public final int getDropLowest() {
		return dropLowest;
	}

	/**
	 * 
	 * @param dropLowest The new amount of low values to drop.
	 * @throws IllegalArgumentException if the parameter is negative.
	 */
	public final void setDropLowest(int dropLowest) {
		if (dropLowest < 0) {
			throw new IllegalArgumentException("Can not drop a negative amount of low numbers!");
		}
		this.dropLowest = dropLowest;
	}

	/**
	 * 
	 * @return The current amount of high values to drop.
	 */
	public final int getDropHighest() {
		return dropHighest;
	}

	/**
	 * 
	 * @param dropHighest The current amount of high values to drop.
	 * @throws IllegalArgumentException if the parameter is negative.
	 */
	public final void setDropHighest(int dropHighest) {
		if (dropHighest < 0) {
			throw new IllegalArgumentException("Can not drop a negative amount of high numbers!");
		}
		this.dropHighest = dropHighest;
	}

	/**
	 * 
	 * @return The current source.
	 */
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
			ret.compute(nextKey, (k, v) -> entry.getValue() + (v == null ? 0 : v));
		}
		return ret;
	}
}