package io.github.qwert26.somedice;

/**
 * Marks classes for needing another instance of {@code IDie} as an input.
 * 
 * @author <b>Qwert26</b>, main author
 */
public interface IRequiresSource {
	/**
	 * Sets the currently used source to this one.
	 * 
	 * @implSpec The given source should be rejected and the old one retained, if an
	 *           infinite loop would be created by using the new source.
	 * @param source The new source.
	 * @see Utils#checkForCycle(IRequiresSource)
	 */
	void setSource(IDie source);

	/**
	 * 
	 * @return The current source.
	 */
	IDie getSource();
}