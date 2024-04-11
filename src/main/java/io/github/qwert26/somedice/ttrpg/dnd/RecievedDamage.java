package io.github.qwert26.somedice.ttrpg.dnd;

import java.math.BigInteger;
import java.util.*;

import io.github.qwert26.somedice.*;

/**
 * 
 */
public class RecievedDamage implements IDie {
	private IDie source = null;
	private int reduction = 0;
	private boolean resistance = false;
	private boolean vulnerability = false;

	/**
	 * 
	 * @param compressor
	 */
	public RecievedDamage(Compressor compressor) {
		source = compressor;
	}

	/**
	 * 
	 * @param unfairDie
	 */
	public RecievedDamage(UnfairDie unfairDie) {
		source = unfairDie;
	}

	public final int getReduction() {
		return reduction;
	}

	/**
	 * Sets the value to reduce damage by: Damage can not be reduced below 0.
	 * 
	 * @param reduction
	 * @throws IllegalArgumentException If the new reduction is negative.
	 */
	public final void setReduction(int reduction) {
		if (reduction < 0) {
			throw new IllegalArgumentException("Reduction can not be negative.");
		}
		this.reduction = reduction;
	}

	public final boolean isResistance() {
		return resistance;
	}

	/**
	 * Enables or disables resistance: It cuts damage in half and is applied after
	 * reduction.
	 * 
	 * @param resistance
	 */
	public final void setResistance(boolean resistance) {
		this.resistance = resistance;
	}

	public final boolean isVulnerability() {
		return vulnerability;
	}

	/**
	 * Enables or disables vulnerability: It doubles damage and is applied after
	 * reduction and a possible resistance.
	 * 
	 * @param vulnerability
	 */
	public final void setVulnerability(boolean vulnerability) {
		this.vulnerability = vulnerability;
	}

	public final IDie getSource() {
		return source;
	}

	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, BigInteger> ret = new TreeMap<Map<Integer, Integer>, BigInteger>();
		Map<Map<Integer, Integer>, BigInteger> result = source.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, BigInteger> resultEntry : result.entrySet()) {
			Map.Entry<Integer, Integer> valueCount = resultEntry.getKey().entrySet().iterator().next();
			// Compressor and UnfairDie both produce valueCounts, where the count is always
			// equal to 1.
			int value = valueCount.getKey();
			value = Math.max(0, value - reduction);
			value = resistance ? Math.ceilDiv(value, 2) : value;
			value *= vulnerability ? 2 : 1;
			Map<Integer, Integer> newKey = Collections.singletonMap(value, 1);
			ret.compute(newKey,
					(key, frequency) -> resultEntry.getValue().add(frequency == null ? BigInteger.ZERO : frequency));
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
		result = prime * result + reduction;
		result = prime * result + (resistance ? 1231 : 1237);
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + (vulnerability ? 1231 : 1237);
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
		if (!(obj instanceof RecievedDamage)) {
			return false;
		}
		RecievedDamage other = (RecievedDamage) obj;
		if (reduction != other.reduction) {
			return false;
		}
		if (resistance != other.resistance) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (vulnerability != other.vulnerability) {
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
		builder.append("RecievedDamage [source=");
		builder.append(source);
		builder.append(", reduction=");
		builder.append(reduction);
		builder.append(", resistance=");
		builder.append(resistance);
		builder.append(", vulnerability=");
		builder.append(vulnerability);
		builder.append("]");
		return builder.toString();
	}

}