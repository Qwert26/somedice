package io.github.qwert26.somedice;

/**
 * @author Qwert26
 */
public interface IRequiresSource {
	void setSource(IDie source);

	IDie getSource();
}