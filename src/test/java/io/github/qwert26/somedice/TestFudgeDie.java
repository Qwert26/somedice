package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link FudgeDie}.
 */
@Tag("unit")
public class TestFudgeDie {
	@Test
	void checkInstanceExists() {
		assertNotNull(FudgeDie.INSTANCE);
	}

	@Test
	void checkDistinctValuesCount() {
		assertEquals(3, FudgeDie.INSTANCE.getDistinctValues());
		assertEquals(3, FudgeDie.INSTANCE.getAbsoluteFrequencies().size());
	}

	@Test
	void checkContents() {
		Map<Map<Integer, Integer>, BigInteger> result = FudgeDie.INSTANCE.getAbsoluteFrequencies();
		for (int value = -1; value <= 1; value++) {
			Map<Integer, Integer> valueCountKey = Collections.singletonMap(value, 1);
			assertTrue(result.containsKey(valueCountKey));
			BigInteger frequency = result.get(valueCountKey);
			assertNotNull(frequency);
			assertEquals(1L, frequency.longValue());
		}
	}
}
