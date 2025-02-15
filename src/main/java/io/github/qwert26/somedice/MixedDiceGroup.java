package io.github.qwert26.somedice;

import java.math.BigInteger;
import java.util.*;

/**
 * <p>
 * A mixed dice group which consist of dice of varying types. For a dice group
 * containing only a single type of die, use {@link HomogeneousDiceGroup}.
 * </p>
 * <p>
 * Being unable to change the used sources after instance creation is a design
 * choice: It prevents the accidental creation of an endless loop, which can
 * cause a {@link StackOverflowError} or a {@link OutOfMemoryError}.
 * </p>
 * 
 * @author <b>Qwert26</b>, main author
 * @see HomogeneousDiceGroup
 * @see IndeterministicDiceGroup
 */
public class MixedDiceGroup implements IDie, IRequiresSources {
	/**
	 * The source where the results are coming from, the same source can be used
	 * multiple times.
	 */
	private final IDie[] sources;
	/**
	 * {@code MixedDiceGroup}s can be quite expensive to calculate their result: So
	 * the result gets cached and reused.
	 */
	private volatile transient Map<Map<Integer, Integer>, BigInteger> cachedResult = null;

	/**
	 * Creates a new mixed dice group with the given sources.
	 * 
	 * @param sources The sources to use. The array gets copied, so changes made to
	 *                it will not reflect into this class.
	 * @throws IllegalArgumentException if the source-array is <code>null</code>,
	 *                                  empty or contains at least one
	 *                                  <code>null</code>.
	 */
	public MixedDiceGroup(IDie... sources) {
		if (sources == null) {
			throw new IllegalArgumentException("No sources were given");
		}
		if (sources.length == 0) {
			throw new IllegalArgumentException("Sources were empty");
		}
		for (IDie source : sources) {
			if (source == null) {
				throw new IllegalArgumentException("A single source was null");
			}
		}
		this.sources = new IDie[sources.length];
		System.arraycopy(sources, 0, this.sources, 0, sources.length);
		cachedResult = null;
	}

	/**
	 * 
	 * @return A copy of the internal sources, changes made to the array do not
	 *         reflect in the instance.
	 */
	public final IDie[] getSources() {
		return Arrays.copyOf(sources, sources.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(sources);
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
		if (!(obj instanceof MixedDiceGroup)) {
			return false;
		}
		MixedDiceGroup other = (MixedDiceGroup) obj;
		if (!Arrays.equals(sources, other.sources)) {
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
		builder.append("MixedDiceGroup [sources=");
		builder.append(Arrays.toString(sources));
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Computes the effective Cartesian product of its input sources. That is the
	 * reason why for a single type of die, the {@link HomogeneousDiceGroup} is
	 * better suited, as it can make use of mathematics to compute all variations.
	 * 
	 * @implNote The same key can get generated multiple times, but it is never
	 *           modified after it is put in the returned map. Additionally, as the
	 *           result can take a while to compute, it is cached after computation
	 *           and reused on following calls.
	 */
	@Override
	public Map<Map<Integer, Integer>, BigInteger> getAbsoluteFrequencies() {
		if (cachedResult != null) {
			return new HashMap<>(cachedResult);
		}
		@SuppressWarnings("unchecked")
		List<Map.Entry<Map<Integer, Integer>, BigInteger>>[] indexedResultEntries = new List[sources.length];
		for (int i = 0; i < sources.length; i++) {
			indexedResultEntries[i] = new ArrayList<Map.Entry<Map<Integer, Integer>, BigInteger>>(
					sources[i].getAbsoluteFrequencies().entrySet());
		}
		Map<Map<Integer, Integer>, BigInteger> ret = new HashMap<Map<Integer, Integer>, BigInteger>();
		int[] indices = new int[sources.length];
		Arrays.fill(indices, 0);
		int masterIndex;
		infinity: while (true) {
			masterIndex = 0;
			final Map<Integer, Integer> nextKey = new TreeMap<Integer, Integer>();
			BigInteger nextValue = BigInteger.ONE;
			for (int i = 0; i < sources.length; i++) {
				Map.Entry<Map<Integer, Integer>, BigInteger> currentEntry = indexedResultEntries[i].get(indices[i]);
				for (Map.Entry<Integer, Integer> valueCount : currentEntry.getKey().entrySet()) {
					nextKey.merge(valueCount.getKey(), valueCount.getValue(), (oldV, newV) -> newV + oldV);
				}
				nextValue = nextValue.multiply(currentEntry.getValue());
			}
			// The next line is problematic, but after merging, we do not modify it further
			// and instead create a new key.
			ret.merge(nextKey, nextValue, (oldV, newV) -> oldV.add(newV));
			do {
				indices[masterIndex]++;
				if (indices[masterIndex] == indexedResultEntries[masterIndex].size()) {
					indices[masterIndex] = 0;
					masterIndex++;
				} else {
					continue infinity;
				}
			} while (masterIndex < indices.length);
			break;
		}
		cachedResult = new HashMap<>(ret);
		return ret;
	}
}