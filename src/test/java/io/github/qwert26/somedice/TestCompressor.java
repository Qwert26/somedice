package io.github.qwert26.somedice;

import java.util.function.IntSupplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link Compressor}
 */
public class TestCompressor {
	/**
	 * Any constructor should throw an exception, if the source is missing.
	 */
	@Test
	void testConstructorsNoDie() {
		assertThrows(NullPointerException.class, () -> new Compressor(null));
		assertThrows(NullPointerException.class, () -> new Compressor(null, 0));
		assertThrows(NullPointerException.class, () -> new Compressor(null, Math::multiplyExact, Math::addExact, 0));
		assertThrows(NullPointerException.class, () -> new Compressor(null, () -> 0));
		assertThrows(NullPointerException.class, () -> new Compressor(null, Math::multiplyExact, Math::addExact));
		assertThrows(NullPointerException.class,
				() -> new Compressor(null, Math::multiplyExact, Math::addExact, () -> 0));
	}

	/**
	 * Any constructor should throw an exception, if the supplier for the start
	 * value is missing.
	 */
	@Test
	void testConstructorsNoStart() {
		IDie source = DiceCollection.WRATH_AND_GLORY_DIE;
		assertThrows(NullPointerException.class, () -> new Compressor(source, null));
		assertThrows(NullPointerException.class,
				() -> new Compressor(source, Math::multiplyExact, Math::addExact, null));
	}

	/**
	 * Any constructor should throw an exception, if the value-count-function is
	 * missing.
	 */
	@Test
	void testConstructorsNoValueCountFunction() {
		IDie source = DiceCollection.WRATH_AND_GLORY_DIE;
		assertThrows(NullPointerException.class, () -> new Compressor(source, null, Math::addExact));
		assertThrows(NullPointerException.class, () -> new Compressor(source, null, Math::addExact, 0));
		assertThrows(NullPointerException.class, () -> new Compressor(source, null, Math::addExact, () -> 0));
	}

	/**
	 * Any constructor should throw an exception, if the accumulation-function is
	 * missing.
	 */
	@Test
	void testConstructorsNoAccumulationFunction() {
		IDie source = DiceCollection.WRATH_AND_GLORY_DIE;
		assertThrows(NullPointerException.class, () -> new Compressor(source, Math::multiplyExact, null));
		assertThrows(NullPointerException.class, () -> new Compressor(source, Math::multiplyExact, null, 0));
		assertThrows(NullPointerException.class, () -> new Compressor(source, Math::multiplyExact, null, () -> 0));
	}

	/**
	 * Test the basic behavior of toString:
	 * <ul>
	 * <li>Never throws an exception.</li>
	 * <li>Never returns <code>null</code>.</li>
	 * <li>Contains the toString-result of the source.</li>
	 * </ul>
	 */
	@Test
	void testToString() {
		IDie source = DiceCollection.WRATH_AND_GLORY_DIE;
		Compressor toTest = new Compressor(source);
		String result = assertDoesNotThrow(() -> toTest.toString());
		assertNotNull(result);
		assertTrue(result.contains(source.toString()));
	}

	/**
	 * Tests the conversion of a simple integer into an {@link IntSupplier}, by
	 * checking the result of its {@link IntSupplier#getAsInt()}.
	 * 
	 * @param original
	 */
	@ParameterizedTest
	@ValueSource(ints = { -100, -10, -1, 0, 1, 10, 100 })
	void testCorrectStartValues(int original) {
		IDie source = DiceCollection.WRATH_AND_GLORY_DIE;
		Compressor toTest = new Compressor(source, original);
		IntSupplier startValue = assertDoesNotThrow(() -> toTest.getStartValue());
		assertNotNull(startValue);
		assertEquals(original, startValue.getAsInt());
	}
}