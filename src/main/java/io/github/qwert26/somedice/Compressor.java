package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;
import java.util.function.*;

/**
 * A compressor takes in a detailed description of rolled dice and converts them
 * into a single number: This is more memory efficient but loses details. It is
 * made {@code final}, as certain other classes rely on the fact, that its
 * {@link #getAbsoluteFrequencies()}-method produces keys in the form of
 * <code>{X=1}</code>.
 * 
 * @author Qwert26
 */
public final class Compressor implements IDie, IRequiresSource {
	/**
	 * The source of dice rolls.
	 */
	private IDie source;
	/**
	 * Compresses a value-count-pair into a single value. Usually by multiplying one
	 * by the other.
	 * 
	 * @see Math#multiplyExact(int, int)
	 */
	private ToIntBiFunction<Integer, Integer> valueCountFunction;
	/**
	 * Function that describes how to combine a new compressed value-count-pair with
	 * the previous result. Usually by adding them.
	 * 
	 * @see Math#addExact(int, int)
	 */
	private ToIntBiFunction<Integer, Integer> accumulator;
	/**
	 * Provides the start value for the accumulation.
	 */
	private IntSupplier startValue;

	/**
	 * Creates a fully customized compressor.
	 * 
	 * @param source The source of the dice rolls.
	 * @param vcf    The function which turns a pair of a value and its occurrences
	 *               into a single value, to be accumulated.
	 * @param accu   The function which takes the previously accumulated result and
	 *               combines it with the next entry.
	 * @param start  The supplier for getting a start value for the accumulation.
	 * @throws NullPointerException if any of the parameters are null.
	 */
	public Compressor(IDie source, ToIntBiFunction<Integer, Integer> vcf, ToIntBiFunction<Integer, Integer> accu,
			IntSupplier start) {
		setSource(source);
		setValueCountFunction(vcf);
		setAccumulator(accu);
		setStartValue(start);
	}

	/**
	 * Creates a fully customized compressor.
	 * 
	 * @param source The source of the dice rolls.
	 * @param vcf    The function which turns a pair of a value and its occurence
	 *               into a single value, to be accumulated.
	 * @param accu   The function which takes the previously accumulated result and
	 *               combines it with the next entry.
	 * @param start  The discrete starting value.
	 * @throws NullPointerException if any of the first three parameters are null.
	 */
	public Compressor(IDie source, ToIntBiFunction<Integer, Integer> vcf, ToIntBiFunction<Integer, Integer> accu,
			int start) {
		setSource(source);
		setValueCountFunction(vcf);
		setAccumulator(accu);
		setStartValue(start);
	}

	/**
	 * Creates a compressor with the given source, value-count-function and
	 * accumulation-function, but with the standard starting value of 0.
	 * 
	 * @param source The source of the dice rolls.
	 * @param vcf    The function which turns a pair of a value and its occurence
	 *               into a single value, to be accumulated.
	 * @param accu   The function which takes the previously accumulated result and
	 *               combines it with the next entry.
	 * @throws NullPointerException if any of the three parameters are null.
	 */
	public Compressor(IDie source, ToIntBiFunction<Integer, Integer> vcf, ToIntBiFunction<Integer, Integer> accu) {
		setSource(source);
		setValueCountFunction(vcf);
		setAccumulator(accu);
		startValue = () -> 0;
	}

	/**
	 * Creates a standard compressor, which adds up all the individual dice with a
	 * custom start value.
	 * 
	 * @param source The source of the dice rolls.
	 * @param start  The supplier for getting a start value for the accumulation.
	 * @throws NullPointerException if any of the parameters are null.
	 */
	public Compressor(IDie source, IntSupplier start) {
		setSource(source);
		setStartValue(start);
		valueCountFunction = Math::multiplyExact;
		accumulator = Math::addExact;
	}

	/**
	 * Creates a standard compressor, which adds up all the individual dice with a
	 * custom start value.
	 * 
	 * @param source The source of the dice rolls.
	 * @param start  The supplier for getting a start value for the accumulation.
	 * @throws NullPointerException if the given source is <code>null</code>.
	 */
	public Compressor(IDie source, int start) {
		setSource(source);
		setStartValue(start);
		valueCountFunction = Math::multiplyExact;
		accumulator = Math::addExact;
	}

	/**
	 * Creates a standard compressor, which adds up all the individual dice with a
	 * start value of zero.
	 * 
	 * @param source The source of the dice rolls.
	 * @throws NullPointerException if the given source is <code>null</code>.
	 */
	public Compressor(IDie source) {
		setSource(source);
		valueCountFunction = Math::multiplyExact;
		accumulator = Math::addExact;
		startValue = () -> 0;
	}

	/**
	 * 
	 * @return The current source.
	 */
	public final IDie getSource() {
		return source;
	}

	/**
	 * 
	 * @param source The source of dice rolls to compress.
	 * 
	 * @throws NullPointerException     if the parameter is <code>null</code>.
	 * @throws IllegalArgumentException if the non-null parameter is of type
	 *                                  {@link IRequiresSource} and using it would
	 *                                  result in an infinite loop.
	 * @see Utils#checkForCycle(IRequiresSource)
	 */
	public final void setSource(IDie source) {
		if (source instanceof IRequiresSource future) {
			IDie oldSource = this.source;
			this.source = source;
			try {
				Utils.checkForCycle(future);
			} catch (IllegalArgumentException iae) {
				this.source = oldSource;
				throw iae;
			}
		}
		this.source = Objects.requireNonNull(source, "A source must be given!");
	}

