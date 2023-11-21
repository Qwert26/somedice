package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.DisplayName;

import io.github.qwert26.somedice.*;

@DisplayName("TestHomogenousDiceGroupWithSingleDie")
public class TestWithSingleDie extends TestHomogenousDiceGroup {
	public TestWithSingleDie() {
		super(new SingleDie(4, false));
	}
}