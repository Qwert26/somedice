package io.github.qwert26.somedice;

import java.util.*;

import io.github.qwert26.somedice.exporter.StringExporter;

/**
 * Not an actual test.
 */
public class ConsoleRunner {
	public static void main(String[] args) {
		SingleDie D6 = new SingleDie(false, 6);
		SingleDie D8 = new SingleDie(8, false);
		HomogenousDiceGroup threeD6 = new HomogenousDiceGroup(3, D6);
		MixedDiceGroup threeD6AndD8 = new MixedDiceGroup(threeD6, D8);
		DiceDropper dropLowest = new DiceDropper(threeD6AndD8, 1, 0);
		Compressor comp = new Compressor(dropLowest);
		UnfairDie result = comp.toUnfairDie();
		System.out.println("Result for " + StringExporter.export(dropLowest) + ":");
		for (Map.Entry<Integer, Long> valueCount : result.getData().entrySet()) {
			System.out.println("\t" + valueCount.getKey() + ": " + valueCount.getValue());
		}
	}
}