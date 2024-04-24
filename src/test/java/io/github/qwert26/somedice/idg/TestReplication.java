package io.github.qwert26.somedice.idg;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junitpioneer.jupiter.cartesian.*;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the {@link IndeterministicDiceGroup} to be able to replicate a
 * {@link HomogenousDiceGroup} or a {@link MixedDiceGroup}.
 */
@DisplayName("TestIDGReplicatesOtherDiceGroups")
public class TestReplication extends TestIndeterministicDiceGroup {
	public static final ArgumentSets diceAndCounts() {
		return ArgumentSets
				.argumentsForFirstParameter(DiceCollection.WRATH_AND_GLORY_DIE, FudgeDie.INSTANCE,
						new SingleDie(false, 4), DiceCollection.DICE_10_TO_100_IN_10)
				.argumentsForNextParameter(1, 2, 3, 4, 5, 6);
		// Anything over 7 for the amount of dice takes too long to compute.
	}

	private static final UnfairDie createMonoDice(int count) {
		UnfairDie ret = new UnfairDie();
		ret.getData().put(count, BigInteger.ONE);
		return ret;
	}

	@CartesianTest
	@CartesianTest.MethodFactory("diceAndCounts")
	void sameAsHDG(AbstractDie baseDie, int count) {
		HomogenousDiceGroup expected = new HomogenousDiceGroup(baseDie, count);
		IndeterministicDiceGroup actual = new IndeterministicDiceGroup(createMonoDice(count), baseDie);
		assertEquals(expected.getAbsoluteFrequencies(), actual.getAbsoluteFrequencies());
	}

	@CartesianTest
	@CartesianTest.MethodFactory("diceAndCounts")
	void sameAsMDG(AbstractDie baseDie, int count) {
		AbstractDie[] sources = new AbstractDie[count];
		Arrays.fill(sources, baseDie);
		MixedDiceGroup expected = new MixedDiceGroup(sources);
		IndeterministicDiceGroup actual = new IndeterministicDiceGroup(createMonoDice(count), baseDie);
		assertEquals(expected.getAbsoluteFrequencies(), actual.getAbsoluteFrequencies());
	}
}