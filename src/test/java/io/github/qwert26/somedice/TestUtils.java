package io.github.qwert26.somedice;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static io.github.qwert26.somedice.Utils.*;

/**
 * Tests {@link Utils}.
 * 
 * @author Qwert26
 */
public class TestUtils {
	@Test
	public void testBinomialNegativeTotal() {
		assertThrows(IllegalArgumentException.class, () -> binomial(-1, 0));
	}

	@Test
	public void testBinomialNegativeGroup() {
		assertThrows(IllegalArgumentException.class, () -> binomial(0, -1));
	}

	@Test
	public void testBinomialGroupBiggerTotal() {
		assertThrows(IllegalArgumentException.class, () -> binomial(1, 2));
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	public void testBinomialZero(int total) {
		assertEquals(BigInteger.ONE, binomial(total, 0));
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	public void testBinomialMax(int total) {
		assertEquals(BigInteger.ONE, binomial(total, total));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	public void testBinomialOne(int total) {
		assertEquals(total, binomial(total, 1).intValueExact());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	public void testBinomialMaxMinusOne(int total) {
		assertEquals(total, binomial(total, total - 1).intValueExact());
	}

	@Test
	public void testFactorialNegative() {
		assertThrows(IllegalArgumentException.class, () -> factorial(-1));
	}

	@Test
	public void testFactorialZero() {
		assertEquals(BigInteger.ONE, factorial(0));
	}

	@Test
	public void testFactorialOne() {
		assertEquals(BigInteger.ONE, factorial(1));
	}
}