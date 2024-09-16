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

	@Test
	void checkSetReductionIgnoresNegatives() {
		RecievedDamage test = new RecievedDamage(DiceCollection.WRATH_AND_GLORY_DIE);
		assumeTrue(test.getReduction() == 0);
		test.setReduction(8);
		assumeTrue(test.getReduction() == 8);
		assertThrows(IllegalArgumentException.class, () -> test.setReduction(-5));
		assertEquals(8, test.getReduction());
	}

	@Test
	void checkConversion() {
		Compressor source = new Compressor(new HomogeneousDiceGroup(new SingleDie(6), 4));
		RecievedDamage test = new RecievedDamage(source);
		assumeTrue(test.getSourceAsCompressor() != null);
		assumeTrue(test.getSourceAsUnfairDie() == null);
		test.convertSourceToUnfairDie();
		assertNotNull(test.getSourceAsUnfairDie());
		assertNull(test.getSourceAsCompressor());
		assertDoesNotThrow(() -> test.convertSourceToUnfairDie());
	}

	@Test
	void checkToString() {
		Compressor source = new Compressor(new HomogeneousDiceGroup(new SingleDie(6), 4));
		RecievedDamage test = new RecievedDamage(source);
		String result = assertDoesNotThrow(() -> test.toString());
		assertNotNull(result);
		assertTrue(result.contains(source.toString()));
	}

	@Test
	void checkHashCode() {
		Compressor source = new Compressor(new HomogeneousDiceGroup(new SingleDie(6), 4));
		RecievedDamage test = new RecievedDamage(source);
		test.setResistance(false);
		test.setVulnerability(false);
		final int noResNoVul = assertDoesNotThrow(() -> test.hashCode());
		test.setResistance(true);
		final int resNoVul = assertDoesNotThrow(() -> test.hashCode());
		assertNotEquals(noResNoVul, resNoVul);
		test.setVulnerability(true);
		final int resVul = assertDoesNotThrow(() -> test.hashCode());
		assertNotEquals(noResNoVul, resVul);
		assertNotEquals(resNoVul, resVul);
		test.setResistance(false);
		final int noResVul = assertDoesNotThrow(() -> test.hashCode());
		assertNotEquals(noResVul, resNoVul);
		assertNotEquals(noResVul, resVul);
		assertNotEquals(noResVul, noResNoVul);
	}
}