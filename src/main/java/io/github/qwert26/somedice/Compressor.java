package io.github.qwert26.somedice;

import java.util.*;
import java.util.function.*;

/**
 * A compressor takes in a detailed description of rolled dice and converts them
 * into a single number: This is more memory efficient but loses details.
 */
public class Compressor implements IDie {
	private IDie source;
	private ToIntBiFunction<Integer, Integer> valueCountFunction;
	private ToIntBiFunction<Integer, Integer> accumulator;
	private IntSupplier startValue;

	/**
	 * Creates a fully customized compressor.
	 * 
	 * @param source The source of the dice rolls.
	 * @param vcf    The function which turns a pair of a value and its occurence
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
	 * start value of zero.
	 * 
	 * @param source The source of the dice rolls.
	 * @throws NullPointerException
	 */
	public Compressor(IDie source) {
		setSource(source);
		valueCountFunction = Math::multiplyExact;
		accumulator = Math::addExact;
		startValue = () -> 0;
	}

	public final IDie getSource() {
		return source;
	}

	/**
	 * 
	 * @param source
	 * @throws NullPointerException
	 */
	public final void setSource(IDie source) {
		this.source = Objects.requireNonNull(source, "A source must be given!");
	}

	public final ToIntBiFunction<Integer, Integer> getValueCountFunction() {
		return valueCountFunction;
	}

	/**
	 * 
	 * @param valueCountFunction
	 * @throws NullPointerException
	 */
	public final void setValueCountFunction(ToIntBiFunction<Integer, Integer> valueCountFunction) {
		this.valueCountFunction = Objects.requireNonNull(valueCountFunction,
				"A function for compressing key-value pairs must be given!");
	}

	public final ToIntBiFunction<Integer, Integer> getAccumulator() {
		return accumulator;
	}

	/**
	 * 
	 * @param accumulator
	 * @throws NullPointerException
	 */
	public final void setAccumulator(ToIntBiFunction<Integer, Integer> accumulator) {
		this.accumulator = Objects.requireNonNull(accumulator,
				"A function for accumulating the results must be given!");
	}

	public final IntSupplier getStartValue() {
		return startValue;
	}

	/**
	 * 
	 * @param startValue
	 * @throws NullPointerException
	 */
	public final void setStartValue(IntSupplier startValue) {
		this.startValue = Objects.requireNonNull(startValue, "A start value must be given!");
	}

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

	@Override
	public Map<Map<Integer, Integer>, Long> getAbsoluteFrequencies() {
		Map<Map<Integer, Integer>, Long> result = source.getAbsoluteFrequencies();
		Map<Map<Integer, Integer>, Long> ret = new HashMap<Map<Integer, Integer>, Long>(result.size(), 1.0f);
		for (Map.Entry<Map<Integer, Integer>, Long> resultEntry : result.entrySet()) {
			int accumulated = startValue.getAsInt();
			for (Map.Entry<Integer, Integer> valueCount : resultEntry.getKey().entrySet()) {
				int temp = valueCountFunction.applyAsInt(valueCount.getKey(), valueCount.getValue());
				accumulated = accumulator.applyAsInt(accumulated, temp);
			}
			ret.compute(Collections.singletonMap(accumulated, 1),
					(k, v) -> v == null ? resultEntry.getValue() : (v + resultEntry.getValue()));
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
		Map<Map<Integer, Integer>, Long> result = source.getAbsoluteFrequencies();
		UnfairDie ret = new UnfairDie();
		Map<Integer, Long> data = ret.getData();
		for (Map.Entry<Map<Integer, Integer>, Long> resultEntry : result.entrySet()) {
			int accumulated = startValue.getAsInt();
			for (Map.Entry<Integer, Integer> valueCount : resultEntry.getKey().entrySet()) {
				int temp = valueCountFunction.applyAsInt(valueCount.getKey(), valueCount.getValue());
				accumulated = accumulator.applyAsInt(accumulated, temp);
			}
			data.compute(accumulated, (k, v) -> v == null ? resultEntry.getValue() : (v + resultEntry.getValue()));
		}
		return ret;
	}
}