package io.github.qwert26.somedice;

import java.util.*;

/**
 * The interface for a die.
 * 
 * @author Qwert26
 */
public interface IDie {
	/**
	 * 
	 * @return A mapping from individual or summed up dice values to their absolute
	 *         frequencies.
	 */
	Map<Map<Integer, Integer>, Long> getAbsoluteFrequencies();
}
