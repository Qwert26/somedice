package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;
import java.util.function.IntPredicate;

/**
 * Simulates an exploding dice: The usual rule is, that a dice explodes, when it
 * lands on its maximum number, but this can be controlled via the predicate
 * {@link #explodeOn}. Exploding on all rolled values, will cause a delegation
 * to the {@link HomogeneousDiceGroup} for faster computation. In reality a dice
 * can explode infinite times, but programs can not deal with infinity when it
 * comes to probabilities and frequencies. So there is an artificial cut-off
 * which is controlled via {@link #explosionDepth}.
 * 
 * @author <b>Qwert26</b>, main author
 */
public class DiceExploder implements IDie, IRequiresSource {
	/**
	 * The die to explode.
	 */
	private AbstractDie source;
	/**
	 * Function to check, on which number(s) to explode on.
	 */
	private IntPredicate explodeOn;
	/**
	 * How often to explode a die.
	 */
	private byte explosionDepth = 0;

	/**
	 * Creates a new DiceExploder with the given source and condition, but with an
	 * explosion depth set to zero.
	 * 
	 * @param source
	 * @param explodeOn
	 * @throws NullPointerException If either the source or the condition is
	 *                              <code>null</code>.
	 */
	public DiceExploder(AbstractDie source, IntPredicate explodeOn) {
		super();
		setSource(source);
		setExplodeOn(explodeOn);
	}

	/**
	 * Creates a new DiceExploder with the given source, explosion condition and the
	 * given depth.
	 * 
	 * @param source
	 * @param explodeOn
	 * @param explosionDepth
	 * @throws NullPointerException     If either the source or the condition is
	 *                                  <code>null</code>.
	 * @throws IllegalArgumentException If the depth is negative.
	 */
	public DiceExploder(AbstractDie source, IntPredicate explodeOn, byte explosionDepth) {
		super();
		setSource(source);
		setExplodeOn(explodeOn);
		setExplosionDepth(explosionDepth);
	}

	/**
	 * Creates a new DiceExploder with the given source, explosion condition and the
	 * given depth.
	 * 
	 * @param source
	 * @param explosionDepth
	 * @param explodeOn
	 * @throws NullPointerException     If either the source or the condition is
	 *                                  <code>null</code>.
	 * @throws IllegalArgumentException If the depth is negative.
	 */
	public DiceExploder(AbstractDie source, byte explosionDepth, IntPredicate explodeOn) {
		super();
		setSource(source);
		setExplosionDepth(explosionDepth);
		setExplodeOn(explodeOn);
	}

	/**
	 * Creates a new DiceExploder with the given source, explosion condition and the
	 * given depth.
	 * 
	 * @param explosionDepth
	 * @param source
	 * @param explodeOn
	 * @throws NullPointerException     If either the source or the condition is
	 *                                  <code>null</code>.
	 * @throws IllegalArgumentException If the depth is negative.
	 */
	public DiceExploder(byte explosionDepth, AbstractDie source, IntPredicate explodeOn) {
		super();
		setExplosionDepth(explosionDepth);
		setSource(source);
		setExplodeOn(explodeOn);
	}

	/**
	 * Creates a new DiceExploder with the given source and condition, but with an
	 * explosion depth set to zero.
	 * 
	 * @param explodeOn
	 * @param source
	 * @throws NullPointerException If either the source or the condition is
	 *                              <code>null</code>.
	 */
	public DiceExploder(IntPredicate explodeOn, AbstractDie source) {
		super();
		setExplodeOn(explodeOn);
		setSource(source);
	}

	/**
	 * Creates a new DiceExploder with the given source, explosion condition and the
	 * given depth.
	 * 
	 * @param explodeOn
	 * @param source
	 * @param explosionDepth
	 * @throws NullPointerException     If either the source or the condition is
	 *                                  <code>null</code>.
	 * @throws IllegalArgumentException If the depth is negative.
	 */
	public DiceExploder(IntPredicate explodeOn, AbstractDie source, byte explosionDepth) {
		super();
		setExplodeOn(explodeOn);
		setSource(source);
		setExplosionDepth(explosionDepth);
	}

	/**
	 * Creates a new DiceExploder with the given source, explosion condition and the
	 * given depth.
	 * 
	 * @param explodeOn
	 * @param explosionDepth
	 * @param source
	 * @throws NullPointerException     If either the source or the condition is
	 *                                  <code>null</code>.
	 * @throws IllegalArgumentException If the depth is negative.
	 */
	public DiceExploder(IntPredicate explodeOn, byte explosionDepth, AbstractDie source) {
		super();
		setExplodeOn(explodeOn);
		setExplosionDepth(explosionDepth);
		setSource(source);
	}

	/**
	 * Creates a new DiceExploder with the given source, explosion condition and the
	 * given depth.
	 * 
	 * @param explosionDepth
	 * @param explodeOn
	 * @param source
	 * @throws NullPointerException     If either the source or the condition is
	 *                                  <code>null</code>.
	 * @throws IllegalArgumentException If the depth is negative.
	 */
	public DiceExploder(byte explosionDepth, IntPredicate explodeOn, AbstractDie source) {
		super();
		setExplosionDepth(explosionDepth);
		setExplodeOn(explodeOn);
		setSource(source);
	}

	/**
	 * 
	 * @return The current explosion condition, never <code>null</code>.
	 */
	public final IntPredicate getExplodeOn() {
		return explodeOn;
	}

