package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.github.qwert26.somedice.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link Utils}.
 * 
 * @author Qwert26
 */
@Tag("unit")
public class TestUtils {
	/**
	 * The binomial coefficient can to be calculated for a negative total.
	 */
	@Test
	void testBinomialNegativeTotal() {
		assertThrows(IllegalArgumentException.class, () -> binomial(-1, 0));
	}

	/**
	 * The binomial coefficient can to be calculated for a negative group size.
	 */
	@Test
	void testBinomialNegativeGroup() {
		assertThrows(IllegalArgumentException.class, () -> binomial(0, -1));
	}

	/**
	 * The binomial coefficient can to be calculated for a group size which is
	 * bigger than the total amount.
	 */
	@Test
	void testBinomialGroupBiggerTotal() {
		assertThrows(IllegalArgumentException.class, () -> binomial(1, 2));
	}

	/**
	 * Choosing zero out of the given total should always result in a value of one.
	 * 
	 * @param total
	 */
	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testBinomialZero(int total) {
		assertEquals(BigInteger.ONE, binomial(total, 0));
	}

	/**
	 * Choosing all should always result in a value of one.
	 * 
	 * @param total
	 */
	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testBinomialMax(int total) {
		assertEquals(BigInteger.ONE, binomial(total, total));
	}

	/**
	 * Choosing one should always result in the number of total entries.
	 * 
	 * @param total
	 */
	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testBinomialOne(int total) {
		assertEquals(total, binomial(total, 1).intValueExact());
	}

	/**
	 * Choosing all BUT one should always result in the number of total entries.
	 * 
	 * @param total
	 */
	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testBinomialMaxMinusOne(int total) {
		assertEquals(total, binomial(total, total - 1).intValueExact());
	}

	/**
	 * Tests that negative factorials result in an exception.
	 */
	@Test
	void testFactorialNegativeOne() {
		assertThrows(IllegalArgumentException.class, () -> factorial(-1));
	}

	/**
	 * {@code 0! = 1}
	 */
	@Test
	void testFactorialZero() {
		assertEquals(BigInteger.ONE, factorial(0));
	}

	/**
	 * {@code 1! = 1}
	 */
	@Test
	void testFactorialOne() {
		assertEquals(BigInteger.ONE, factorial(1));
	}

	/**
	 * {@code 2! = 2}
	 */
	@Test
	void testFactorialTwo() {
		assertEquals(BigInteger.TWO, factorial(2));
	}

	/**
	 * {@code 3! = 6}
	 */
	@Test
	void testFactorialThree() {
		assertEquals(new BigInteger("6"), factorial(3));
	}

	/**
	 * {@code 4! = 24}
	 */
	@Test
	void testFactorialFour() {
		assertEquals(new BigInteger("24"), factorial(4));
	}

	@Test
	void testCompleteMultinomialNegativeTotal() {
		assertThrows(IllegalArgumentException.class, () -> multinomialComplete(-1, -1));
	}

	@Test
	void testCompleteMultinomialZeroTotalNoIndividuals() {
		assertEquals(BigInteger.ONE, multinomialComplete(0));
	}

	@Test
	void testCompleteMultinomialOneTotalNoIndividuals() {
		assertThrows(IllegalArgumentException.class, () -> multinomialComplete(1));
	}

	@Test
	void testCompleteMultinomialOneTotalTwoIndividuals() {
		assertThrows(IllegalArgumentException.class, () -> multinomialComplete(1, 2));
	}

	@Test
	void testIncompleteMultinomialNegativeTotal() {
		assertThrows(IllegalArgumentException.class, () -> multinomialIncomplete(-1, -1));
	}

	@Test
	void testIncompleteMultinomialZeroTotalNoIndividuals() {
		assertEquals(BigInteger.ONE, multinomialIncomplete(0));
	}

	@Test
	void testIncompleteMultinomialOneTotalNoIndividuals() {
		assertEquals(BigInteger.ONE, multinomialIncomplete(1));
	}

	@Test
	void testIncompleteMultinomialOneTotalTwoIndividuals() {
		assertThrows(IllegalArgumentException.class, () -> multinomialIncomplete(1, 2));
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testIncompleteMultinomialReplicatesBinomialTotal(int total) {
		assertEquals(binomial(total, total), multinomialIncomplete(total, total, 0));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testIncompleteMultinomialReplicatesBinomialTotalMinusOne(int total) {
		assertEquals(binomial(total, total - 1), multinomialIncomplete(total, total - 1, 1));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testIncompleteMultinomialReplicatesBinomialOne(int total) {
		assertEquals(binomial(total, 1), multinomialIncomplete(total, 1, total - 1));
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testIncompleteMultinomialReplicatesBinomialZero(int total) {
		assertEquals(binomial(total, 0), multinomialIncomplete(total, total, 0));
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void testIncompleteMultinomialReplicatesFactorial(int x) {
		assertEquals(factorial(x), multinomialIncomplete(x));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
	void testRandomBigInteger(int exponent) {
		Random random = new Random();
		final BigInteger start = BigInteger.ZERO;
		final BigInteger end = BigInteger.TEN.pow(exponent);
		final BigInteger result = assertDoesNotThrow(() -> Utils.RandomBigInteger(start, end, random));
		assertTrue(start.compareTo(result) <= 0);
		assertTrue(result.compareTo(end) <= 0);
	}
}