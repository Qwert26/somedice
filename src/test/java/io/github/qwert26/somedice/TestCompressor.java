package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.*;
import java.util.function.*;

/**
 * Class under test is {@link Compressor}.
 * 
 * @author Qwert26
 */
class TestCompressor {
	@Test
	void disallowsNullSource() {
		assertThrows(NullPointerException.class, () -> new Compressor(null));
		assertThrows(NullPointerException.class, () -> new Compressor(null, 0));
		assertThrows(NullPointerException.class, () -> new Compressor(null, () -> 0));
	}

	@Test
	void disallowsNullVCF() {
		assertThrows(NullPointerException.class, () -> new Compressor(FudgeDie.INSTANCE, null, Math::addExact, 0));
		assertThrows(NullPointerException.class,
				() -> new Compressor(FudgeDie.INSTANCE, null, Math::addExact, () -> 0));
	}

	@Test
	void disallowsNullAccu() {
		assertThrows(NullPointerException.class, () -> new Compressor(FudgeDie.INSTANCE, Math::multiplyExact, null, 0));
		assertThrows(NullPointerException.class,
				() -> new Compressor(FudgeDie.INSTANCE, Math::multiplyExact, null, () -> 0));
	}

	@Test
	void disallowsNullStart() {
		assertThrows(NullPointerException.class, () -> new Compressor(FudgeDie.INSTANCE, null));
		assertThrows(NullPointerException.class,
				() -> new Compressor(FudgeDie.INSTANCE, Math::multiplyExact, Math::addExact, null));
	}

	@Test
	void checkDefaults() {
		Compressor underTest = new Compressor(FudgeDie.INSTANCE);
		assertEquals(FudgeDie.INSTANCE, underTest.getSource(), "Source was not properly set via short constructor!");
		assertNotNull(underTest.getValueCountFunction(), "Value-Count not set via short constructor!");
		assertNotNull(underTest.getAccumulator(), "Accumulator not set via short constructor!");
		assertNotNull(underTest.getStartValue(), "Starter not set via short constructor!");
	}

	@ParameterizedTest
	@ValueSource(ints = { -100, -10, 1, 0, 1, 10, 100 })
	void keepStartValues(final int value) {
		Compressor underTest = new Compressor(FudgeDie.INSTANCE, value);
		assumeTrue(underTest.getStartValue() != null);
		assertEquals(value, underTest.getStartValue().getAsInt());
	}

	@ParameterizedTest
	@ValueSource(ints = { -100, -10, 1, 0, 1, 10, 100 })
	void keepStartValuesSupplier(final int value) {
		IntSupplier start = new IntSupplier() {
			@Override
			public int getAsInt() {
				return value;
			}
		};
		Compressor underTest = new Compressor(FudgeDie.INSTANCE, start);
		assertEquals(start, underTest.getStartValue());
		assertEquals(value, underTest.getStartValue().getAsInt());
	}

	@ParameterizedTest
	@MethodSource("io.github.qwert26.somedice.TestSingleDie#commonDieSizes")
	void producesCorrectSizedResultForTwoDice(final int dieSize) {
		SingleDie die = new SingleDie(dieSize);
		HomogenousDiceGroup group = new HomogenousDiceGroup(die, 2);
		Compressor underTest = new Compressor(group);
		Map<Map<Integer, Integer>, Long> result = underTest.getAbsoluteFrequencies();
		assertEquals(dieSize * 2 - 1, result.size());
	}

	@Test
	void reproducesFatesDistribution() {
		Compressor truth = new Compressor(new HomogenousDiceGroup(FudgeDie.INSTANCE, 4), 0);
		Compressor underTest = new Compressor(new HomogenousDiceGroup(new SingleDie(3, false), 4), -8);
		assumeFalse(truth.equals(underTest));
		UnfairDie trueFate = truth.toUnfairDie();
		UnfairDie dieUnderTest = underTest.toUnfairDie();
		assumeTrue(trueFate.hashCode() == dieUnderTest.hashCode(), "The two dice could never be identical!");
		assertEquals(trueFate, dieUnderTest, "Dice have not the same distribution!");
	}
}