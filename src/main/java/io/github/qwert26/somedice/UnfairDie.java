package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

/**
 * A Die designed intentionally to be unfair. This can be user designed or the
 * result of a chain of manipulating actions. It can also mimic any other die.
 * 
 * @author <b>Qwert26</b>, main author
 * @see Compressor#toUnfairDie()
 */
public final class UnfairDie extends AbstractDie {
	/**
	 * The internal mapping from values to frequencies.
	 */
	private final Map<Integer, BigInteger> data;

	/**
	 * Creates a new empty die, ready to be filled with data.
	 * 
	 * @implNote Uses a {@link TreeMap} for its data-field.
	 */
	public UnfairDie() {
		this(TreeMap::new);
	}

	/**
	 * Creates a new die with the data-field filled by the returned value of the
	 * given supplier.
	 * 
	 * @param dataCreator
	 * @throws IllegalArgumentException If the given {@link Supplier} was
	 *                                  <code>null</code>.
	 * @throws NullPointerException     If a {@code null}-value was given by the
	 *                                  non-null {@link Supplier}.
	 */
	public UnfairDie(Supplier<Map<Integer, BigInteger>> dataCreator) {
		super();
		if (dataCreator == null) {
			throw new IllegalArgumentException("DataCreator was null.");
		}
		Map<Integer, BigInteger> temp = dataCreator.get();
		if (temp == null) {
			throw new NullPointerException("Supplier returned null via its get()-method.");
		}
		data = temp;
	}

	/**
	 * Creates a new {@code UnfairDie}, filled with data matching another already
	 * made Die.
	 * 
	 * @implNote Uses a {@link TreeMap} for its data-field.
	 * @param source The die to copy from, if it is {@code null}, data must be given
	 *               "manually".
	 */
	public UnfairDie(AbstractDie source) {
		this(TreeMap::new);
		if (source != null) {
			switch (source) {
			case UnfairDie ud -> {
				data.putAll(ud.data);
			}
			case SingleDie sd -> {
				for (int i = 1; i < sd.getMaximum(); i++) {
					data.put(i, BigInteger.ONE);
				}
				if (sd.isStartAt0()) {
					data.put(0, BigInteger.ONE);
				} else {
					data.put(sd.getMaximum(), BigInteger.ONE);
				}
			}
			case RangeDie rd -> {
				for (int value = rd.getStart(); value < rd.getEnd(); value += rd.getStep()) {
					data.put(value, BigInteger.ONE);
				}
			}
			case FudgeDie _ -> {
				data.put(-1, BigInteger.ONE);
				data.put(0, BigInteger.ONE);
				data.put(1, BigInteger.ONE);
			}
			}
		}
	}

	/**
	 * @implSpec Returns the actual object, as {@code UnfairDie}s are expected to
	 *           operate via side-effects.
	 * @return The internal mapping of distinct values to absolute frequencies.
	 */
	public final Map<Integer, BigInteger> getData() {
		return data;
	}

	/**
	 * Implements the default rule for exploding. An empty UnfairDie however is
	 * unable to explode.
	 * 
	 * @param value
	 * @return <code>true</code>, if the rolled number is equal to the maximum
	 *         number in the backing map.
	 * @see DiceExploder
	 */
	public final boolean explodesOn(int value) {
		OptionalInt result = data.keySet().stream().mapToInt(Integer::intValue).max();
		if (result.isEmpty()) {
			return false;
		} else {
			return value == result.getAsInt();
		}
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