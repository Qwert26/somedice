package io.github.qwert26.somedice;

import java.util.*;
import java.math.BigInteger;

/**
 * The interface for a die.
 * 
 * @author Qwert26
 */
@FunctionalInterface
public interface IDie {
	/**
	 * 
	 * @return A mapping from individual or summed up dice values to their absolute
	 *         frequencies.
	 */
	Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies();
}
