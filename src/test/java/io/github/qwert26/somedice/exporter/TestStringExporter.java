package io.github.qwert26.somedice.exporter;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 
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
}
