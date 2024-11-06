package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;
import java.util.function.IntPredicate;

/**
 * @author Qwert26
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
	 * 
	 * @param source
	 * @param explodeOn
	 */
	public DiceExploder(AbstractDie source, IntPredicate explodeOn) {
		super();
		setSource(source);
		setExplodeOn(explodeOn);
	}

	/**
	 * 
	 * @param source
	 * @param explodeOn
	 * @param explosionDepth
	 */
	public DiceExploder(AbstractDie source, IntPredicate explodeOn, byte explosionDepth) {
		super();
		setSource(source);
		setExplodeOn(explodeOn);
		setExplosionDepth(explosionDepth);
	}

	/**
	 * 
	 * @param source
	 * @param explosionDepth
	 * @param explodeOn
	 */
	public DiceExploder(AbstractDie source, byte explosionDepth, IntPredicate explodeOn) {
		super();
		setSource(source);
		setExplosionDepth(explosionDepth);
		setExplodeOn(explodeOn);
	}

	/**
	 * 
	 * @param explosionDepth
	 * @param source
	 * @param explodeOn
	 */
	public DiceExploder(byte explosionDepth, AbstractDie source, IntPredicate explodeOn) {
		super();
		setExplosionDepth(explosionDepth);
		setSource(source);
		setExplodeOn(explodeOn);
	}

	/**
	 * 
	 * @param explodeOn
	 * @param source
	 */
	public DiceExploder(IntPredicate explodeOn, AbstractDie source) {
		super();
		setExplodeOn(explodeOn);
		setSource(source);
	}

	/**
	 * 
	 * @param explodeOn
	 * @param source
	 * @param explosionDepth
	 */
	public DiceExploder(IntPredicate explodeOn, AbstractDie source, byte explosionDepth) {
		super();
		setExplodeOn(explodeOn);
		setSource(source);
		setExplosionDepth(explosionDepth);
	}

	/**
	 * 
	 * @param explodeOn
	 * @param explosionDepth
	 * @param source
	 */
	public DiceExploder(IntPredicate explodeOn, byte explosionDepth, AbstractDie source) {
		super();
		setExplodeOn(explodeOn);
		setExplosionDepth(explosionDepth);
		setSource(source);
	}

	/**
	 * 
	 * @param explosionDepth
	 * @param explodeOn
	 * @param source
	 */
	public DiceExploder(byte explosionDepth, IntPredicate explodeOn, AbstractDie source) {
		super();
		setExplosionDepth(explosionDepth);
		setExplodeOn(explodeOn);
		setSource(source);
	}

	/**
	 * 
	 * @return
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
	 * @return
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
	 * @throws IllegalArgumentException
	 */
	@Override
	public final void setSource(IDie source) {
		if (source instanceof AbstractDie absDie) {
			source = absDie;
		} else {
			throw new IllegalArgumentException("Given source was not an AbstractDie.");
		}
	}

	/**
	 * 
	 * @param source
	 * @throws NullPointerException
	 */
	public final void setSource(AbstractDie source) {
		this.source = Objects.requireNonNull(source, "Source must not be null.");
	}

	/**
	 * 
	 */
	@Override
	public final IDie getSource() {
		return source;
	}

	/**
	 * @see HomogeneousDiceGroup
	 * @implNote Uses a mixture of {@link Collections#singletonMap(Object, Object)}s
	 *           and {@link TreeMap}s for its keys.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		int previousMuliplier = source.getDistinctValues();
		Map<Map<Integer, Integer>, BigInteger> baseMapping = source.getAbsoluteFrequencies();
		previousMuliplier -= baseMapping.entrySet().stream()
				.map(entry -> entry.getKey().keySet().stream().mapToInt(Integer::intValue).allMatch(explodeOn))
				.mapToInt(e -> e ? 1 : 0).sum();
		if (previousMuliplier == 0) {
			// In that case we were to told to always explode
			return new HomogeneousDiceGroup(source, explosionDepth).getAbsoluteFrequencies();
		}
		// At least one number of the die does not result in an explosion!
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
}