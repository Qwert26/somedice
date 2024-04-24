package io.github.qwert26.somedice.mdg;

import io.github.qwert26.somedice.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link MixedDiceGroup}.
 * 
 * @author Qwert26
 */
public class TestErrorCases extends TestMixedDiceGroup {
	@Test
	void constructorDisallowsNullArray() {
		assertThrows(Exception.class, () -> new MixedDiceGroup((IDie[]) null));
	}
}