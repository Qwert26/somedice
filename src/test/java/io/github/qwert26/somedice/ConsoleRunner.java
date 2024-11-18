package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;
import org.junit.jupiter.api.Disabled;

import io.github.qwert26.somedice.util.MapComparators;

/**
 * Not an actual test!
 */
@Disabled("Not an actual Test!")
@Deprecated
public class ConsoleRunner {
	public static void main(String[] args) {
		SingleDie d4 = new SingleDie(4);
		DiceExploder exploder = new DiceExploder(d4, d4::explodesOn);
		exploder.setExplosionDepth((byte) 10);
		Map<Map<Integer, Integer>, BigInteger> result = new TreeMap<Map<Integer, Integer>, BigInteger>(
				MapComparators.NATURAL_MAP_INT_INT);
		result.putAll(exploder.getAbsoluteFrequencies());
		for (var resultEntry : result.entrySet()) {
			System.out.println(resultEntry);
		}
	}
}