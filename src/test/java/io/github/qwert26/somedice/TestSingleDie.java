package io.github.qwert26.somedice;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests {@link SingleDie}.
 */
public class TestSingleDie {
	private static final int[] PHYSICAL_DIE_SIZES = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 20, 21, 22, 24, 26, 28, 30, 36, 48, 50, 60, 100, 120 };

	public static final int[] physicalDieSizes() {
		return PHYSICAL_DIE_SIZES;
	}

	@Test
	void dissallowsMaximumOfOne() {
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(1));
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(1, true));
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(true, 1));
	}

	@Test
	void dissallowsNegativeMaximum() {
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(-1));
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(-1, true));
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(true, -1));
	}

	@Test
	void dissallowsMaximumOfZero() {
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(0));
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(0, true));
		assertThrows(IllegalArgumentException.class, () -> new SingleDie(true, 0));
	}

	@ParameterizedTest
	@MethodSource("physicalDieSizes")
	void checkValues(final int size) {
		SingleDie underTest = new SingleDie(size);
		assertEquals(size, underTest.getMaximum());
		assertEquals(size, underTest.getDistinctValues());
		underTest = new SingleDie(size, true);
		assertEquals(size, underTest.getMaximum());
		assertEquals(size, underTest.getDistinctValues());
		assertTrue(underTest.isStartAt0());
		underTest = new SingleDie(false, size);
		assertEquals(size, underTest.getMaximum());
		assertEquals(size, underTest.getDistinctValues());
		assertFalse(underTest.isStartAt0());
	}

	@ParameterizedTest
	@MethodSource("physicalDieSizes")
	void checkTruthfullness(final int size) {
		SingleDie underTest = new SingleDie(true, size);
		assertEquals(size, underTest.getAbsoluteFrequencies().size());
		underTest = new SingleDie(size, false);
		assertEquals(size, underTest.getAbsoluteFrequencies().size());
	}

	@ParameterizedTest
	@MethodSource("physicalDieSizes")
	void checkToString(final int size) {
		{
			final SingleDie underTest = new SingleDie(size, false);
			String result = assertDoesNotThrow(() -> underTest.toString());
			assertNotNull(result);
			assertTrue(result.contains(Integer.toString(size, 10)));
			assertTrue(result.contains(Boolean.toString(false)));
		}
		{
			final SingleDie underTest = new SingleDie(true, size);
			String result = assertDoesNotThrow(() -> underTest.toString());
			assertNotNull(result);
			assertTrue(result.contains(Integer.toString(size, 10)));
			assertTrue(result.contains(Boolean.toString(true)));
		}
	}

	@Test
	void checkEqualsUnequalStarts() {
		SingleDie one, two;
		one = new SingleDie(2, true);
		two = new SingleDie(2, false);
		assertNotEquals(one.hashCode(), two.hashCode());
		assertFalse(one.equals(two));
	}

	@Test
	void checkEqualsUnequalSizes() {
		SingleDie one, two;
		one = new SingleDie(2, true);
		two = new SingleDie(20, true);
		assertNotEquals(one.hashCode(), two.hashCode());
		assertFalse(one.equals(two));
	}

	@Test
	void checkEqualsUnequalClasses() {
		SingleDie one = new SingleDie(false, 3);
		assertFalse(one.equals(new Object()));
	}

	@Test
	void checkEqualsThis() {
		SingleDie one = new SingleDie(false, 3);
		assertTrue(one.equals(one));
	}

	@Test
	void checkEquals() {
		SingleDie one = new SingleDie(false, 4);
		SingleDie two = new SingleDie(4, false);
		assumeTrue(one.hashCode() == two.hashCode());
		assertTrue(one.equals(two));
	}

	@Nested
	public class Properties {
		@ParameterizedTest
		@MethodSource("io.github.qwert26.somedice.TestSingleDie#physicalDieSizes")
		void checkIsDenseWith0(final int size) {
			SingleDie underTest = new SingleDie(size, true);
			Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
			for (int value = 0; value < size; value++) {
				Map<Integer, Integer> valueCountKey = Collections.singletonMap(value, 1);
				assertTrue(result.containsKey(valueCountKey));
				Long frequency = result.get(valueCountKey);
				assertNotNull(frequency);
				assertEquals(1L, frequency.longValue());
			}
		}

		@ParameterizedTest
		@MethodSource("io.github.qwert26.somedice.TestSingleDie#physicalDieSizes")
		void checkIsDenseWithout0(final int size) {
			SingleDie underTest = new SingleDie(size, false);
			Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
			for (int value = 1; value <= size; value++) {
				Map<Integer, Integer> valueCountKey = Collections.singletonMap(value, 1);
				assertTrue(result.containsKey(valueCountKey));
				Long frequency = result.get(valueCountKey);
				assertNotNull(frequency);
				assertEquals(1L, frequency.longValue());
			}
		}
	}
}