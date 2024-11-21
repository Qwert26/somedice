package io.github.qwert26.somedice.mdg;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junitpioneer.jupiter.cartesian.*;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the replication of a {@link HomogeneousDiceGroup} by a
 * {@link MixedDiceGroup}.
 * 
 * @author <b>Qwert26</b>, main author
 */
@DisplayName("TestMDGReplicatesHDG")
public class TestReplication extends TestMixedDiceGroup {
	/**
	 * 
	 * @param baseDie
	 * @param count
	 */
	@CartesianTest
	@CartesianTest.MethodFactory("io.github.qwert26.somedice.idg.TestReplication#diceAndCounts")
	void sameAsHDG(AbstractDie baseDie, int count) {
		HomogeneousDiceGroup expected = new HomogeneousDiceGroup(baseDie, count);
		AbstractDie[] sources = new AbstractDie[count];
		Arrays.fill(sources, baseDie);
		MixedDiceGroup actual = new MixedDiceGroup(sources);
		assertEquals(expected.getAbsoluteFrequencies(), actual.getAbsoluteFrequencies());
	}
}