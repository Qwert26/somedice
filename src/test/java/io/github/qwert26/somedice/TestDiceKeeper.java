package io.github.qwert26.somedice;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Class under test is {@link DiceKeeper}.
 */
class TestDiceKeeper {
	@Test
	void disallowsNull() {
		assertThrows(NullPointerException.class, () -> new DiceKeeper(null));
	}

	@Test
	void disallowsExecution() {
		DiceKeeper underTest = new DiceKeeper(FudgeDie.INSTANCE);
		assertThrows(IllegalStateException.class, () -> underTest.getAbsoluteFrequencies());
	}
}