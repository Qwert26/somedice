package io.github.qwert26.somedice.util;

import java.util.function.ToIntBiFunction;

/**
 * @author Qwert26
 */
public interface ExportableToIntBiFunction<T, U> extends ToIntBiFunction<T, U> {
	/**
	 * @return
	 */
	String export();
}