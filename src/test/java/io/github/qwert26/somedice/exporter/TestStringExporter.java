package io.github.qwert26.somedice.exporter;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link StringExporter}.}
 */
public class TestStringExporter {
	@ParameterizedTest
	@ValueSource(ints = { 2, 4, 6, 8, 10, 12, 20 })
	void testExportSingleDieStandard(int dieSize) {
		SingleDie die = new SingleDie(dieSize, false);
		String result = assertDoesNotThrow(() -> StringExporter.export(die));
		assertNotNull(result);
		assertEquals("d" + dieSize, result);
	}

	@ParameterizedTest
	@ValueSource(ints = { 2, 4, 6, 8, 10, 12, 20 })
	void testExportSingleDieStandardViaDispatch(int dieSize) {
		IDie die = new SingleDie(dieSize, false);
		String result = assertDoesNotThrow(() -> StringExporter.export(die));
		assertNotNull(result);
		assertEquals("d" + dieSize, result);
	}

	@ParameterizedTest
	@ValueSource(ints = { 2, 4, 6, 8, 10, 12, 20 })
	void testExportSingleDieZero(int dieSize) {
		SingleDie die = new SingleDie(dieSize, true);
		String result = assertDoesNotThrow(() -> StringExporter.export(die));
		assertNotNull(result);
		assertEquals("d0" + dieSize, result);
	}

	@Test
	void testExportFudgeDie() {
		String result = assertDoesNotThrow(() -> StringExporter.export(FudgeDie.INSTANCE));
		assertNotNull(result);
		assertEquals("dF", result);
	}

	@Test
	void testExportFudgeDieViaDispatch() {
		String result = assertDoesNotThrow(() -> StringExporter.export((IDie) FudgeDie.INSTANCE));
		assertNotNull(result);
		assertEquals("dF", result);
	}

	@Test
	void testExportRangeDie() {
		String result = assertDoesNotThrow(() -> StringExporter.export(DiceCollection.DICE_0_TO_90_IN_10));
		assertNotNull(result);
		assertEquals("d[0:91:10]", result);
		result = assertDoesNotThrow(() -> StringExporter.export(DiceCollection.DICE_10_TO_100_IN_10));
		assertNotNull(result);
		assertEquals("d[10:101:10]", result);
	}

	@Test
	void testExportRangeDieViaDisptach() {
		String result = assertDoesNotThrow(() -> StringExporter.export((IDie) DiceCollection.DICE_0_TO_90_IN_10));
		assertNotNull(result);
		assertEquals("d[0:91:10]", result);
		result = assertDoesNotThrow(() -> StringExporter.export((IDie) DiceCollection.DICE_10_TO_100_IN_10));
		assertNotNull(result);
		assertEquals("d[10:101:10]", result);
	}

	@Test
	void testExportNothing() {
		String result = assertDoesNotThrow(() -> StringExporter.export((IDie) null));
		assertNotNull(result);
		assertEquals("", result);
	}

