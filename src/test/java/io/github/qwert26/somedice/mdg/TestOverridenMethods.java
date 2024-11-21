package io.github.qwert26.somedice.mdg;

import org.junit.jupiter.api.Test;

import io.github.qwert26.somedice.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link MixedDiceGroup}.
 * 
 * @author <b>Qwert26</b>, main author
 */
public class TestOverridenMethods extends TestMixedDiceGroup {
	@Test
	void hashcodeIsExceptionless() {
		MixedDiceGroup underTest = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		assertDoesNotThrow(() -> underTest.hashCode());
	}

	@Test
	void tostringIsExceptionless() {
		MixedDiceGroup underTest = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		assertDoesNotThrow(() -> underTest.toString());
	}

	@Test
	void tostringContainsSource() {
		MixedDiceGroup underTest = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		assertTrue(underTest.toString().contains(DiceCollection.DICE_10_TO_100_IN_10.toString()));
	}

	@Test
	void equalsItself() {
		MixedDiceGroup underTest = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		assertTrue(underTest.equals(underTest));
	}

	@Test
	void equalsNotNull() {
		MixedDiceGroup underTest = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		assertFalse(underTest.equals(null));
	}

	@Test
	void equalsDifferentSources() {
		MixedDiceGroup first = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		MixedDiceGroup second = new MixedDiceGroup(DiceCollection.DICE_0_TO_90_IN_10);
		assertFalse(first.equals(second));
	}

	@Test
	void equalsFully() {
		MixedDiceGroup first = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		MixedDiceGroup second = new MixedDiceGroup(DiceCollection.DICE_10_TO_100_IN_10);
		assertTrue(first.equals(second));
	}
}