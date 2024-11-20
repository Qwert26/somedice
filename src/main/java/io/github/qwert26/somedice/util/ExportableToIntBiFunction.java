package io.github.qwert26.somedice.util;

import java.util.function.ToIntBiFunction;

/**
 * Extends the standard {@code ToIntBiFunction} with a parameterless
 * {@code export}-function.
 * 
 * @author <b>Qwert26</b>, main author
 */
public interface ExportableToIntBiFunction<T, U> extends ToIntBiFunction<T, U> {
	/**
	 * @return
	 */
	String export();
}