package io.github.qwert26.somedice.util;

import java.util.*;

/**
 * Contains various comparators for comparing maps.
 */
public final class MapComparators {

	/**
	 * 
	 */
	private MapComparators() {
		super();
	}

	/**
	 * Sorts tree-maps in a string-like fashion:
	 * <ol>
	 * <li>If the maps have different sizes, the smaller one comes first.</li>
	 * <li>If a map has a key, that the other one does not, the map with the smaller
	 * key comes first.</li>
	 * <li>If both maps have the key, the one with the smaller associated value
	 * comes first.</li>
	 * </ol>
	 * If everything runs through, the two tree-maps are considered identical.
	 * 
	 * @author Qwert26
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
					Integer floor = o2.floorKey(key); // DO NOT use auto-unboxing: There might be no key.
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
				diff = o1.get(key) - o2.get(key);
				if (diff != 0) {
					return diff;
				}
			}
			return 0;
		}
	};
	/**
	 * Sorts generic map in a string-like fashion:
	 * <ol>
	 * <li>If the maps have different sizes, the smaller one comes first.</li>
	 * <li>If a map has a key, that the other one does not, the map with the smaller
	 * key comes first.</li>
	 * <li>If both maps have the key, the one with the smaller associated value
	 * comes first.</li>
	 * </ol>
	 * If everything runs through, the two maps are considered identical.
	 * 
	 * @author Qwert26
	 * @deprecated Currently violates the last point.
	 */
	@Deprecated
	public static final Comparator<Map<Integer, Integer>> NATURAL_MAP_INT_INT = new Comparator<Map<Integer, Integer>>() {
		@Override
		public int compare(Map<Integer, Integer> o1, Map<Integer, Integer> o2) {
			int diff = o1.size() - o2.size();
			if (diff != 0) {
				return diff;
			}
			// These two hold the smallest key NOT in the other map.
			int minKey1 = Integer.MAX_VALUE, minKey2 = Integer.MAX_VALUE;
			// They keys can be in ANY order!
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
			// TODO: How to deal with uncertainty of order?
			return 0;
		}
	};
}