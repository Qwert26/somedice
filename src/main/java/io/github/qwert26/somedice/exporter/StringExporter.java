package io.github.qwert26.somedice.exporter;

import io.github.qwert26.somedice.*;

/**
 * @author Qwert26
 */
public final class StringExporter {
	/**
	 * Instances are not allowed.
	 */
	private StringExporter() throws Exception {
		super();
		throw new Exception("Instances of StringExporters are not allowed!");
	}

	/**
	 * Exports something that implements IDie and is known to the framework.
	 * 
	 * @param somedie
	 * @throws IllegalArgumentException if the instance is an unknown class or an
	 *                                  anonymous implementation of {@link IDie}.
	 * @return
	 */
	public static final String export(IDie somedie) {
		return switch (somedie) {
		case null -> "";
		case DiceDropper dropper -> export(dropper);
		case DiceKeeper keeper -> export(keeper);
		case FudgeDie fudge -> export(fudge);
		case HomogeneousDiceGroup hdg -> export(hdg);
		case MixedDiceGroup mdg -> export(mdg);
		case RangeDie range -> export(range);
		case SingleDie single -> export(single);
		case UnfairDie unfair -> export(unfair);
		case Compressor compressor -> export(compressor);
		default -> throw new IllegalArgumentException("Unable to Export!");
		};
	}

	/**
	 * Exports a {@link DiceDropper}. When {@link DiceDropper#getDropLowest()} and
	 * {@link DiceDropper#getDropHighest()} are both zero, its output might get
	 * confused with that of {@link #export(DiceKeeper)}.
	 * 
	 * @param dropper
	 * @see #export(DiceKeeper)
	 * @return
	 */
	public static final String export(DiceDropper dropper) {
		StringBuilder builder = new StringBuilder("(");
		builder.append(export(dropper.getSource()));
		builder.append(')');
		if (dropper.getDropHighest() > 0) {
			builder.append("dh").append(dropper.getDropHighest());
		}
		if (dropper.getDropLowest() > 0) {
			builder.append("dl").append(dropper.getDropLowest());
		}
		return builder.toString();
	}

	/**
	 * Exports a {@link DiceKeeper}. When {@link DiceKeeper#getKeepLowest()} and
	 * {@link DiceKeeper#getKeepHighest()} are both zero, its output might get
	 * confused with that of {@link #export(DiceDropper)}.
	 * 
	 * @param keeper
	 * @see #export(DiceDropper)
	 * @return
	 */
	public static final String export(DiceKeeper keeper) {
		StringBuilder builder = new StringBuilder("(");
		builder.append(export(keeper.getSource()));
		builder.append(')');
		if (keeper.getKeepHighest() > 0) {
			builder.append("kh").append(keeper.getKeepHighest());
		}
		if (keeper.getKeepLowest() > 0) {
			builder.append("kl").append(keeper.getKeepLowest());
		}
		return builder.toString();
	}

	/**
	 * "Exports" the fudge/fate die.
	 * 
	 * @param fudge ignored, as the parameter is only there for type
	 *              differentiation.
	 * @return {@code "df"}, as there is only one instance.
	 */
	public static final String export(FudgeDie fudge) {
		return "dF";
	}

	/**
	 * Exports a homogeneous dice group.
	 * 
	 * @param homogenousDice
	 * @return
	 */
	public static final String export(HomogeneousDiceGroup homogenousDice) {
		StringBuilder builder = new StringBuilder();
		builder.append(homogenousDice.getCount());
		builder.append(export(homogenousDice.getBaseDie()));
		return builder.toString();
	}

	public static final String export(MixedDiceGroup mixedGroup) {
		StringBuilder builder = new StringBuilder("(");
		IDie[] sources = mixedGroup.getSources();
		for (int i = 0; i < sources.length; i++) {
			builder.append(export(sources[i]));
			if (i + 1 < sources.length) {
				builder.append(',');
			}
		}
		builder.append(')');
		return builder.toString();
	}

	@Deprecated
	public static final String export(IndeterministicDiceGroup indeterministicGroup) {
		StringBuilder builder = new StringBuilder("");
		// TODO
		return builder.toString();
	}

	/**
	 * Exports a single die in the classical form of a small 'd', followed by the
	 * number of sides it has. If the die starts at zero, the number is prefixed
	 * with that value. It is NOT in the octal format!
	 * 
	 * @param single
	 * @return
	 */
	public static final String export(SingleDie single) {
		StringBuilder builder = new StringBuilder("d");
		if (single.isStartAt0()) {
			builder.append('0');
		}
		builder.append(single.getMaximum());
		return builder.toString();
	}

	/**
	 * 
	 * @param unfair
	 * @return
	 */
	public static final String export(UnfairDie unfair) {
		StringBuilder builder = new StringBuilder("d");
		builder.append(unfair.getData().toString());
		return builder.toString();
	}

	/**
	 * Exports a range die into a string form. It uses square brackets and separates
	 * the start-, end- and step-values by colons.
	 * 
	 * @param range
	 * @return
	 */
	public static final String export(RangeDie range) {
		StringBuilder builder = new StringBuilder("d[");
		builder.append(range.getStart());
		builder.append(':');
		builder.append(range.getEnd());
		builder.append(':');
		builder.append(range.getStep());
		builder.append(']');
		return builder.toString();
	}

	/**
	 * @deprecated The values of {@link Compressor#getValueCountFunction()} and
	 *             {@link Compressor#getAccumulator()} can not be exported.
	 * @param compressor
	 * @return
	 */
	@Deprecated
	public static final String export(Compressor compressor) {
		return export(compressor.getSource());
	}
}