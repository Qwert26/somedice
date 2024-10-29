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

	public final IntPredicate getExplodeOn() {
		return explodeOn;
	}

	public final void setExplodeOn(IntPredicate explodeOn) {
		this.explodeOn = Objects.requireNonNull(explodeOn, "Given integer-predicate was null.");
	}

	public final byte getExplosionDepth() {
		return explosionDepth;
	}

	public final void setExplosionDepth(byte explosionDepth) {
		if (explosionDepth < 0) {
			throw new IllegalArgumentException("Explosion-Depth must be non-negative.");
		}
		this.explosionDepth = explosionDepth;
	}

	@Override
	public void setSource(IDie source) {
		if (source instanceof AbstractDie absDie) {
			source = absDie;
		} else {
			throw new IllegalArgumentException("Given source was not an AbstractDie.");
		}
	}

	public void setSource(AbstractDie source) {
		this.source = Objects.requireNonNull(source, "Source must not be null.");
	}

	@Override
	public IDie getSource() {
		return source;
	}

	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		// TODO Auto-generated method stub
		return null;
	}
}