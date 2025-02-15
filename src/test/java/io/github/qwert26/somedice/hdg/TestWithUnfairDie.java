package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;

/**
 * Tests the {@link HomogeneousDiceGroup} with a {@link UnfairDie}.
 * 
 * @author <b>Qwert26</b>, main author
 */
@DisplayName("TestHomogenousDiceGroupWithUnfairDie")
public class TestWithUnfairDie extends TestHomogenousDiceGroup {
	public TestWithUnfairDie() {
		super(DiceCollection.WRATH_AND_GLORY_DIE);
	}
}