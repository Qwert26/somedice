package io.github.qwert26.somedice;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

/**
 * Class under test is {@link FudgeDie}.
 * 
 * @author Qwert26
 */
class TestFudgeDie {
	@Test
	void checkDistribution() {
		Map<Map<Integer, Integer>, Long> result = FudgeDie.INSTANCE.getAbsoluteFrequencies();
		assertEquals(3, result.size(), "Fudge die always have 3 possible results!");
		for (Map.Entry<Map<Integer, Integer>, Long> resultEntry : result.entrySet()) {
			Map<Integer, Integer> valueCount = resultEntry.getKey();
			assertEquals(1, valueCount.size(), "Fudge die produced multiple numbers!");
			Map.Entry<Integer, Integer> valueCountEntry = valueCount.entrySet().iterator().next();
			assertEquals(1, valueCountEntry.getValue(), "More then one die was rolled!");
			assertTrue(-1 <= valueCountEntry.getKey(), "Fudge die has a number smaller than -1.");
			assertTrue(valueCountEntry.getKey() <= 1, "Fudge die has a number greater than 1.");
			assertEquals(1, resultEntry.getValue(), "Fudge die has wrong frequency!");
		}
	}
}
