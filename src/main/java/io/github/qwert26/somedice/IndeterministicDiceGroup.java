package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * An indeterministic dice group has its name, because the amount of dice to be
 * rolled depends on another dice roll.
 * 
 * @author <b>Qwert26</b>, Main author
 * @see MixedDiceGroup
 * @see HomogeneousDiceGroup
 */
public class IndeterministicDiceGroup implements IDie {
	private AbstractDie baseDie;
	private UnfairDie countDistribution;

	/**
	 * 
	 * @param baseDie
	 * @param countDistribution
	 */
	public IndeterministicDiceGroup(AbstractDie baseDie, UnfairDie countDistribution) {
		super();
		setBaseDie(baseDie);
		setCountDistribution(countDistribution);
	}

	/**
	 * 
	 * @param countDistribution
	 * @param baseDie
	 */
	public IndeterministicDiceGroup(UnfairDie countDistribution, AbstractDie baseDie) {
		super();
		setCountDistribution(countDistribution);
		setBaseDie(baseDie);
	}

	/**
	 * This constructor blocks the use of using two unfair die for initialization.
	 * 
	 * @deprecated Use
	 *             {@link #IndeterministicDiceGroup(UnfairDie, UnfairDie, boolean)}
	 *             instead.
	 * @throws Exception Always, as this constructor should not be used.
	 * @param first  ignored
	 * @param second ignored
	 */
	@Deprecated
	public IndeterministicDiceGroup(UnfairDie first, UnfairDie second) throws Exception {
		super();
		throw new Exception("Paramater are ambigous!");
	}

	/**
	 * This constructor allows the use of two unfair die, because of the boolean
	 * flag telling it which one is the base die.
	 * 
	 * @param first
	 * @param second
	 * @param useFirstAsBase If set to {@code true}, the parameter {@code first}
	 *                       will be used as the base. Otherwise, it will be
	 *                       {@code second}.
	 */
	public IndeterministicDiceGroup(UnfairDie first, UnfairDie second, boolean useFirstAsBase) {
		super();
		if (useFirstAsBase) {
			setBaseDie(first);
			setCountDistribution(second);
		} else {
			setBaseDie(second);
			setCountDistribution(first);
		}
	}

	/**
	 * 
	 * @param baseDie
	 * @param countDistribution
	 */
	public IndeterministicDiceGroup(AbstractDie baseDie, Compressor countDistribution) {
		super();
		setBaseDie(baseDie);
		setCountDistribution(countDistribution);
	}

	/**
	 * 
	 * @param countDistribution
	 * @param baseDie
	 */
	public IndeterministicDiceGroup(Compressor countDistribution, AbstractDie baseDie) {
		super();
		setCountDistribution(countDistribution);
		setBaseDie(baseDie);
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
	 * @param baseDie The new base die.
	 */
	public final void setBaseDie(AbstractDie baseDie) {
		this.baseDie = Objects.requireNonNull(baseDie, "A base die must be given!");
	}

	/**
	 * 
	 * @return The current distribution.
	 */
	public final UnfairDie getCountDistribution() {
		return countDistribution;
	}

	/**
	 * 
	 * @param countDistribution The new distribution.
	 */
	public final void setCountDistribution(UnfairDie countDistribution) {
		this.countDistribution = Objects.requireNonNull(countDistribution,
				"An unfair die for the count distribution must be given!");
	}

	/**
	 * 
	 * @param countDistribution The new distribution.
	 */
	public final void setCountDistribution(Compressor countDistribution) {
		this.countDistribution = Objects.requireNonNull(countDistribution, "A compressor must be given for conversion!")
				.toUnfairDie();
	}

	/**
	 * @return a <b>mixed</b> mapping of individual dice results with their absolute
	 *         occurrence.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>();
		for (Map.Entry<Integer, BigInteger> valueCount : countDistribution.getData().entrySet()) {
			final int value = valueCount.getKey();
			final BigInteger factor = valueCount.getValue();
			if (value != 0) {
				HomogeneousDiceGroup temp = new HomogeneousDiceGroup(baseDie, Math.absExact(value));
				// The next line is problematic, as there might be different kind of maps but
				// having the same content!
				temp.getAbsoluteFrequencies().forEach((composition, count) -> {
					ret.compute(composition, (k, v) -> {
						return count.multiply(factor).add(v == null ? BigInteger.ZERO : v);
					});
				});
			} else {
				Map<Integer, Integer> zero = Collections.singletonMap(0, 1);
				ret.compute(zero, (k, v) -> {
					return factor.add(v == null ? BigInteger.ZERO : v);
				});
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseDie == null) ? 0 : baseDie.hashCode());
		result = prime * result + ((countDistribution == null) ? 0 : countDistribution.hashCode());
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
		if (!(obj instanceof IndeterministicDiceGroup)) {
			return false;
		}
		IndeterministicDiceGroup other = (IndeterministicDiceGroup) obj;
		if (baseDie == null) {
			if (other.baseDie != null) {
				return false;
			}
		} else if (!baseDie.equals(other.baseDie)) {
			return false;
		}
		if (countDistribution == null) {
			if (other.countDistribution != null) {
				return false;
			}
		} else if (!countDistribution.equals(other.countDistribution)) {
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
		builder.append("IndeterministicDiceGroup [baseDie=");
		builder.append(baseDie);
		builder.append(", countDistribution=");
		builder.append(countDistribution);
		builder.append("]");
		return builder.toString();
	}

}