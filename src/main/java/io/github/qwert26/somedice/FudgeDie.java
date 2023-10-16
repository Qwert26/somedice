package io.github.qwert26.somedice;

import java.util.*;

/**
 * A fudge-die is a special die with only three values, usually printed on a
 * standard D6 as <code>+</code>, <code>-</code> and <code>0</code>.
 * 
 * @author Qwert26
 */
public final class FudgeDie extends AbstractDie {
	/**
	 * The only instance of the fudge die: A die whose only values are -1, 0 and 1.
	 */
	public static final FudgeDie INSTANCE = new FudgeDie();

	/**
	 * 
	 */
	private FudgeDie() {
		super();
	}

	@Override
	public Map<Map<Integer, Integer>, Long> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, Long> ret = new HashMap<Map<Integer, Integer>, Long>(3, 1f);
		for (int v = -1; v <= 1; v++) {
			ret.put(Collections.singletonMap(v, 1), 1L);
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