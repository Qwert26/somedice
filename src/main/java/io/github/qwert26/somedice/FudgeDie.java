package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * A fudge-die is a special die with only three values, usually printed on a
 * standard D6 as <code>+</code>, <code>-</code> and <code>0</code>. It is also
 * sometimes called a fate-die.
 * 
 * @author Qwert26
 */
public final class FudgeDie extends AbstractDie {
	/**
	 * The only instance of the fudge-die: A die whose only values are -1, 0 and 1.
	 */
	public static final FudgeDie INSTANCE = new FudgeDie();

	/**
	 * Creates the only fudge-die.
	 */
	private FudgeDie() {
		super();
	}

	/**
	 * Creates a new map with the following entries:
	 * <ul>
	 * <li><code>{{-1=1}=1}</code></li>
	 * <li><code>{{0=1}=1}</code></li>
	 * <li><code>{{1=1}=1}</code></li>
	 * </ul>
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>(3, 1f);
		for (int v = -1; v <= 1; v++) {
			ret.put(Collections.singletonMap(v, 1), BigInteger.ONE);
		}
		return ret;
	}

	/**
	 * @return 3, always.
	 */
	@Override
	public int getDistinctValues() {
		return 3;
	}
}