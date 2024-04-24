package io.github.qwert26.somedice.hdg;

import org.junit.jupiter.api.*;

import io.github.qwert26.somedice.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link HomogenousDiceGroup#equals(Object)} with different base die.
 * 
 * @author Qwert26
 */
@Tag("integration")
public class TestEqualsWithDifferentDice {
	@Test
	void fudgeAndRange() {
		HomogenousDiceGroup first = new HomogenousDiceGroup(FudgeDie.INSTANCE);
		HomogenousDiceGroup second = new HomogenousDiceGroup(DiceCollection.DICE_0_TO_90_IN_10);
		assertFalse(first.equals(second));
	}

	@Test
	void fudgeAndSingle() {
		HomogenousDiceGroup first = new HomogenousDiceGroup(FudgeDie.INSTANCE);
		HomogenousDiceGroup second = new HomogenousDiceGroup(new SingleDie(6));
		assertFalse(first.equals(second));
	}

	@Test
	void rangeAndSingle() {
		HomogenousDiceGroup first = new HomogenousDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		HomogenousDiceGroup second = new HomogenousDiceGroup(new SingleDie(8));
		assertFalse(first.equals(second));
	}
}