package io.github.qwert26.somedice;

import java.util.*;
import java.math.BigInteger;

/**
 * The interface for a die.
 * 
 * @author <b>Qwert26</b>, main author
 */
@FunctionalInterface
public interface IDie {
	/**
	 * Requests the absolute frequencies of a single die or a collection of dice.
	 * The "keying" map is a mapping from thrown values to their occurrences.
	 * 
	 * @implSpec After adding a map as a key, further modifications to it are
	 *           prohibited, because this might result in <b>inaccessible</b> data!
	 * @see Collections#singletonMap(Object, Object)
	 * @see Collections#unmodifiableMap(Map)
	 * @see Collections#unmodifiableNavigableMap(NavigableMap)
	 * @return A mapping from individual or summed up dice values to their absolute
	 *         frequencies.
	 */
	Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies();
}
