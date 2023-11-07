package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;

/**
 * 
 */
@DisplayName("TestHomogenousDiceGroupWithFudgeDie")
public class TestWithFudgeDie extends TestHomogenousDiceGroup {
	public TestWithFudgeDie() {
		super(FudgeDie.INSTANCE);
	}
}