package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link RangeDie}.
 */
public class TestRangeDie {
	@Test
	void disallowsWrongRanges() {
		assertThrows(IllegalArgumentException.class, () -> new RangeDie(0, -10));
		assertThrows(IllegalArgumentException.class, () -> new RangeDie(0, -10, 2));
	}

	@Test
	void disallowsWrongSteps() {
		assertThrows(IllegalArgumentException.class, () -> new RangeDie(0, 10, 0));
		assertThrows(IllegalArgumentException.class, () -> new RangeDie(0, 10, -1));
	}

	@Test
	void checkValues() {
		int start = -10;
		int end = 10;
		RangeDie underTest = new RangeDie(start, end);
		assertEquals(start, underTest.getStart());
		assertEquals(end, underTest.getEnd());
		int step = 1;
		start *= 2;
		end *= 2;
		assumeTrue(step > 0);
		underTest = new RangeDie(start, end, step);
		assertEquals(start, underTest.getStart());
		assertEquals(end, underTest.getEnd());
		assertEquals(step, underTest.getStep());
	}

	@Test
	void keepsStartValue() {
		int start = -8;
		int end = 12;
		RangeDie underTest = new RangeDie(start, end);
		assumeTrue(start == underTest.getStart());
		assertThrows(IllegalArgumentException.class, () -> underTest.setStart(end));
		assertEquals(start, underTest.getStart());
	}

	@Test
	void keepsEndValue() {
		int start = -12;
		int end = 8;
		RangeDie underTest = new RangeDie(start, end);
		assumeTrue(end == underTest.getEnd());
		assertThrows(IllegalArgumentException.class, () -> underTest.setEnd(start));
		assertEquals(end, underTest.getEnd());
	}

	@Test
	void updatesStartValue() {
		final int start = -6;
		int end = 20;
		RangeDie underTest = new RangeDie(start, end);
		assumeTrue(start == underTest.getStart());
		assertDoesNotThrow(() -> underTest.setStart(-start));
		assertEquals(-start, underTest.getStart());
	}

	@Test
	void updatesEndValue() {
		int start = -20;
		final int end = 6;
		RangeDie underTest = new RangeDie(start, end);
		assumeTrue(end == underTest.getEnd());
		assertDoesNotThrow(() -> underTest.setEnd(-end));
		assertEquals(-end, underTest.getEnd());
	}

	@Test
	void predictsCorrectSizeWithStepsize1() {
		RangeDie underTest = new RangeDie(1, 21, 1);
		assertEquals(20, underTest.getDistinctValues());
	}

	@Test
	void predictsCorrectSizeWithStepsize2() {
		RangeDie underTest = new RangeDie(1, 21, 2);
		assertEquals(10, underTest.getDistinctValues());
	}

	@Test
	void predictsCorrectSizeWithStepsize4() {
		RangeDie underTest = new RangeDie(1, 21, 4);
		assertEquals(5, underTest.getDistinctValues());
	}

	@Test
	void predictsCorrectSizeWithStepsize5() {
		RangeDie underTest = new RangeDie(1, 21, 5);
		assertEquals(4, underTest.getDistinctValues());
	}

	@Test
	void predictsCorrectSizeWithStepsize10() {
		RangeDie underTest = new RangeDie(1, 21, 10);
		assertEquals(2, underTest.getDistinctValues());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 4, 5, 10, 20, 25, 50 })
	void checkPredictionWithActualSizeWithStepsize(final int size) {
		RangeDie underTest = new RangeDie(0, 100, size);
		assertEquals(underTest.getDistinctValues(), underTest.getAbsoluteFrequencies().size());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 4, 5, 10, 20, 25, 50 })
	void checkContentsWithStepsize(final int size) {
		final int start = 0, end = 100;
		RangeDie underTest = new RangeDie(start, end, size);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		for (int value = start; value < end; value += size) {
			Map<Integer, Integer> valueCountKey = Collections.singletonMap(value, 1);
			assertTrue(result.containsKey(valueCountKey));
			Long frequency = result.get(valueCountKey);
			assertNotNull(frequency);
			assertEquals(1L, frequency.longValue());
		}
	}
}