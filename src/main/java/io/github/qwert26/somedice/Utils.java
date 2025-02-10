package io.github.qwert26.somedice;

import java.math.*;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Contains various utility methods, mostly math-related.
 * 
 * @author <b>Qwert26</b>, main author
 */
public final class Utils {
	/**
	 * No instances are allowed.
	 * 
	 * @throws UnsupportedOperationException Always.
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
	 * @see Utils#multinomialIncomplete(int, int...)
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
	 * items can be sprinkled in any order between the groups of identical elements.
	 * 
	 * @param total       The expected sum of individual group sizes.
	 * @param individuals Each value in this variable argument array represent a
	 *                    single group with a certain size.
	 * @throws IllegalArgumentException If the sum of the individual group sizes is
	 *                                  greater than the total count.
	 * @see Utils#multinomialComplete(int, int...)
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
	 * Computes the semifactorial of {@code n}.
	 * 
	 * @param n
	 * @throws IllegalArgumentException If {@code n} is negative.
	 * @return {@code 1} if {@code n} is zero or one. {@code n!!} for any other
	 *         value greater than one.
	 * @implSpec Iterative implementation.
	 */
	public static final BigInteger semiFactorial(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Factorial of negative numbers can not be computed!");
		}
		if (n == 0 || n == 1) {
			return BigInteger.ONE;
		}
		BigInteger ret = BigInteger.ONE;
		for (; n > 1; n -= 2) {
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
		return ret;
	}

	/**
	 * Creates a new random {@code BigInteger} having a value from the interval
	 * {@code [rangeStart; rangeEnd]}. It uses the given random-instance to make its
	 * results repeatable.
	 * 
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

	/**
	 * Checks, if using the given source would result in an infinite loop. If yes,
	 * it will throw an exception. It uses the slow-fast-detection-method: The slow
	 * pointer only moves one step at a time, the fast one however two. If the fast
	 * one reaches the end of a chain, then there can be no cycle. If the fast
	 * pointer runs into the slow one, than there is a cycle. Where it is, does not
	 * matter at all.
	 * 
	 * @param current The starting point, something that has another
	 *                {@link IDie}-instance. Said instance could also be a
	 *                {@code IRequiresSource}-instance.
	 * @return <code>true</code>, if using the given source results in an infinite
	 *         loop.
	 */
	public static final boolean checkForCycle(IRequiresSource current) {
		IDie temp = current.getSource();
		IRequiresSource slow, fast;
		{
			if (temp instanceof IRequiresSource next) {
				slow = next;
			} else {
				return false;
			}
		}
		temp = slow.getSource();
		{
			if (temp instanceof IRequiresSource next) {
				fast = next;
			} else {
				return false;
			}
		}
		while (fast != null) {
			temp = fast.getSource();
			if (temp instanceof IRequiresSource next) {
				temp = next.getSource();
				if (temp instanceof IRequiresSource nextNext) {
					fast = nextNext;
				} else {
					// The source does not require a source
					return false;
				}
			} else {
				// The source does not require a source
				return false;
			}
			// slow should always be behind fast: So additional checks are not needed.
			slow = (IRequiresSource) slow.getSource();
			if (slow == fast) {
				return true;
			}
		}
		return false;
	}
}