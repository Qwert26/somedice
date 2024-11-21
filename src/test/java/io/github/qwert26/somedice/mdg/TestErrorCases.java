package io.github.qwert26.somedice.mdg;

import java.util.Arrays;

import org.junit.jupiter.api.*;
import io.github.qwert26.somedice.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests {@link MixedDiceGroup}.
 * 
 * @author <b>Qwert26</b>, main author
 */
@DisplayName("TestErrorCasesOfMixedDiceGroup")
public class TestErrorCases extends TestMixedDiceGroup {
	/**
	 * The constructor should throw an exception for a missing array.
	 */
	@Test
	void constructorDisallowsNullArray() {
		assertThrows(IllegalArgumentException.class, () -> new MixedDiceGroup((IDie[]) null));
	}

	/**
	 * The constructor should throw an exception for an empty array.
	 */
	@Test
	void constructorDisallowsEmptyArray() {
		assertThrows(IllegalArgumentException.class, () -> new MixedDiceGroup());
	}

	/**
	 * The constructor should throw an exception for an array, which contains a
	 * <code>null</code>.
	 */
	@Test
	void constructorDisallowsNullInArray() {
		assertThrows(IllegalArgumentException.class, () -> new MixedDiceGroup((IDie) null));
		assertThrows(IllegalArgumentException.class,
				() -> new MixedDiceGroup(DiceCollection.WRATH_AND_GLORY_DIE, null));
	}

	/**
	 * Tests, that the returned array of the getSources-method does NOT allow a
	 * write-through.
	 */
	@Test
	void changeArrayAfterCreation() {
		IDie[] sources = new IDie[] { DiceCollection.WRATH_AND_GLORY_DIE };
		MixedDiceGroup underTest = new MixedDiceGroup(sources);
		assumeTrue(Arrays.deepEquals(sources, underTest.getSources()));
		sources[0] = null;
		assertNotEquals(null, underTest.getSources()[0]);
		assertEquals(DiceCollection.WRATH_AND_GLORY_DIE, underTest.getSources()[0]);
	}

	/**
	 * Tests that changes made to the initially given source array does not change
	 * the used sources of an instance.
	 */
	@Test
	void changeReturnedSourceArray() {
		MixedDiceGroup underTest = new MixedDiceGroup(DiceCollection.DICE_0_TO_90_IN_10);
		IDie[] sources = underTest.getSources();
		assumeTrue(1 == sources.length);
		assumeTrue(DiceCollection.DICE_0_TO_90_IN_10.equals(sources[0]));
		sources[0] = null;
		assertNotEquals(null, underTest.getSources()[0]);
		assertEquals(DiceCollection.DICE_0_TO_90_IN_10, underTest.getSources()[0]);
	}
}