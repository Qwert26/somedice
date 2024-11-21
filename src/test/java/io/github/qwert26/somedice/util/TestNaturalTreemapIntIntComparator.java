package io.github.qwert26.somedice.util;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests the {@link MapComparators#NATURAL_TREEMAP_INT_INT}.
 * 
 * @author <b>Qwert26</b>, main author
 */
public class TestNaturalTreemapIntIntComparator {
	/**
	 * 
	 */
	@Test
	void testDifferentSizes() {
		TreeMap<Integer, Integer> small, big;
		small = new TreeMap<Integer, Integer>();
		big = new TreeMap<Integer, Integer>();
		small.put(0, 0);
		big.put(1, 1);
		big.put(2, 2);
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(small, big));
		assertTrue(result < 0);
		// Swap the arguments;
		result = MapComparators.NATURAL_TREEMAP_INT_INT.compare(big, small);
		assertTrue(result > 0);
	}

	/**
	 * 
	 */
	@Test
	void testSameMap() {
		TreeMap<Integer, Integer> single = new TreeMap<Integer, Integer>();
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(single, single));
		assertEquals(0, result, "Expected a zero, as a Map is always equal to itself.");
	}

	/**
	 * 
	 */
	@Test
	void testIdenticalMaps() {
		TreeMap<Integer, Integer> first, second;
		first = new TreeMap<Integer, Integer>();
		second = new TreeMap<Integer, Integer>();
		first.put(3, 3);
		second.put(3, 3);
		assumeTrue(first != second);
		assumeTrue(first.equals(second));
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(first, second));
		assertEquals(0, result);
		// Swap the arguments
		result = MapComparators.NATURAL_TREEMAP_INT_INT.compare(second, first);
		assertEquals(0, result);
	}

	/**
	 * 
	 */
	@Test
	void testSameSizedMapsWithoutOverlap() {
		TreeMap<Integer, Integer> small, big;
		small = new TreeMap<Integer, Integer>();
		big = new TreeMap<Integer, Integer>();
		small.put(4, 4);
		big.put(5, 5);
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(small, big));
		assertTrue(result < 0);
		// Swap the arguments
		result = MapComparators.NATURAL_TREEMAP_INT_INT.compare(big, small);
		assertTrue(result > 0);
	}

	/**
	 * 
	 */
	@Test
	void testSameSizedMapsWithOverlap() {
		TreeMap<Integer, Integer> small, big;
		small = new TreeMap<Integer, Integer>();
		big = new TreeMap<Integer, Integer>();
		small.put(6, 6);
		small.put(7, 7);
		big.put(6, 6);
		big.put(8, 8);
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(small, big));
		assertTrue(result < 0);
		// Swap the arguments
		result = MapComparators.NATURAL_TREEMAP_INT_INT.compare(big, small);
		assertTrue(result > 0);
	}

	/**
	 * 
	 */
	@Test
	void testSameSizedMapsWithDifferentValues() {
		TreeMap<Integer, Integer> small, big;
		small = new TreeMap<Integer, Integer>();
		big = new TreeMap<Integer, Integer>();
		small.put(9, 9);
		big.put(9, 99);
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(small, big));
		assertTrue(result < 0);
		// Swap the arguments
		result = MapComparators.NATURAL_TREEMAP_INT_INT.compare(big, small);
		assertTrue(result > 0);
	}

	/**
	 * 
	 */
	@Test
	void testEmptyMaps() {
		TreeMap<Integer, Integer> empty1, empty2;
		empty1 = new TreeMap<Integer, Integer>();
		empty2 = new TreeMap<Integer, Integer>();
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(empty1, empty2));
		assertTrue(result == 0);
	}

	/**
	 * 
	 */
	@Test
	void testEmptyAndNonEmptyMaps() {
		TreeMap<Integer, Integer> empty, full;
		empty = new TreeMap<Integer, Integer>();
		full = new TreeMap<Integer, Integer>();
		full.put(10, 10);
		int result = assertDoesNotThrow(() -> MapComparators.NATURAL_TREEMAP_INT_INT.compare(empty, full));
		assertTrue(result < 0);
		// Swap the arguments
		result = MapComparators.NATURAL_TREEMAP_INT_INT.compare(full, empty);
		assertTrue(result > 0);
	}
}
