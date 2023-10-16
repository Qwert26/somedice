package io.github.qwert26.somedice;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class under test is {@link Compressor}.
 * 
 * @author Qwert26
 */
class TestCompressor {
	@Test
	void disallowsNullSource() {
		assertThrows(NullPointerException.class, () -> new Compressor(null));
	}

	@Test
	void disallowsNullVCF() {
		assertThrows(NullPointerException.class,
				() -> new Compressor(FudgeDie.INSTANCE, null, Math::addExact, () -> 0));
	}

	@Test
	void disallowsNullAccu() {
		assertThrows(NullPointerException.class,
				() -> new Compressor(FudgeDie.INSTANCE, Math::multiplyExact, null, () -> 0));
	}

	@Test
	void disallowsNullStart() {
		assertThrows(NullPointerException.class,
				() -> new Compressor(FudgeDie.INSTANCE, Math::multiplyExact, Math::addExact, null));
	}

	@Test
	void checkDefaults() {
		Compressor underTest = new Compressor(FudgeDie.INSTANCE);
		assertEquals(FudgeDie.INSTANCE, underTest.getSource(), "Source was not properly set via short constructor!");
		assertNotNull(underTest.getValueCountFunction(), "Value-Count not set via short constructor!");
		assertNotNull(underTest.getAccumulator(), "Accumulator not set via short constructor!");
		assertNotNull(underTest.getStartValue(), "Starter not set via short constructor!");
	}
}