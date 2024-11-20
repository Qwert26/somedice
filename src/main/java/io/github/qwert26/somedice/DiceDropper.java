package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * Drops dice from a roll. It can drop any number of low and high rolls, as long
 * as there are dice still left over to create a frequency mapping. For keeping
 * dice instead, use the {@link DiceKeeper}.
 * 
 * @author <b>Qwert26</b>, main author
 * @see DiceKeeper
 */
public class DiceDropper implements IDie, IRequiresSource {
	/**
	 * The source to drop dice from, can never be <code>null</code>.
	 */
	private IDie source;
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
		setSource(source);
		dropHighest = dropLowest = 0;
	}

	/**
	 * Creates a new <code>DiceDropper</code>, which drops the specified high and
	 * low amount of dice rolls.
	 * 
	 * @param source      The source to filter, it must not be <code>null</code>.
	 * @param dropLowest  The amount of low rolls to drop, it must not be negative.
	 * @param dropHighest The amount of high rolls to drop, it must not be negative.
	 * @throws NullPointerException     if the source is <code>null</code>.
	 * @throws IllegalArgumentException if the amount of dice to drop is negative.
	 */
	public DiceDropper(IDie source, int dropLowest, int dropHighest) {
		setSource(source);
		setDropHighest(dropHighest);
		setDropLowest(dropLowest);
	}

	/**
	 * Creates a new <code>DiceDropper</code>, which drops the specified high and
	 * low amount of dice rolls.
	 * 
	 * @param dropLowest  The amount of low rolls to drop, it must not be negative.
	 * @param dropHighest The amount of high rolls to drop, it must not be negative.
	 * @param source      The source to filter, it must not be <code>null</code>.
	 * @throws NullPointerException     if the source is <code>null</code>.
	 * @throws IllegalArgumentException if the amount of dice to drop is negative.
	 */
	public DiceDropper(int dropLowest, int dropHighest, IDie source) {
		this(source, dropLowest, dropHighest);
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
	 * 
	 * @param source The source from which dice are to be dropped.
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
	 * @throws IllegalStateException if the source did not "rolled enough dice" and
	 *                               dropping values resulted in an empty key.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, BigInteger> base = source.getAbsoluteFrequencies();
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>(base.size(), 1.0f);
		for (Map.Entry<Map<Integer, Integer>, BigInteger> entry : base.entrySet()) {
			// It is important that we clone the mapping here!
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
			ret.compute(nextKey, (k, v) -> entry.getValue().add(v == null ? BigInteger.ZERO : v));
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dropHighest;
		result = prime * result + dropLowest;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DiceDropper)) {
			return false;
		}
		DiceDropper other = (DiceDropper) obj;
		if (dropHighest != other.dropHighest) {
			return false;
		}
		if (dropLowest != other.dropLowest) {
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiceDropper [source=");
		builder.append(source);
		builder.append(", dropLowest=");
		builder.append(dropLowest);
		builder.append(", dropHighest=");
		builder.append(dropHighest);
		builder.append("]");
		return builder.toString();
	}
}