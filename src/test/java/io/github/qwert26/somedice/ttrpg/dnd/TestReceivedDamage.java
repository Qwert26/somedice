package io.github.qwert26.somedice.ttrpg.dnd;

import org.junit.jupiter.api.Test;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link RecievedDamage}.
 */
public class TestReceivedDamage {
	@Test
	void constructorFailsWithoutSource() {
		assertThrows(NullPointerException.class, () -> new RecievedDamage((Compressor) null));
		assertThrows(NullPointerException.class, () -> new RecievedDamage((UnfairDie) null));
	}

	@Test
	void constructorWithUnfairDie() {
		RecievedDamage test = assertDoesNotThrow(() -> new RecievedDamage(DiceCollection.WRATH_AND_GLORY_DIE));
		assertNotNull(test.getSource());
		assertEquals(DiceCollection.WRATH_AND_GLORY_DIE, test.getSource());
		assertNotNull(test.getSourceAsUnfairDie());
		assertNull(test.getSourceAsCompressor());
	}

	@Test
	void constructorWithCompressor() {
		Compressor source = new Compressor(new HomogeneousDiceGroup(new SingleDie(6), 4));
		RecievedDamage test = assertDoesNotThrow(() -> new RecievedDamage(source));
		assertNotNull(test.getSource());
		assertEquals(source, test.getSource());
		assertNull(test.getSourceAsUnfairDie());
		assertNotNull(test.getSourceAsCompressor());
	}

	@Test
	void checkDefaults() {
		RecievedDamage test = new RecievedDamage(DiceCollection.WRATH_AND_GLORY_DIE);
		assertEquals(0, test.getReduction());
		assertFalse(test.isResistance());
		assertFalse(test.isVulnerability());
	}

	@Test
	void checkSetters() {
		RecievedDamage test = new RecievedDamage(DiceCollection.WRATH_AND_GLORY_DIE);
		assumeTrue(test.getReduction() == 0);
		assumeFalse(test.isResistance());
		assumeFalse(test.isVulnerability());
		test.setReduction(10);
		test.setResistance(true);
		test.setVulnerability(true);
		assertEquals(10, test.getReduction());
		assertTrue(test.isResistance());
		assertTrue(test.isVulnerability());
	}
}