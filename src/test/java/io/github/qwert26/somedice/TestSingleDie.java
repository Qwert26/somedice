package io.github.qwert26.somedice;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class under test is {@link SingleDie}.
 * 
 * @author Qwert26
 */
class TestSingleDie {
	private static final int[] CDS = { 2, 4, 6, 8, 10, 12, 20, 100 };

	static int[] commonDieSizes() {
		return CDS;
	}

	@Test
	void dissallowOnes() {
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(1), "A one-sided die was allowed!");
	}

	@Test
	void constructorIdempotenty() {
		SingleDie boolInt = new SingleDie(true, 10);
		SingleDie intBool = new SingleDie(10, true);
		assertEquals(boolInt.hashCode(), intBool.hashCode(),
				"Constructors are not idempotent in relationship to argument order!");
		assertEquals(boolInt, intBool, "Constructors are not idempotent in relationship to argument order!");
	}

	@ParameterizedTest
	@MethodSource("commonDieSizes")
	void checkFrequenciesWith0(int size) {
		SingleDie underTest = new SingleDie(size, true);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, Long> entry : result.entrySet()) {
			assertEquals(1L, entry.getValue(), "A single die must be fair!");
			Map<Integer, Integer> valueCount = entry.getKey();
			assertEquals(1, valueCount.size(), "A single die must produce singular numbers!");
			for (Map.Entry<Integer, Integer> countEntry : valueCount.entrySet()) {
				assertEquals(1, countEntry.getValue(), "A single die must produce singular numbers!");
				assertTrue(0 <= countEntry.getKey(), "Die produced a negative number!");
				assertTrue(countEntry.getKey() < size, "Die produced a number to large!");
			}
		}
	}

	@ParameterizedTest
	@MethodSource("commonDieSizes")
	void checkFrequenciesWithout0(int size) {
		SingleDie underTest = new SingleDie(size, false);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		for (Map.Entry<Map<Integer, Integer>, Long> entry : result.entrySet()) {
			assertEquals(1L, entry.getValue(), "A single die must be fair!");
			Map<Integer, Integer> valueCount = entry.getKey();
			assertEquals(1, valueCount.size(), "A single die must produce singular numbers!");
			for (Map.Entry<Integer, Integer> countEntry : valueCount.entrySet()) {
				assertEquals(1, countEntry.getValue(), "A single die must produce singular numbers!");
				assertTrue(0 < countEntry.getKey(), "Die produced a negative number or zero!");
				assertTrue(countEntry.getKey() <= size, "Die produced a number to large!");
			}
		}
	}
}