	@Test
	void testExportAnonymousImplementation() {
		final IDie unknown = new IDie() {
			@Override
			public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
				return null;
			}
		};
		assertThrows(IllegalArgumentException.class, () -> StringExporter.export(unknown));
	}

	@Test
	void testExportHomogenousDiceGroup() {
		HomogeneousDiceGroup hdg = new HomogeneousDiceGroup(new SingleDie(false, 12), 2);
		String result = assertDoesNotThrow(() -> StringExporter.export(hdg));
		assertNotNull(result);
		assertEquals("2d12", result);
	}

	@Test
	void testExportHomogenousDiceGroupViaDispatch() {
		IDie hdg = new HomogeneousDiceGroup(new SingleDie(false, 8), 4);
		String result = assertDoesNotThrow(() -> StringExporter.export(hdg));
		assertNotNull(result);
		assertEquals("4d8", result);
	}

	@Test
	void testExportMixedDiceGroup() {
		MixedDiceGroup mdg = new MixedDiceGroup(FudgeDie.INSTANCE, new SingleDie(true, 10));
		String result = assertDoesNotThrow(() -> StringExporter.export(mdg));
		assertNotNull(result);
		assertEquals("(dF,d010)", result);
	}

	@Test
	void testExportMixedDiceGroupViaDispatch() {
		IDie mdg = new MixedDiceGroup(new SingleDie(false, 6), FudgeDie.INSTANCE);
		String result = assertDoesNotThrow(() -> StringExporter.export(mdg));
		assertNotNull(result);
		assertEquals("(d6,dF)", result);
	}

	@Test
	void testExportDiceDropperActive() {
		DiceDropper dropper = new DiceDropper(new HomogeneousDiceGroup(new SingleDie(4), 4), 1, 1);
		String result = assertDoesNotThrow(() -> StringExporter.export(dropper));
		assertNotNull(result);
		assertEquals("(4d4)dh1dl1", result);
	}

	@Test
	void testExportDiceDropperInactive() {
		DiceDropper dropper = new DiceDropper(new HomogeneousDiceGroup(new SingleDie(6), 3), 0, 0);
		String result = assertDoesNotThrow(() -> StringExporter.export(dropper));
		assertNotNull(result);
		assertEquals("(3d6)", result);
	}

	@Test
	void testExportDiceDropperViaDispatch() {
		IDie dropper = new DiceDropper(new HomogeneousDiceGroup(new SingleDie(6), 4), 1, 0);
		String result = assertDoesNotThrow(() -> StringExporter.export(dropper));
		assertNotNull(result);
		assertEquals("(4d6)dl1", result);
	}

	@Test
	void testExportDiceKeeperActive() {
		DiceKeeper keeper = new DiceKeeper(new HomogeneousDiceGroup(new SingleDie(20), 3), 1, 1);
		String result = assertDoesNotThrow(() -> StringExporter.export(keeper));
		assertNotNull(result);
		assertEquals("(3d20)kh1kl1", result);
	}

	@Test
	void testExportDiceKeeperInactive() {
		DiceKeeper keeper = new DiceKeeper(new HomogeneousDiceGroup(new SingleDie(12), 2), 0, 0);
		String result = assertDoesNotThrow(() -> StringExporter.export(keeper));
		assertNotNull(result);
		assertEquals("(2d12)", result);
	}

	@Test
	void testExportDiceKeeperViaDispatch() {
		IDie keeper = new DiceKeeper(new HomogeneousDiceGroup(new SingleDie(2), 8), 0, 1);
		String result = assertDoesNotThrow(() -> StringExporter.export(keeper));
		assertNotNull(result);
		assertEquals("(8d2)kh1", result);
	}

	@Test
	void testExportUnfairDie() {
		UnfairDie die = DiceCollection.WRATH_AND_GLORY_DIE;
		String result = assertDoesNotThrow(() -> StringExporter.export(die));
		assertNotNull(result);
		assertTrue(result.contains(die.getData().toString()));
	}

	@Test
	void testExportUnfairDieViaDispatch() {
		IDie die = DiceCollection.WRATH_AND_GLORY_DIE;
		String result = assertDoesNotThrow(() -> StringExporter.export(die));
		assertNotNull(result);
	}

	@Test
	void testExportIndeterministicDiceGroup() {
		UnfairDie base = DiceCollection.WRATH_AND_GLORY_DIE;
		Compressor dist = new Compressor(new HomogeneousDiceGroup(new SingleDie(6), 2));
		IndeterministicDiceGroup idg = new IndeterministicDiceGroup(base, dist);
		String result = assertDoesNotThrow(() -> StringExporter.export(idg));
		assertNotNull(result);
		assertTrue(result.contains(StringExporter.export(base)));
	}

	@Test
	void testExportIndeterministicDiceGroupViaDispatch() {
		UnfairDie base = DiceCollection.WRATH_AND_GLORY_DIE;
		Compressor dist = new Compressor(new HomogeneousDiceGroup(new SingleDie(6), 2));
		IDie idg = new IndeterministicDiceGroup(base, dist);
		String result = assertDoesNotThrow(() -> StringExporter.export(idg));
		assertNotNull(result);
		assertTrue(result.contains(StringExporter.export(base)));
	}
}
