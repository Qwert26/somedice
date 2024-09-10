package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link HomogeneousDiceGroup#equals(Object)} with different base die.
 * 
 * @author Qwert26
 */
@Tag("integration")
public class TestEqualsWithDifferentDice {
	/**
	 * Uses a {@link FudgeDie} for one instance and a {@link RangeDie} for the
	 * other.
	 */
	@Test
	void fudgeAndRange() {
		HomogeneousDiceGroup first = new HomogeneousDiceGroup(FudgeDie.INSTANCE);
		HomogeneousDiceGroup second = new HomogeneousDiceGroup(DiceCollection.DICE_0_TO_90_IN_10);
		assertFalse(first.equals(second));
	}

	/**
	 * Uses a {@link FudgeDie} for one instance and a {@link SingleDie} for the
	 * other.
	 */
	@Test
	void fudgeAndSingle() {
		HomogeneousDiceGroup first = new HomogeneousDiceGroup(FudgeDie.INSTANCE);
		HomogeneousDiceGroup second = new HomogeneousDiceGroup(new SingleDie(6));
		assertFalse(first.equals(second));
	}

	/**
	 * Uses a {@link RangeDie} for one instance and a {@link SingleDie} for the
	 * other.
	 */
	@Test
	void rangeAndSingle() {
		HomogeneousDiceGroup first = new HomogeneousDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		HomogeneousDiceGroup second = new HomogeneousDiceGroup(new SingleDie(8));
		assertFalse(first.equals(second));
	}
}