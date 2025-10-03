package io.github.qwert26.somedice.util;

import java.util.function.*;

/**
 * The primitive version {@link BiPredicate}.
 */
@FunctionalInterface
public interface IntIntPredicate {
	/**
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	boolean test(int u, int v);
}