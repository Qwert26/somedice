package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;

/**
 * Tests {@link IndeterministicDiceGroup}.
 * 
 * @author Qwert26
 */
@Tag("integration")
public abstract class TestIndeterministicDiceGroup {
	protected UnfairDie distribution;

	/**
	 * @param distribution The distribution of base die to roll.
	 */
	public TestIndeterministicDiceGroup(UnfairDie distribution) {
		super();
		this.distribution = distribution;
	}
}