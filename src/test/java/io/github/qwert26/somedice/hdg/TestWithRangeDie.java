package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.DisplayName;

import io.github.qwert26.somedice.*;

@DisplayName("TestHomogenousDiceGroupWithD[0,100,10]")
public class TestWithRangeDie extends TestHomogenousDiceGroup {
	public TestWithRangeDie() {
		super(new RangeDie(0, 100, 10));
	}
}