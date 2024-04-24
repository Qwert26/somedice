package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

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
	 * 
	 * @apiNote Never <code>null</code>.
	 */
	private AbstractDie baseDie;
	/**
	 * Amount of identical dice in this group.
	 */
	private int count;

	/**
	 * Creates a new homogenous dice group consisting of a single base die.
	 * 
	 * @param baseDie The dice to use, it is not replicated.
	 * @throws NullPointerException If the base die is <code>null</code>.
	 */
	public HomogenousDiceGroup(AbstractDie baseDie) {
		this(baseDie, 1);
	}

	/**
	 * Creates a new homogenous dice group consisting of the given base die, which
	 * is virtually replicated by an indicated amount.
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
	 * Creates a new homogeneous dice group consisting of the given base die, which
	 * is virtually replicated by an indicated amount.
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
	 * @throws IllegalArgumentException If count is not positive.
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
		result = prime * result + baseDie.hashCode();
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
		if (!baseDie.equals(other.baseDie)) {
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
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		int[] primitiveKeys = new int[baseDie.getDistinctValues()];
		BigInteger[] primitiveCounts = new BigInteger[primitiveKeys.length];
		int[] indexGroups = new int[primitiveCounts.length];
		int masterIndex = 0;
		for (Map.Entry<Map<Integer, Integer>, BigInteger> baseEntry : baseDie.getAbsoluteFrequencies().entrySet()) {
			primitiveKeys[masterIndex] = baseEntry.getKey().entrySet().iterator().next().getKey();
			primitiveCounts[masterIndex++] = baseEntry.getValue();
		}
		int[] indices = new int[count];
		Arrays.fill(indices, 0);
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>(
				primitiveKeys.length, count);
		BigInteger nextValue;
		infinity: while (true) {
			masterIndex = 0;
			final Map<Integer, Integer> nextKey = new TreeMap<Integer, Integer>();
			nextValue = BigInteger.ONE;
			Arrays.fill(indexGroups, 0);
			for (int subIndex : indices) {
				nextKey.compute(primitiveKeys[subIndex], (k, v) -> {
					if (v == null) {
						return 1;
					} else {
						return v + 1;
					}
				});
				nextValue = nextValue.multiply(primitiveCounts[subIndex]);
			}
			// At this point we have the raw value
			for (int subIndex : indices) {
				indexGroups[subIndex]++;
			}
			nextValue = nextValue.multiply(Utils.multinomialComplete(count, indexGroups));
			ret.put(nextKey, nextValue);
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
}