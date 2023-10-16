package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Class under test is {@link UnfairDie}.
 * 
 * @author Qwert26
 */
class TestUnfairDie {
	@Test
	void rollsSingularNumbers() {
		final int number = 1;
		final long occurences = 1L;
		UnfairDie underTest = new UnfairDie();
		Map<Integer, Long> internalData = underTest.getData();
		assumeTrue(internalData != null, "Constructor failed to create a map to fill.");
		assumeTrue(internalData.isEmpty(), "Constructor filled the map with something.");
		internalData.put(number, occurences);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(1, result.size(), "Unfair die rolled more than one result!");
		Map.Entry<Map<Integer, Integer>, Long> resultEntry = result.entrySet().iterator().next();
		assertEquals(1, resultEntry.getKey().get(number), "The given number occured more than once!");
		assertEquals(occurences, resultEntry.getValue(), "The given number occured more or less than expected!");
	}

	@Test
	void checkEquality() {
		UnfairDie one = new UnfairDie();
		UnfairDie two = new UnfairDie();
		one.getData().put(1, 1L);
		two.getData().put(1, 1L);
		assertEquals(one.hashCode(), two.hashCode(), "Unfair die can not be equal!");
		assertTrue(one.equals(two), "Unfair die are not equal!");
	}
}
