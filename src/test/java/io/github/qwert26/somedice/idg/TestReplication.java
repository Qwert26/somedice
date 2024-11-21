package io.github.qwert26.somedice.idg;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junitpioneer.jupiter.cartesian.*;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the {@link IndeterministicDiceGroup} to be able to replicate a
 * {@link HomogeneousDiceGroup} or a {@link MixedDiceGroup}.
 * 
 * @author <b>Qwert26</b>, main author
 */
@DisplayName("TestIDGReplicatesOtherDiceGroups")
public class TestReplication extends TestIndeterministicDiceGroup {
	/**
	 * Creates an ArgumentSets for the later {@link CartesianTest}s.
	 * 
	 * @return
	 */
	public static final ArgumentSets diceAndCounts() {
		return ArgumentSets
				.argumentsForFirstParameter(DiceCollection.WRATH_AND_GLORY_DIE, FudgeDie.INSTANCE,
						new SingleDie(false, 4), DiceCollection.DICE_10_TO_100_IN_10)
				.argumentsForNextParameter(1, 2, 3, 4, 5, 6);
		// Anything over 7 for the amount of dice takes too long to compute.
	}

	/**
	 * Creates a new {@link UnfairDie} with only a single number.
	 * 
	 * @param count
	 * @return
	 */
	private static final UnfairDie createMonoDice(int count) {
		UnfairDie ret = new UnfairDie();
		ret.getData().put(count, BigInteger.ONE);
		return ret;
	}

	/**
	 * Checks the replication of a {@link HomogeneousDiceGroup}.
	 * 
	 * @param baseDie
	 * @param count
	 */
	@CartesianTest
	@CartesianTest.MethodFactory("diceAndCounts")
	void sameAsHDG(AbstractDie baseDie, int count) {
		HomogeneousDiceGroup expected = new HomogeneousDiceGroup(baseDie, count);
		IndeterministicDiceGroup actual = new IndeterministicDiceGroup(createMonoDice(count), baseDie);
		assertEquals(expected.getAbsoluteFrequencies(), actual.getAbsoluteFrequencies());
	}

	/**
	 * Checks the replication of a {@link MixedDiceGroup}.
	 * 
	 * @param baseDie
	 * @param count
	 */
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