	/**
	 * 
	 * @param explodeOn
	 * @throws NullPointerException
	 */
	public final void setExplodeOn(IntPredicate explodeOn) {
		this.explodeOn = Objects.requireNonNull(explodeOn, "Given integer-predicate was null.");
	}

	/**
	 * 
	 * @return The current explosion depth, never negative.
	 */
	public final byte getExplosionDepth() {
		return explosionDepth;
	}

	/**
	 * 
	 * @param explosionDepth
	 * @throws IllegalArgumentException
	 */
	public final void setExplosionDepth(byte explosionDepth) {
		if (explosionDepth < 0) {
			throw new IllegalArgumentException("Explosion-Depth must be non-negative.");
		}
		this.explosionDepth = explosionDepth;
	}

	/**
	 * @param source
	 * @throws NullPointerException     If the given source was <code>null</code>.
	 * @throws IllegalArgumentException If the given source was not an
	 *                                  {@link AbstractDie}.
	 */
	@Override
	public final void setSource(IDie source) {
		if (source == null) {
			throw new NullPointerException("Source as IDie must not be null.");
		} else if (source instanceof AbstractDie absDie) {
			source = absDie;
		} else {
			throw new IllegalArgumentException("Given source was not an AbstractDie.");
		}
	}

	/**
	 * 
	 * @param source
	 * @throws NullPointerException If the given source was <code>null</code>.
	 */
	public final void setSource(AbstractDie source) {
		this.source = Objects.requireNonNull(source, "Source as AbstractDie must not be null.");
	}

	/**
	 * @return The current source, always an {@link AbstractDie} and never
	 *         <code>null</code>.
	 */
	@Override
	public final IDie getSource() {
		return source;
	}

	/**
	 * @return An empty map, if {@link #explosionDepth} is set to zero. Otherwise,
	 *         an asymmetrical filled map AND if {@link #explodeOn} did NOT return
	 *         <code>true</code> for all values of the {@link #source}. Lastly the
	 *         result of
	 *         {@code new HomogeneousDiceGroup(source, explosionDepth).getAbsoluteFrequencies()}.
	 * @see HomogeneousDiceGroup#getAbsoluteFrequencies()
	 * @implNote Uses a mixture of {@link Collections#singletonMap(Object, Object)}s
	 *           and {@link TreeMap}s for its keys. Will delegate to
	 *           {@link HomogeneousDiceGroup#getAbsoluteFrequencies()} when it is
	 *           being told to explode on all values of the dice.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		int previousMuliplier = source.getDistinctValues();
		Map<Map<Integer, Integer>, BigInteger> baseMapping = source.getAbsoluteFrequencies();
		previousMuliplier -= baseMapping.entrySet().stream()
				.map(entry -> entry.getKey().keySet().stream().mapToInt(Integer::intValue).allMatch(explodeOn))
				.mapToInt(e -> e ? 1 : 0).sum();
		if (previousMuliplier == 0) {
			// In that case we were to told to always explode.
			return new HomogeneousDiceGroup(source, explosionDepth).getAbsoluteFrequencies();
		}
		// At least one number of the die does not result in an explosion.
		final BigInteger mul = BigInteger.valueOf(previousMuliplier);
		final Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>();
		for (byte currentDepth = 0; currentDepth < explosionDepth; currentDepth++) {
			if (ret.isEmpty()) {
				// That is for a depth of 1.
				ret.putAll(baseMapping);
			} else {
				// That is for depths of 2 and higher.
				final Map<Map<Integer, Integer>, BigInteger> shouldExplode = new HashMap<Map<Integer, Integer>, BigInteger>();
				ret.entrySet().stream().filter(
						entry -> entry.getKey().keySet().stream().mapToInt(Integer::intValue).allMatch(explodeOn))
						.forEach(entry -> shouldExplode.put(entry.getKey(), entry.getValue()));
				shouldExplode.keySet().stream().forEach(key -> ret.remove(key));
				// ret is free of sets, which should explode in this round.
				// shouldExplode now contains set to explode.
				ret.keySet().stream().forEach(key -> ret.compute(key, (k, value) -> value.multiply(mul)));
				// ret now contains updated absolute frequencies for previous sets.
				for (Map<Integer, Integer> keyPart : shouldExplode.keySet()) {
					for (var baseEntry : baseMapping.entrySet()) {
						Map<Integer, Integer> newKey = new TreeMap<Integer, Integer>(keyPart);
						for (var subEntry : baseEntry.getKey().entrySet()) {
							newKey.merge(subEntry.getKey(), subEntry.getValue(), Integer::sum);
						}
						ret.put(newKey, baseEntry.getValue());
					}
				}
			}
		}
		// A depth of 0 will skip to here.
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((explodeOn == null) ? 0 : explodeOn.hashCode());
		result = prime * result + explosionDepth;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		if (!(obj instanceof DiceExploder)) {
			return false;
		}
		DiceExploder other = (DiceExploder) obj;
		if (explodeOn == null) {
			if (other.explodeOn != null) {
				return false;
			}
		} else if (!explodeOn.equals(other.explodeOn)) {
			return false;
		}
		if (explosionDepth != other.explosionDepth) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiceExploder [source=");
		builder.append(source);
		builder.append(", explodeOn=");
		builder.append(explodeOn);
		builder.append(", explosionDepth=");
		builder.append(explosionDepth);
		builder.append("]");
		return builder.toString();
	}

}