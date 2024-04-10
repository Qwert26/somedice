package io.github.qwert26.somedice;

import java.util.*;

/**
 * An indeterministic dice group has its name, because the amount of dice to be
 * rolled depends on another dice roll.
 * 
 * @author <b>Qwert26</b>, Main author
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
	 * @return
	 */
	public final AbstractDie getBaseDie() {
		return baseDie;
	}

	/**
	 * 
	 * @param baseDie
	 */
	public final void setBaseDie(AbstractDie baseDie) {
		this.baseDie = Objects.requireNonNull(baseDie, "A base die must be given!");
	}

	/**
	 * 
	 * @return
	 */
	public final UnfairDie getCountDistribution() {
		return countDistribution;
	}

	/**
	 * 
	 * @param countDistribution
	 */
	public final void setCountDistribution(UnfairDie countDistribution) {
		this.countDistribution = Objects.requireNonNull(countDistribution,
				"An unfair die for the count distribution must be given!");
	}

	/**
	 * 
	 * @param countDistribution
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
	public Map<Map<Integer, Integer>, Long> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, Long> ret = new HashMap<Map<Integer, Integer>, Long>();
		for (Map.Entry<Integer, Long> valueCount : countDistribution.getData().entrySet()) {
			final int value = valueCount.getKey();
			final long factor = valueCount.getValue();
			if (value != 0) {
				HomogenousDiceGroup temp = new HomogenousDiceGroup(baseDie, Math.absExact(value));
				temp.getAbsoluteFrequencies().forEach((composition, count) -> {
					ret.compute(composition, (k, v) -> {
						return count * factor + (v == null ? 0L : v);
					});
				});
			} else {
				Map<Integer, Integer> zero = Collections.singletonMap(0, 1);
				ret.compute(zero, (k, v) -> {
					return factor + (v == null ? 0L : v);
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