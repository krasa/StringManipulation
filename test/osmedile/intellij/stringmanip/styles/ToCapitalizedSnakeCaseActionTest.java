package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToCapitalizedSnakeCaseActionTest extends CaseSwitchingTest {
	protected ToCapitalizedSnakeCaseAction action;

	@Test
	public void testTransform() throws Exception {
		action = new ToCapitalizedSnakeCaseAction(false);
		assertEquals("11_Foo22_Foo_Bar33_Bar44_Foo55_X6_Y7_Z", action.test_transformByLine("11_foo22_foo_bar33_bar44_foo55_x6_y7_z"));
		assertEquals("11_Foo22_Foo_Bar33_Bar44_Foo55_X6_Y7_Z", action.test_transformByLine("11_FOO22_FOO_BAR33_BAR44_FOO55_X6_Y7_Z"));
		assertEquals("Should_Be_Able_To_Search_Without_Filtering_Based_On_Bonded_Stock_Holdings", action.test_transformByLine("should be able to search without filtering based on bonded stock holdings"));
		assertEquals("Should_Be_Able_To_Search_Without_Filtering_Based_On_Bonded_Stock_Holdings", action.test_transformByLine("ShouldBeAbleToSearchWithoutFilteringBasedOnBondedStockHoldings"));

	}
}