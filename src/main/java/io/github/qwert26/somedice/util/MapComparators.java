package io.github.qwert26.somedice.util;

import java.util.*;

/**
 * Contains various comparators for comparing maps.
 * 
 * @author <b>Qwert26</b>, main author
 */
public final class MapComparators {

	/**
	 * @throws UnsupportedOperationException Always, as this class is not meant to
	 *                                       have instances.
	 */
	private MapComparators() {
		super();
		throw new UnsupportedOperationException("Instances of MapComparators are not allowed.");
	}

	/**
	 * Sorts tree-maps in a string-like fashion:
	 * <ol>
	 * <li>If the maps have different sizes, the smaller one comes first.</li>
	 * <li>If a map has a key, that the other one does not, the map with the smaller
	 * key comes first.</li>
	 * <li>If both maps have the same keys, the smallest one with a difference comes
	 * first.</li>
	 * </ol>
	 * If everything runs through, the two tree-maps are considered identical.
	 * 
	 * @author <b>Qwert26</b>, main author
	 */
	public static final Comparator<TreeMap<Integer, Integer>> NATURAL_TREEMAP_INT_INT = new Comparator<TreeMap<Integer, Integer>>() {
		@Override
		public int compare(TreeMap<Integer, Integer> o1, TreeMap<Integer, Integer> o2) {
			int diff = o1.size() - o2.size();
			if (diff != 0) {
				return diff;
			}
			// The keys are in ascending order!
			for (Integer key : o1.keySet()) {
				if (!o2.containsKey(key)) {
					Integer floor = o2.floorKey(key); // DO NOT use auto-unboxing: There might be no key (null-value).
					if (floor == null) {
						return -1;
					} else if (o1.containsKey(floor)) {
						// We already checked that one, the values must be equal otherwise we would not
						// be here.
						return -1;
					} else {
						// o2 contains a key, that o1 does not and it is smaller!
						return 1;
					}
				}
				// The key is in both sets
				diff = o1.get(key) - o2.get(key);
				if (diff != 0) {
					return diff;
				}
			}
			return 0;
		}
	};
	/**
	 * <p>
	 * Sorts generic maps in a string-like fashion:
	 * <ol>
	 * <li>If the maps have different sizes, the smaller one comes first.</li>
	 * <li>If a map has a key, that the other one does not, the map with the smaller
	 * missing key comes first.</li>
	 * <li>If both maps have the same keys, the smallest one with a difference comes
	 * first.</li>
	 * </ol>
	 * If everything runs through, the two maps are considered identical.
	 * </p>
	 * <p>
	 * If the maps to be compared are only {@link TreeMap}s, consider using
	 * {@link #NATURAL_TREEMAP_INT_INT} instead, as it can make use of the strict
	 * ordering of keys, as well as some of the extra methods.
	 * </p>
	 * 
	 * @author <b>Qwert26</b>, main author
	 */
	public static final Comparator<Map<Integer, Integer>> NATURAL_MAP_INT_INT = new Comparator<Map<Integer, Integer>>() {
		@Override
		public int compare(Map<Integer, Integer> o1, Map<Integer, Integer> o2) {
			int diff = o1.size() - o2.size();
			if (diff != 0) {
				return diff;
			}
			if (o1.isEmpty()) {
				return 0; // o2 is also empty.
			}
			// At this point there is at least one key in each map.
			// These two hold the smallest key NOT in the other map.
			int minKey1 = Integer.MAX_VALUE, minKey2 = Integer.MAX_VALUE;
			// The keys can be in ANY order!
			for (Integer key1 : o1.keySet()) {
				if (!o2.containsKey(key1)) {
					minKey1 = Math.min(minKey1, key1);
				}
			}
			for (Integer key2 : o2.keySet()) {
				if (!o1.containsKey(key2)) {
					minKey2 = Math.min(minKey2, key2);
				}
			}
			diff = minKey1 - minKey2;
			if (diff != 0) {
				// Realistically speaking, both should be MAX_VALUE...
				return diff;
			}
			// At this point, both maps have the same keys.
			int smallestKeyWithDiff = Integer.MAX_VALUE;
			int lastDiff;
			for (Integer key : o1.keySet()) { // Don't auto-unbox, as we would need to box it again for the next
												// methods.
				lastDiff = o1.get(key) - o2.get(key);
				if (lastDiff != 0 && key < smallestKeyWithDiff) {
					smallestKeyWithDiff = key;
					diff = lastDiff;
				}
			}
			// If diff did not update during the loop, it is still zero.
			return diff;
		}
	};
}