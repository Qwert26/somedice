package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests {@link UnfairDie}.
 * 
 * @author <b>Qwert26</b>, main author
 */
@Tag("unit")
public class TestUnfairDie {
	@Test
	void checkEmpty() {
		UnfairDie underTest = new UnfairDie();
		assertEquals(0, underTest.getDistinctValues());
		assertTrue(underTest.getData().isEmpty());
		assertTrue(underTest.getAbsoluteFrequencies().isEmpty());
	}

	@Test
	void checkEqualsThis() {
		UnfairDie underTest = new UnfairDie();
		assertTrue(underTest.equals(underTest));
	}

	@Test
	void checkEqualsDifferentClass() {
		UnfairDie underTest = new UnfairDie();
		assertFalse(underTest.equals(new Object()));
	}

	@Test
	void checkEqualsDifferentData() {
		UnfairDie underTest = new UnfairDie();
		assertNotEquals(underTest.hashCode(), DiceCollection.WRATH_AND_GLORY_DIE.hashCode());
		assertFalse(underTest.equals(DiceCollection.WRATH_AND_GLORY_DIE));
	}

	@Test
	void checkEquals() {
		UnfairDie underTest = new UnfairDie();
		underTest.getData().put(0, BigInteger.valueOf(3));
		underTest.getData().put(1, BigInteger.TWO);
		underTest.getData().put(2, BigInteger.ONE);
		assumeTrue(underTest.hashCode() == DiceCollection.WRATH_AND_GLORY_DIE.hashCode());
		assertTrue(underTest.equals(DiceCollection.WRATH_AND_GLORY_DIE));
	}

	@Test
	void checkToString() {
		String result = assertDoesNotThrow(() -> DiceCollection.WRATH_AND_GLORY_DIE.toString());
		assertNotNull(result);
	}

	@Test
	void checkConsistency() {
		UnfairDie underTest = DiceCollection.WRATH_AND_GLORY_DIE;
		Map<Map<Integer, Integer>, BigInteger> result = underTest.getAbsoluteFrequencies();
		Map<Integer, BigInteger> data = underTest.getData();
		assertEquals(data.size(), result.size());
		for (Map.Entry<Integer, BigInteger> dataEntry : data.entrySet()) {
			Map<Integer, Integer> valueCountKey = Collections.singletonMap(dataEntry.getKey(), 1);
			assertTrue(result.containsKey(valueCountKey));
			BigInteger frequency = result.get(valueCountKey);
			assertNotNull(frequency);
			assertEquals(dataEntry.getValue(), frequency);
		}
	}

	@Test
	void checkCloningConstructor() {
		UnfairDie truth = DiceCollection.WRATH_AND_GLORY_DIE;
		UnfairDie test = new UnfairDie(truth);
		assertNotSame(truth.getData(), test.getData());
		assertEquals(truth, test);
		assertEquals(truth.getAbsoluteFrequencies(), test.getAbsoluteFrequencies());
	}

	@Test
	void checkCloningWithNull() {
		UnfairDie truth = new UnfairDie();
		UnfairDie test = assertDoesNotThrow(() -> new UnfairDie((AbstractDie) null));
		assertEquals(truth, test);
	}

	@Nested
	@Tag("comparison")
	public class ReplicatesFudgeDie {
		private UnfairDie underTest;

		@BeforeEach
		void setup() {
			underTest = new UnfairDie();
			underTest.getData().put(-1, BigInteger.ONE);
			underTest.getData().put(0, BigInteger.ONE);
			underTest.getData().put(1, BigInteger.ONE);
		}

		@Test
		void checkIdenticalCount() {
			assertEquals(FudgeDie.INSTANCE.getDistinctValues(), underTest.getDistinctValues());
		}

		@Test
		void checkIdenticalFrequencies() {
			assertEquals(FudgeDie.INSTANCE.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}

		@Test
		void checkViaConstructor() {
			UnfairDie cloned = new UnfairDie(FudgeDie.INSTANCE);
			assertTrue(cloned.hashCode() == underTest.hashCode());
			assertTrue(cloned.equals(underTest));
			assertTrue(underTest.equals(cloned));
		}

		@AfterEach
		void teardown() {
			underTest = null;
		}
	}

	@Nested
	@Tag("comparison")
	public class ReplicatesSingleDie {
		@ParameterizedTest
		@MethodSource("io.github.qwert26.somedice.TestSingleDie#physicalDieSizes")
		void checkIdenticalCountStartingWith1(final int size) {
			SingleDie truth = new SingleDie(size, false);
			UnfairDie underTest = new UnfairDie();
			for (int i = 1; i <= size; i++) {
				underTest.getData().put(i, BigInteger.ONE);
			}
			assertEquals(truth.getDistinctValues(), underTest.getDistinctValues());
			assertEquals(truth.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}

		@ParameterizedTest
		@MethodSource("io.github.qwert26.somedice.TestSingleDie#physicalDieSizes")
		void checkIdenticalCountStartingWith0(final int size) {
			SingleDie truth = new SingleDie(size, true);
			UnfairDie underTest = new UnfairDie();
			for (int i = 0; i < size; i++) {
				underTest.getData().put(i, BigInteger.ONE);
			}
			assertEquals(truth.getDistinctValues(), underTest.getDistinctValues());
			assertEquals(truth.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}

		@ParameterizedTest
		@MethodSource("io.github.qwert26.somedice.TestSingleDie#physicalDieSizes")
		void checkIdenticalCountStartingWith1ViaCloning(final int size) {
			SingleDie truth = new SingleDie(size, false);
			UnfairDie underTest = new UnfairDie(truth);
			assertEquals(truth.getDistinctValues(), underTest.getDistinctValues());
			assertEquals(truth.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}

		@ParameterizedTest
		@MethodSource("io.github.qwert26.somedice.TestSingleDie#physicalDieSizes")
		void checkIdenticalCountStartingWith0ViaCloning(final int size) {
			SingleDie truth = new SingleDie(size, true);
			UnfairDie underTest = new UnfairDie(truth);
			assertEquals(truth.getDistinctValues(), underTest.getDistinctValues());
			assertEquals(truth.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}
	}

	@Nested
	@Tag("comparison")
	public class ReplicatesRangeDie {
		@Test
		void checkViaCloning() {
			RangeDie truth = DiceCollection.DICE_0_TO_90_IN_10;
			UnfairDie test = new UnfairDie(truth);
			assertEquals(truth.getAbsoluteFrequencies(), test.getAbsoluteFrequencies());
		}
	}
}