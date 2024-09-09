package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link IndeterministicDiceGroup}.
 * 
 * @author Qwert26
 */
@Tag("integration")
public abstract class TestIndeterministicDiceGroup {
	public TestIndeterministicDiceGroup() {
		super();
	}

	@SuppressWarnings("deprecation")
	@Test
	void testIllegalConstructor() {
		UnfairDie unfair = DiceCollection.WRATH_AND_GLORY_DIE;
		assertThrows(Exception.class, () -> new IndeterministicDiceGroup(unfair, unfair));
	}
}