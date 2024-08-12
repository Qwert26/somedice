package io.github.qwert26.somedice.exporter;

import io.github.qwert26.somedice.*;

/**
 * @author Qwert26
 */
public final class StringExporter {
	private StringExporter() {
		super();
	}

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

	public static final String export(FudgeDie fudge) {
		return "dF";
	}

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
	
	public static final String export(IndeterministicDiceGroup indeterministicGroup) {
		StringBuilder builder = new StringBuilder("");
		// TODO
		return builder.toString();
	}

	public static final String export(SingleDie single) {
		StringBuilder builder = new StringBuilder("d");
		if (single.isStartAt0()) {
			builder.append('0');
		}
		builder.append(single.getMaximum());
		return builder.toString();
	}

	public static final String export(UnfairDie unfair) {
		StringBuilder builder = new StringBuilder("d");
		builder.append(unfair.getData().toString());
		return builder.toString();
	}

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

	public static final String export(Compressor compressor) {
		return export(compressor.getSource());
	}
}