package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * 
 */
@Tag("integration")
public abstract class TestHomogenousDiceGroup {
	private AbstractDie source;

	protected TestHomogenousDiceGroup(AbstractDie source) {
		this.source = source;
	}

	@BeforeEach
	void verify() {
		Assumptions.assumeTrue(source != null);
	}

	@Test
	void disallowsZero() {
		assertThrows(IllegalArgumentException.class, () -> new HomogeneousDiceGroup(source, 0));
		assertThrows(IllegalArgumentException.class, () -> new HomogeneousDiceGroup(0, source));
	}

	@Test
	void keepsDieValue() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assumeTrue(source.equals(underTest.getBaseDie()));
		assertThrows(NullPointerException.class, () -> underTest.setBaseDie(null));
		assertEquals(source, underTest.getBaseDie());
	}

	@Test
	void keepsDieCount() {
		final int count = 2;
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(count, source);
		assumeTrue(count == underTest.getCount());
		assertThrows(IllegalArgumentException.class, () -> underTest.setCount(0));
		assertEquals(count, underTest.getCount());
	}

	@Test
	void toStringContainsDie() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		String result = assertDoesNotThrow(() -> underTest.toString());
		assertNotNull(result);
		assertTrue(result.contains(source.toString()));
	}

	@Test
	void hashcodeDoesNotThrow() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assertDoesNotThrow(() -> underTest.hashCode());
	}

	@Test
	void equalsItselfIsTrue() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assertTrue(underTest.equals(underTest));
	}

	@Test
	void equalsNullIsFalse() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assertFalse(underTest.equals(null));
	}

	@Test
	void equalsDifferentCountsIsFalse() {
		HomogeneousDiceGroup first = new HomogeneousDiceGroup(source, 1);
		HomogeneousDiceGroup second = new HomogeneousDiceGroup(source, 2);
		assertFalse(first.equals(second));
	}

	@Test
	void equalsAllMatchIsTrue() {
		HomogeneousDiceGroup first = new HomogeneousDiceGroup(source, 3);
		HomogeneousDiceGroup second = new HomogeneousDiceGroup(source, 3);
		assertTrue(first.equals(second));
	}

	@Nested
	@Tag("integration")
	public class Frequencies {
		@Test
		void identity() {
			HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source, 1);
			assertEquals(source.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}

		@ParameterizedTest
		@ValueSource(ints = { 2, 3, 4, 5, 6 })
		void checkValueCounts(final int count) {
			HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source, count);
			Map<Map<Integer, Integer>, BigInteger> result = underTest.getAbsoluteFrequencies();
			for (Map.Entry<Map<Integer, Integer>, BigInteger> entry : result.entrySet()) {
				int total = 0;
				for (Map.Entry<Integer, Integer> valueCount : entry.getKey().entrySet()) {
					total += valueCount.getValue();
				}
				assertEquals(count, total);
			}
		}
	}
}