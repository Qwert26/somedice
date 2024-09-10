package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Provides the framework for testing the {@link HomogeneousDiceGroup} with
 * multiple different base die.
 */
@Tag("integration")
public abstract class TestHomogenousDiceGroup {
	/**
	 * The current base die.
	 */
	private AbstractDie source;

	protected TestHomogenousDiceGroup(AbstractDie source) {
		super();
		this.source = source;
	}

	/**
	 * Each test requires a source to be present.
	 */
	@BeforeEach
	void verify() {
		Assumptions.assumeTrue(source != null);
	}

	/**
	 * Independent of the current source, any constructor should not accept a count
	 * of zero.
	 */
	@Test
	void disallowsZero() {
		assertThrows(IllegalArgumentException.class, () -> new HomogeneousDiceGroup(source, 0));
		assertThrows(IllegalArgumentException.class, () -> new HomogeneousDiceGroup(0, source));
	}

	/**
	 * The dice group should not change its source, because its setBaseDie-method
	 * received a value of <code>null</code>.
	 */
	@Test
	void keepsDieValue() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assumeTrue(source.equals(underTest.getBaseDie()));
		assertThrows(NullPointerException.class, () -> underTest.setBaseDie(null));
		assertEquals(source, underTest.getBaseDie());
	}

	/**
	 * The dice group should not change its current count, because its
	 * setCount-method received a value of zero or less.
	 */
	@Test
	void keepsDieCount() {
		final int count = 2;
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(count, source);
		assumeTrue(count == underTest.getCount());
		assertThrows(IllegalArgumentException.class, () -> underTest.setCount(0));
		assertEquals(count, underTest.getCount());
	}

	/**
	 * Tests some basic behavior of the toString-method:
	 * <ul>
	 * <li>It does not throw any exceptions.</li>
	 * <li>It does not return a <code>null</code>-value.</li>
	 * <li>It contains the result of toString-method of its source.</li>
	 * </ul>
	 */
	@Test
	void toStringContainsDie() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		String result = assertDoesNotThrow(() -> underTest.toString());
		assertNotNull(result);
		assertTrue(result.contains(source.toString()));
	}

	/**
	 * Tests that the hashCode-method does not throw any exception.
	 */
	@Test
	void hashcodeDoesNotThrow() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assertDoesNotThrow(() -> underTest.hashCode());
	}

	/**
	 * Tests that any instance always equals itself.
	 */
	@Test
	void equalsItselfIsTrue() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assertTrue(underTest.equals(underTest));
	}

	/**
	 * Tests that any instance never equals <code>null</code>.
	 */
	@Test
	void equalsNullIsFalse() {
		HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source);
		assertFalse(underTest.equals(null));
	}

	/**
	 * Tests that two instances with different count-values are not equal.
	 */
	@Test
	void equalsDifferentCountsIsFalse() {
		HomogeneousDiceGroup first = new HomogeneousDiceGroup(source, 1);
		HomogeneousDiceGroup second = new HomogeneousDiceGroup(source, 2);
		assertFalse(first.equals(second));
		assertFalse(second.equals(first));
	}

	/**
	 * Tests that two instances with all equal values are truly equal.
	 */
	@Test
	void equalsAllMatchIsTrue() {
		HomogeneousDiceGroup first = new HomogeneousDiceGroup(source, 3);
		HomogeneousDiceGroup second = new HomogeneousDiceGroup(source, 3);
		assertTrue(first.equals(second));
	}

	/**
	 * As inner, non-static class, it requires an instance of its outer class.
	 */
	@Nested
	@Tag("integration")
	public class Frequencies {
		/**
		 * Tests that the absolute frequencies of a dice group containing one die is
		 * identical to the used die.
		 */
		@Test
		void identity() {
			HomogeneousDiceGroup underTest = new HomogeneousDiceGroup(source, 1);
			assertEquals(source.getAbsoluteFrequencies(), underTest.getAbsoluteFrequencies());
		}

		/**
		 * Tests that each value-count mapping in the returned frequencies sums up to
		 * the size of the dice group.
		 * 
		 * @param count The size of the dice group.
		 */
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