	/**
	 * 
	 * @return The current function for compressing entries (a value and its count)
	 *         into a single number.
	 */
	public final ToIntBiFunction<Integer, Integer> getValueCountFunction() {
		return valueCountFunction;
	}

	/**
	 * 
	 * @param valueCountFunction The function compressing pairs of values and counts
	 *                           into a single value, which will be accumulated.
	 * @throws NullPointerException if the parameter is <code>null</code>.
	 */
	public final void setValueCountFunction(ToIntBiFunction<Integer, Integer> valueCountFunction) {
		this.valueCountFunction = Objects.requireNonNull(valueCountFunction,
				"A function for compressing key-value pairs must be given!");
	}

	/**
	 * 
	 * @return The current function for accumulating the complete dice roll.
	 */
	public final ToIntBiFunction<Integer, Integer> getAccumulator() {
		return accumulator;
	}

	/**
	 * 
	 * @param accumulator The function for accumulating the results of the
	 *                    {@link #valueCountFunction}.
	 * @throws NullPointerException if the parameter is <code>null</code>.
	 */
	public final void setAccumulator(ToIntBiFunction<Integer, Integer> accumulator) {
		this.accumulator = Objects.requireNonNull(accumulator,
				"A function for accumulating the results must be given!");
	}

	/**
	 * 
	 * @return The supplier of the start value of the accumulation.
	 */
	public final IntSupplier getStartValue() {
		return startValue;
	}

	/**
	 * 
	 * @param startValue A supplier for giving out the start value for accumulating.
	 *                   It should return a constant value each time.
	 * @throws NullPointerException if the parameter is <code>null</code>.
	 */
	public final void setStartValue(IntSupplier startValue) {
		this.startValue = Objects.requireNonNull(startValue, "A start value must be given!");
	}

	/**
	 * 
	 * @param startValue The discrete starting value for accumulation, will be
	 *                   wrapped in a {@link IntSupplier}.
	 */
	public final void setStartValue(final int startValue) {
		this.startValue = () -> startValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accumulator == null) ? 0 : accumulator.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((startValue == null) ? 0 : startValue.hashCode());
		result = prime * result + ((valueCountFunction == null) ? 0 : valueCountFunction.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Compressor)) {
			return false;
		}
		Compressor other = (Compressor) obj;
		if (accumulator == null) {
			if (other.accumulator != null) {
				return false;
			}
		} else if (!accumulator.equals(other.accumulator)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (startValue == null) {
			if (other.startValue != null) {
				return false;
			}
		} else if (!startValue.equals(other.startValue)) {
			return false;
		}
		if (valueCountFunction == null) {
			if (other.valueCountFunction != null) {
				return false;
			}
		} else if (!valueCountFunction.equals(other.valueCountFunction)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Compressor [source=");
		builder.append(source);
		builder.append(", valueCountFunction=");
		builder.append(valueCountFunction);
		builder.append(", accumulator=");
		builder.append(accumulator);
		builder.append(", startValue=");
		builder.append(startValue);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Compresses the source down into a single value. All entries in the returned
	 * map will look like this: <code>{{X=1}=Y}</code>. <code>X</code> is the
	 * compressed value and <code>Y</code> is the total frequency of said result.
	 * 
	 * @implNote Uses singleton-maps as its keys, as those are unmodifiable.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, BigInteger> result = source.getAbsoluteFrequencies();
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>(result.size(),
				1.0f);
		for (Map.Entry<Map<Integer, Integer>, BigInteger> resultEntry : result.entrySet()) {
			int accumulated = startValue.getAsInt();
			for (Map.Entry<Integer, Integer> valueCount : resultEntry.getKey().entrySet()) {
				int temp = valueCountFunction.applyAsInt(valueCount.getKey(), valueCount.getValue());
				accumulated = accumulator.applyAsInt(accumulated, temp);
			}
			ret.compute(Collections.singletonMap(accumulated, 1),
					(k, v) -> resultEntry.getValue().add(v == null ? BigInteger.ZERO : v));
		}
		return ret;
	}

	/**
	 * Compresses the source into an unfair die, which would allow the removal of
	 * the creation process afterwards.
	 * 
	 * @return A new unfair with the exact same distribution of values as the
	 *         compressed source.
	 */
	public UnfairDie toUnfairDie() {
		Map<Map<Integer, Integer>, BigInteger> result = source.getAbsoluteFrequencies();
		UnfairDie ret = new UnfairDie();
		Map<Integer, BigInteger> data = ret.getData();
		for (Map.Entry<Map<Integer, Integer>, BigInteger> resultEntry : result.entrySet()) {
			int accumulated = startValue.getAsInt();
			for (Map.Entry<Integer, Integer> valueCount : resultEntry.getKey().entrySet()) {
				int temp = valueCountFunction.applyAsInt(valueCount.getKey(), valueCount.getValue());
				accumulated = accumulator.applyAsInt(accumulated, temp);
			}
			data.compute(accumulated, (k, v) -> resultEntry.getValue().add(v == null ? BigInteger.ZERO : v));
		}
		return ret;
	}
}