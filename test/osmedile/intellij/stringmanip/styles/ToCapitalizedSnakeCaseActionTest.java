package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToCapitalizedSnakeCaseActionTest extends CaseSwitchingTest {
	protected ToCapitalizedSnakeCaseAction action;

	@Test
	public void testTransform() throws Exception {
		action = new ToCapitalizedSnakeCaseAction(false);
		assertEquals("Should_Be_Able_To_Search_Without_Filtering_Based_On_Bonded_Stock_Holdings", action.transformByLine("should be able to search without filtering based on bonded stock holdings"));
		assertEquals("Should_Be_Able_To_Search_Without_Filtering_Based_On_Bonded_Stock_Holdings", action.transformByLine("ShouldBeAbleToSearchWithoutFilteringBasedOnBondedStockHoldings"));

	}
}