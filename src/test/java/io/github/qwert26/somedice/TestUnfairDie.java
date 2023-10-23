package io.github.qwert26.somedice;

import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests {@link UnfairDie}.
 */
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
}
