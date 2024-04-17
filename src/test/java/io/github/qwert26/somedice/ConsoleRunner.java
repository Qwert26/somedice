package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Disabled;

import io.github.qwert26.somedice.exporter.StringExporter;

/**
 * Not an actual test!
 */
@Disabled("Not an actual Test!")
@Deprecated
public class ConsoleRunner {
	public static void main(String[] args) {
		mixedStatRoller();
		System.out.println();
		testIDG();
	}

	private static final void mixedStatRoller() {
		SingleDie D6 = new SingleDie(false, 6);
		SingleDie D8 = new SingleDie(8, false);
		HomogenousDiceGroup threeD6 = new HomogenousDiceGroup(3, D6);
		MixedDiceGroup threeD6AndD8 = new MixedDiceGroup(threeD6, D8);
		DiceDropper dropLowest = new DiceDropper(threeD6AndD8, 1, 0);
		Compressor comp = new Compressor(dropLowest);
		UnfairDie result = comp.toUnfairDie();
		System.out.println("Result for " + StringExporter.export(dropLowest) + ":");
		for (Map.Entry<Integer, BigInteger> valueCount : result.getData().entrySet()) {
			System.out.println(valueCount);
		}
	}

	private static final void testIDG() {
		SingleDie D4 = new SingleDie(false, 4);
		SingleDie D10 = new SingleDie(false, 10);
		HomogenousDiceGroup threeD10 = new HomogenousDiceGroup(D10, 3);
		Compressor compress3D10 = new Compressor(threeD10);
		IndeterministicDiceGroup peashooter = new IndeterministicDiceGroup(D4, compress3D10);
		Compressor compressPeashooter = new Compressor(peashooter);
		for (Entry<Integer, BigInteger> resultEntry : compressPeashooter.toUnfairDie().getData().entrySet()) {
			System.out.println(resultEntry);
		}
	}
}