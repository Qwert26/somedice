package io.github.qwert26.somedice;

import java.math.*;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Contains various utility methods, mostly math-related.
 * 
 * @author Qwert26
 */
public final class Utils {
	/**
	 * No instances are allowed.
	 */
	private Utils() {
		super();
		throw new UnsupportedOperationException("Not allowed!");
	}

	/**
	 * Computes the multinomial coefficient with the assumption, that all items are
	 * in groups.
	 * 
	 * @param total       The expected sum of individual group sizes.
	 * @param individuals Each value in this variable argument array represent a
	 *                    single group with a certain size.
	 * @throws IllegalArgumentException If the sum of the individual group sizes is
	 *                                  not equal to the total count.
	 * @return The total amount of arrangements.
	 */
	public static final BigInteger multinomialComplete(int total, int... individuals) {
		if (total < 0) {
			throw new IllegalArgumentException("Total can not be negative!");
		}
		final int sum = IntStream.of(individuals).sum();
		if (sum > total) {
			throw new IllegalArgumentException("Too many individual groups!");
		}
		if (sum < total) {
			throw new IllegalArgumentException("Not enough individual groups!");
		}
		BigInteger ret = BigInteger.ONE;
		for (int individual : individuals) {
			ret = ret.multiply(binomial(total, individual));
			total -= individual;
		}
		return ret;
	}

	/**
	 * Computes the multinomial coefficient with the assumption, that left over
	 * items can be sprinkled in in any order between the groups of identical
	 * elements.
	 * 
	 * @param total       The expected sum of individual group sizes.
	 * @param individuals Each value in this variable argument array represent a
	 *                    single group with a certain size.
	 * @throws IllegalArgumentException If the sum of the individual group sizes is
	 *                                  greater than the total count.
	 * @return The total amount of arrangements.
	 */
	public static final BigInteger multinomialIncomplete(int total, int... individuals) {
		if (total < 0) {
			throw new IllegalArgumentException("Total can not be negative!");
		}
		final int sum = IntStream.of(individuals).sum();
		if (sum > total) {
			throw new IllegalArgumentException("Too many individual groups!");
		}
		BigInteger ret = BigInteger.ONE;
		for (int individual : individuals) {
			ret = ret.multiply(binomial(total, individual));
			total -= individual;
		}
		return ret.multiply(factorial(total));
	}

	/**
	 * Computes the factorial of {@code n}.
	 * 
	 * @param n
	 * @throws IllegalArgumentException If {@code n} is negative.
	 * @return {@code 1} if {@code n} is zero or one. {@code n!} for any other value
	 *         greater than one.
	 * @implSpec Iterative implementation.
	 */
	public static final BigInteger factorial(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Factorial of negative numbers can not be computed!");
		}
		if (n == 0 || n == 1) {
			return BigInteger.ONE;
		}
		BigInteger ret = BigInteger.ONE;
		for (; n > 1; n--) {
			ret = ret.multiply(BigInteger.valueOf(n));
		}
		return ret;
	}

	/**
	 * Computes the binomial coefficient {@code total}C{@code group}.
	 * 
	 * @param total
	 * @param group
	 * @throws IllegalArgumentException If either {@code total} or {@code group} are
	 *                                  negative or {@code group} is greater than
	 *                                  {@code total.}
	 * @return
	 */
	public static final BigInteger binomial(int total, int group) {
		if (total < 0 || group < 0 || group > total) {
			throw new IllegalArgumentException(total + "C" + group + " is not defined!");
		}
		if (group == 0 || group == total) {
			return BigInteger.ONE;
		}
		BigInteger ret = BigInteger.ONE;
		group = Math.max(group, total - group);
		int div = total - group;
		for (; total > group; total--) {
			ret = ret.multiply(BigInteger.valueOf(total));
			while (div > 1 && ret.mod(BigInteger.valueOf(div)).compareTo(BigInteger.ZERO) == 0) {
				ret = ret.divide(BigInteger.valueOf(div));
				div--;
			}
		}
		while (div > 1 && ret.mod(BigInteger.valueOf(div)).compareTo(BigInteger.ZERO) == 0) {
			ret = ret.divide(BigInteger.valueOf(div));
			div--;
		}
		return ret;
	}

	/**
	 * @see <a href="https://stackoverflow.com/a/70607245">The answer to the
	 *      question of generating random BigIntegers</a>
	 * @param rangeStart the smallest value to return, inclusive.
	 * @param rangeEnd   the biggest value to return, also inclusive.
	 * @param rand       the source of randomness.
	 * @implNote The generated values may not be entirely uniform.
	 * @author Panibo on StackOverflow
	 * @return
	 */
	public final static BigInteger RandomBigInteger(BigInteger rangeStart, BigInteger rangeEnd, Random rand) {
		int scale = rangeEnd.toString().length();
		String generated = "";
		for (int i = 0; i < rangeEnd.toString().length(); i++) {
			generated += rand.nextInt(10);
		}
		//
		BigDecimal inputRangeStart = new BigDecimal("0").setScale(scale, RoundingMode.FLOOR);
		BigDecimal inputRangeEnd = new BigDecimal("9".repeat(rangeEnd.toString().length())).setScale(scale,
				RoundingMode.FLOOR);
		//
		BigDecimal outputRangeStart = new BigDecimal(rangeStart).setScale(scale, RoundingMode.FLOOR);
		// Adds one to the output range to correct rounding
		BigDecimal outputRangeEnd = new BigDecimal(rangeEnd).add(BigDecimal.ONE).setScale(scale, RoundingMode.FLOOR);
		// Calculates: (generated - inputRangeStart) / (inputRangeEnd - inputRangeStart)
		// * (outputRangeEnd - outputRangeStart) + outputRangeStart
		BigDecimal generatedValue = new BigDecimal(new BigInteger(generated)).setScale(scale, RoundingMode.FLOOR)
				.subtract(inputRangeStart);
		BigDecimal inputRange = inputRangeEnd.subtract(inputRangeStart);
		BigDecimal relativeGeneratedValue = generatedValue.divide(inputRange, RoundingMode.FLOOR);
		BigDecimal outputRange = outputRangeEnd.subtract(outputRangeStart);
		BigDecimal relativeRawResult = relativeGeneratedValue.multiply(outputRange);
		BigDecimal rawResult = relativeRawResult.add(outputRangeStart);

		BigInteger returnInteger = rawResult.setScale(0, RoundingMode.FLOOR).toBigInteger();
		// Converts number to the end of output range if it's over it. This is to
		// correct rounding.
		returnInteger = (returnInteger.compareTo(rangeEnd) > 0 ? rangeEnd : returnInteger);
		return returnInteger;
	}
}