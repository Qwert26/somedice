package io.github.qwert26.somedice;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link UnfairDie}.
 */
@Tag("unit")
public class TestUnfairDie {
	@Test
	void checkEmpty() {
		UnfairDie underTest = new UnfairDie();
		assertEquals(0, underTest.getDistinctValues());
		assertTrue(underTest.getData().isEmpty());
		assertTrue(underTest.getAbsoluteFrequencies().isEmpty());
	}

	@Test
	void checkEqualsThis() {
		UnfairDie underTest = new UnfairDie();
		assertTrue(underTest.equals(underTest));
	}

	@Test
	void checkEqualsDifferentClass() {
		UnfairDie underTest = new UnfairDie();
		assertFalse(underTest.equals(new Object()));
	}

	@Test
	void checkEqualsDifferentData() {
		UnfairDie underTest = new UnfairDie();
		assertNotEquals(underTest.hashCode(), DiceCollection.WRATH_AND_GLORY_DIE.hashCode());
		assertFalse(underTest.equals(DiceCollection.WRATH_AND_GLORY_DIE));
	}

	@Test
	void checkEquals() {
		UnfairDie underTest = new UnfairDie();
		underTest.getData().put(0, 3L);
		underTest.getData().put(1, 2L);
		underTest.getData().put(2, 1L);
		assumeTrue(underTest.hashCode() == DiceCollection.WRATH_AND_GLORY_DIE.hashCode());
		assertTrue(underTest.equals(DiceCollection.WRATH_AND_GLORY_DIE));
	}

	@Test
	void checkToString() {
		String result = assertDoesNotThrow(() -> DiceCollection.WRATH_AND_GLORY_DIE.toString());
		assertNotNull(result);
	}

	@Test
	void checkConsistency() {
		UnfairDie underTest = DiceCollection.WRATH_AND_GLORY_DIE;
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		Map<Integer, Long> data = underTest.getData();
		assertEquals(data.size(), result.size());
		for (Map.Entry<Integer, Long> dataEntry : data.entrySet()) {
			Map<Integer, Integer> valueCountKey = Collections.singletonMap(dataEntry.getKey(), 1);
			assertTrue(result.containsKey(valueCountKey));
			Long frequency = result.get(valueCountKey);
			assertNotNull(frequency);
			assertEquals(dataEntry.getValue(), frequency);
		}
	}

	@Nested
	@Tag("compare")
	public class ReplicatesFudgeDie {
		private UnfairDie underTest;

		@BeforeEach
		void setup() {
			underTest = new UnfairDie();
			underTest.getData().put(-1, 1L);
			underTest.getData().put(0, 1L);
			underTest.getData().put(1, 1L);
		}

		@Test
		void checkIdenticalCount() {
			assertEquals(FudgeDie.INSTANCE.getDistinctValues(), underTest.getDistinctValues());
		}

		@Test
		void checkIdenticalFrequencies() {
			assertEquals(FudgeDie.INSTANCE.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}

		@AfterEach
		void teardown() {
			underTest = null;
		}
	}

	@Nested
	@Tag("compare")
	public class ReplicatesSingleDie {
		private UnfairDie underTest;
		private SingleDie truth;

		@BeforeEach
		@MethodSource("io.github.qwert26.somedice.TestSingleDie#physicalDieSizes")
		void setup(final int size) {
			truth = new SingleDie(size);
			underTest = new UnfairDie();
			for (int value = 1; value < size; value++) {
				underTest.getData().put(value, 1L);
			}
		}

		@ParameterizedTest
		@ValueSource(booleans = { true, false })
		void checkIdenticalCount(final boolean startAt0) {
			truth.setStartAt0(startAt0);
			if (startAt0) {
				underTest.getData().put(0, 1L);
			} else {
				underTest.getData().put(truth.getMaximum(), 1L);
			}
			assertEquals(truth.getDistinctValues(), underTest.getDistinctValues());
		}

		@AfterEach
		void teardown() {
			truth = null;
			underTest = null;
		}
	}
}
