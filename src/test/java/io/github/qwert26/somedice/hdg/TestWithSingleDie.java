package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.DisplayName;

import io.github.qwert26.somedice.*;

/**
 * Tests the {@link HomogeneousDiceGroup} with a {@link SingleDie}.
 * 
 * @author Qwert26
 */
@DisplayName("TestHomogenousDiceGroupWithSingleDie")
public class TestWithSingleDie extends TestHomogenousDiceGroup {
	public TestWithSingleDie() {
		super(new SingleDie(4, false));
	}
}