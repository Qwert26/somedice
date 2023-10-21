package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class under test is {@link RangeDie}.
 */
class TestRangeDie {
	@Test
	void dissallowsWrongIntervalAtConstruction() {
		assertThrows(IllegalArgumentException.class, () -> new RangeDie(0, -10));
		assertThrows(IllegalArgumentException.class, () -> new RangeDie(0, -10, 1));
	}

	@Test
	void dissallowsNegativeStepsAtConstruction() {
		assertThrows(IllegalArgumentException.class, () -> new RangeDie(0, 10, -1));
	}

	@Test
	void dissallowsWrongIntervals() {
		RangeDie underTest = new RangeDie(0, 10);
		assertThrows(IllegalArgumentException.class, () -> underTest.setStart(20));
		assertThrows(IllegalArgumentException.class, () -> underTest.setEnd(-10));
	}

	@Test
	void checkFormulaCorrectness() {
		RangeDie underTest = DiceCollection.DICE_0_TO_90_IN_10;
		assertEquals(underTest.getAbsoluteFrequencies().size(), underTest.getDistinctValues());
		underTest = DiceCollection.DICE_10_TO_100_IN_10;
		assertEquals(underTest.getAbsoluteFrequencies().size(), underTest.getDistinctValues());
		underTest = new RangeDie(0, 10, 1);
		assertEquals(underTest.getAbsoluteFrequencies().size(), underTest.getDistinctValues());
	}
}