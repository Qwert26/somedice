package io.github.qwert26.somedice.mdg;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junitpioneer.jupiter.cartesian.*;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TestMDGReplicatesHDG")
public class TestReplication extends TestMixedDiceGroup {
	@CartesianTest
	@CartesianTest.MethodFactory("io.github.qwert26.somedice.idg.TestReplication#diceAndCounts")
	void sameAsHDG(AbstractDie baseDie, int count) {
		HomogenousDiceGroup expected = new HomogenousDiceGroup(baseDie, count);
		AbstractDie[] sources = new AbstractDie[count];
		Arrays.fill(sources, baseDie);
		MixedDiceGroup actual = new MixedDiceGroup(sources);
		assertEquals(expected.getAbsoluteFrequencies(), actual.getAbsoluteFrequencies());
	}
}
