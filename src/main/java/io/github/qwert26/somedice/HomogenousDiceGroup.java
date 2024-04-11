package io.github.qwert26.somedice;

import java.util.*;
import java.util.stream.IntStream;

/**
 * A homogenous dice group consist of dice of a single type. For mixed dice
 * groups, use {@link MixedDiceGroup}.
 * 
 * @author Qwert26
 * @see MixedDiceGroup
 */
public class HomogenousDiceGroup implements IDie {
	/**
	 * The base die this group consists of: It will be virtually replicated as
	 * indicated by {@link #count}.
	 */
	private AbstractDie baseDie;
	/**
	 * Amount of identical dice in this group.
	 */
	private int count;

	/**
	 * 
	 * @param baseDie
	 * @throws NullPointerException If the base die is <code>null</code>.
	 */
	public HomogenousDiceGroup(AbstractDie baseDie) {
		this(baseDie, 1);
	}

	/**
	 * 
	 * @param baseDie
	 * @param count
	 * @throws NullPointerException     If the base die is <code>null</code>.
	 * @throws IllegalArgumentException If the amount is not positive.
	 */
	public HomogenousDiceGroup(AbstractDie baseDie, int count) {
		setBaseDie(baseDie);
		setCount(count);
	}

	/**
	 * 
	 * @param count
	 * @param baseDie
	 * @throws NullPointerException     If the base die is <code>null</code>.
	 * @throws IllegalArgumentException If the amount is not positive.
	 */
	public HomogenousDiceGroup(int count, AbstractDie baseDie) {
		setBaseDie(baseDie);
		setCount(count);
	}

	/**
	 * 
	 * @return The current base die.
	 */
	public final AbstractDie getBaseDie() {
		return baseDie;
	}

	/**
	 * 
	 * @param baseDie
	 * @throws NullPointerException If the new base die is <code>null</code>.
	 */
	public final void setBaseDie(AbstractDie baseDie) {
		this.baseDie = Objects.requireNonNull(baseDie, "A base die is required!");
	}

	/**
	 * 
	 * @return The current amount of virtual dice.
	 */
	public final int getCount() {
		return count;
	}

	/**
	 * 
	 * @param count The new amount of virtual dice.
	 * @throws IllegalArgumentException if count is not positive.
	 */
	public final void setCount(int count) {
		if (count < 1) {
			throw new IllegalArgumentException("A dice group has at least one die in it!");
		}
		this.count = count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HomogenousDiceGroup [baseDie=");
		builder.append(baseDie);
		builder.append(", count=");
		builder.append(count);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseDie == null) ? 0 : baseDie.hashCode());
		result = prime * result + count;
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
		if (!(obj instanceof HomogenousDiceGroup)) {
			return false;
		}
		HomogenousDiceGroup other = (HomogenousDiceGroup) obj;
		if (baseDie == null) {
			if (other.baseDie != null) {
				return false;
			}
		} else if (!baseDie.equals(other.baseDie)) {
			return false;
		}
		if (count != other.count) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public Map<Map<Integer, Integer>, Long> getAbsoluteFrequencies() {
		int[] primitiveKeys = new int[baseDie.getDistinctValues()];
		long[] primitiveCounts = new long[primitiveKeys.length];
		int[] indexGroups = new int[primitiveCounts.length];
		int masterIndex = 0;
		for (Map.Entry<Map<Integer, Integer>, Long> baseEntry : baseDie.getAbsoluteFrequencies().entrySet()) {
			primitiveKeys[masterIndex] = baseEntry.getKey().entrySet().iterator().next().getKey();
			primitiveCounts[masterIndex++] = baseEntry.getValue();
		}
		int[] indices = new int[count];
		Arrays.fill(indices, 0);
		Map<Map<Integer, Integer>, Long> ret = new HashMap<Map<Integer, Integer>, Long>(primitiveKeys.length, count);
		long nextValue;
		// FIXME: Replace counting loop with calculation based on the multinomial
		// coefficients.
		infinity: while (true) {
			masterIndex = 0;
			final Map<Integer, Integer> nextKey = new TreeMap<Integer, Integer>();
			nextValue = 1;
			Arrays.fill(indexGroups, 0);
			for (int subIndex : indices) {
				nextKey.compute(primitiveKeys[subIndex], (k, v) -> {
					if (v == null) {
						return 1;
					} else {
						return v + 1;
					}
				});
				nextValue *= primitiveCounts[subIndex];
			}
			// At this point we have the raw value
			for (int subIndex : indices) {
				indexGroups[subIndex]++;
			}
			final long contextValue = nextValue * multinomial(count, indexGroups);
			ret.put(nextKey, contextValue);
			do {
				indices[masterIndex]++;
				if (indices[masterIndex] == primitiveKeys.length) {
					masterIndex++;
				} else {
					for (int beforeMaster = 0; beforeMaster < masterIndex; beforeMaster++) {
						indices[beforeMaster] = indices[masterIndex];
					}
					continue infinity;
				}
			} while (masterIndex < indices.length);
			break;
		}
		return ret;
	}

	private static final long multinomial(int total, int... individuals) {
		if (IntStream.of(individuals).sum() > total) {
			throw new IllegalArgumentException("Too many individual groups!");
		}
		long ret = 1L;
		for (int individual : individuals) {
			ret *= binomial(total, individual);
			total -= individual;
		}
		return ret;
	}

	private static final long binomial(int total, int group) {
		if (total < 0 || group < 0 || group > total) {
			throw new IllegalArgumentException(total + "C" + group + " is not defined!");
		} else if (group == 0 || group == total) {
			return 1L;
		} else {
			long ret = 1L;
			group = Math.min(group, total - group);
			int div = group;
			for (; total > group; total--) {
				ret *= total;
				while (div > 1 && ret % div == 0) {
					ret /= div;
					div--;
				}
			}
			while (div > 1 && ret % div == 0) {
				ret /= div;
				div--;
			}
			return ret;
		}
	}
}