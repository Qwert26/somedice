package io.github.qwert26.somedice.ttrpg.dnd;

import java.math.BigInteger;
import java.util.*;

import io.github.qwert26.somedice.*;

/**
 * Implements the standard way of calculating damage in "Dungeons & Dragons".
 * 
 * @author Qwert26
 */
public class RecievedDamage implements IDie {
	/**
	 * The source of damage.
	 */
	private IDie source = null;
	/**
	 * The damage reduction: Must be positive or zero.
	 */
	private int reduction = 0;
	/**
	 * If the target has resistance to the damage-type. As we do not differentiate
	 * here, it is just a {@code boolean}.
	 */
	private boolean resistance = false;
	/**
	 * If the target has vulnerability to the damage-type. As we do not
	 * differentiate here, it is just a {@code boolean}.
	 */
	private boolean vulnerability = false;

	/**
	 * Uses a {@link Compressor} as its source, as it produces value counts of 1.
	 * 
	 * @param compressor
	 * @throws NullPointerException If the given source is null.
	 */
	public RecievedDamage(Compressor compressor) {
		setSource(compressor);
	}

	/**
	 * Uses an {@link UnfairDie} as its source, as it produces value counts of 1.
	 * 
	 * @param unfairDie
	 * @throws NullPointerException If the given source is null.
	 */
	public RecievedDamage(UnfairDie unfairDie) {
		setSource(unfairDie);
	}

	/**
	 * 
	 * @return The current reduction-value, will be zero or greater.
	 */
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

	/**
	 * 
	 * @return The currently used source, will never be null and always be either an
	 *         {@link UnfairDie} or a {@link Compressor}.
	 */
	public final IDie getSource() {
		return source;
	}

	/**
	 * 
	 * @return The current source, if it actually is a {@link Compressor}.
	 *         <code>null</code>, if it is an {@link UnfairDie}.
	 */
	public final Compressor getSourceAsCompressor() {
		if (source instanceof Compressor compressor) {
			return compressor;
		}
		return null;
	}

	/**
	 * 
	 * @return The current source, if it actually is a {@link UnfairDie}.
	 *         <code>null</code>, if it is an {@link Compressor}.
	 */
	public final UnfairDie getSourceAsUnfairDie() {
		if (source instanceof UnfairDie die) {
			return die;
		}
		return null;
	}

	/**
	 * 
	 * @param source
	 * @throws NullPointerException if the new source is <code>null</code>.
	 */
	public final void setSource(Compressor source) {
		this.source = Objects.requireNonNull(source, "Compressor-Source can not be null!");
	}

	/**
	 * 
	 * @param source
	 * @throws NullPointerException if the new source is <code>null</code>.
	 */
	public final void setSource(UnfairDie source) {
		this.source = Objects.requireNonNull(source, "UnfairDie-Source can not be null!");
	}

	/**
	 * If the current source is a {@link Compressor}, it will convert it into an
	 * {@link UnfairDie}. This operation can not be undone.
	 */
	public final void convertSourceToUnfairDie() {
		Compressor original = getSourceAsCompressor();
		if (original != null) {
			source = original.toUnfairDie();
		}
	}

	/**
	 * Calculates the damage distribution. The order of modifiers are:
	 * <ol>
	 * <li>Reduction of damage, this can push it down to zero but not below it.</li>
	 * <li>Resistance, this divides the already reduced damage by two, rounding
	 * up.</li>
	 * <li>Vulnerability, this multiplies the reduced damage by two.</li>
	 * </ol>
	 * Resistance and Vulnerability are <b>NOT</b> mutually exclusive: Having both
	 * essentially means, that the damage gets rounded up to the next even number.
	 */
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