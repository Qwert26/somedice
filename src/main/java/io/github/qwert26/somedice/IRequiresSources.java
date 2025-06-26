package io.github.qwert26.somedice;

/**
 * Marks classes for needing multiple instances of {@link IDie} as inputs.
 * 
 * @author <b>Qwert26</b>, main author
 */
public interface IRequiresSources {
	/**
	 * @implSpec Changes to the array <b>must not</b> be visible in the class.
	 * @return The used sources.
	 */
	IDie[] getSources();
}