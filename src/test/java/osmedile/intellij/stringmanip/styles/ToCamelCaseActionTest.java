package osmedile.intellij.stringmanip.styles;

import org.junit.Test;
import osmedile.intellij.stringmanip.styles.switching.ToWordCapitalizedOrCamelCaseAction;

import static org.junit.Assert.assertEquals;

public class ToCamelCaseActionTest extends CaseSwitchingTest {
	protected ToWordCapitalizedOrCamelCaseAction action;
	protected ToCamelCaseAction toCamelCaseAction;

    @Test
    public void testTransform() throws Exception {
		action = new ToWordCapitalizedOrCamelCaseAction(false);
		toCamelCaseAction = new ToCamelCaseAction(false);

		assertEquals("\"submittedBy\",\"owner\",\"creationDate\",\"rootCause\",", action.test_transformByLine("\"Submitted By\",\"Owner\",\"Creation Date\",\"Root Cause\","));
		assertEquals("\"submittedBy\",\"owner\",\"creationDate\",\"rootCause\",", toCamelCaseAction.test_transformByLine("\"Submitted By\",\"Owner\",\"Creation Date\",\"Root Cause\","));
		assertEquals("\"submittedBy\", \"owner\", \"creationDate\", \"rootCause\",", toCamelCaseAction.test_transformByLine("\"Submitted By\", \"Owner\", \"Creation Date\", \"Root Cause\","));
		assertEquals("Foo", action.test_transformByLine("foo"));
		assertEquals("foo", action.test_transformByLine("Foo"));
		assertEquals("Foo", action.test_transformByLine("FOO"));
		assertEquals("!@#$%^&*)(*&|+!!!!!Foo!!!!", action.test_transformByLine("!@#$%^&*)(*&|+!!!!!FOO!!!!"));

		assertEquals("Test Flexible Query", action.test_transformByLine("testFLEXIBLE_QUERY"));
		assertEquals("Test Flexible Query Products For Workflow Attachment",
				action.test_transformByLine("testFlexibleQuery_PRODUCTS_FOR_WORKFLOW_ATTACHMENT"));

		assertEquals("thisIsAText", action.test_transformByLine("This Is A Text"));
		assertEquals("This Is A Text", action.test_transformByLine("This is a text"));

		//this is ugly but nothing can be done about that.
		assertEquals("Wh Oah A Te St", action.test_transformByLine("WhOAh a TeSt"));
//		assertEquals("whOAhATeSt", action.transformByLine("WhOAh_a_TeSt"));
		assertEquals("Wh Oah A Te St", action.test_transformByLine("WhOAh_a_TeSt"));
		assertEquals("whOAhATeSt", action.test_transformByLine("Wh OAh_a_te St"));
//		assertEquals("whOAhATeSt", action.transformByLine("WhOAh a_TeSt"));
		assertEquals("Wh Oah A Te St", action.test_transformByLine("WhOAh a_TeSt")); //ugly
		assertEquals("Wh Oah A Te St", action.test_transformByLine("WhOAh a.TeSt")); //ugly
		assertEquals("Wh Oah A Te St", action.test_transformByLine("WhOAh a-TeSt")); //ugly

		assertEquals("Whoah A Test", action.test_transformByLine("whoah a_test"));  //ugly
		assertEquals("Whoah A Test", action.test_transformByLine("whoah a.test"));  //ugly
		assertEquals("Whoah A Test", action.test_transformByLine("whoah a-test"));  //ugly

//		assertEquals("Whoah A_test", action.transformByLine("WHOAH A_TEST"));
//		assertEquals("Whoah A.test", action.transformByLine("WHOAH A.TEST"));
//		assertEquals("Whoah A-test", action.transformByLine("WHOAH A-TEST"));

		assertEquals("'closeBsAlert'", action.test_transformByLine("'Close Bs Alert'"));
		assertEquals("\"closeBsAlert\"", action.test_transformByLine("\"Close Bs Alert\""));
	}
}