package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;

/**
 * Tests the {@link HomogeneousDiceGroup} with a {@link FudgeDie}.
 * 
 * @author Qwert26
 */
@DisplayName("TestHomogenousDiceGroupWithFudgeDie")
public class TestWithFudgeDie extends TestHomogenousDiceGroup {
	public TestWithFudgeDie() {
		super(FudgeDie.INSTANCE);
	}
}