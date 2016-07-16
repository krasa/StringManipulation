package osmedile.intellij.stringmanip.styles;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ToCamelCaseActionTest {

    protected ToCamelCaseAction action;

    @Test
    public void testTransform() throws Exception {
        action = new ToCamelCaseAction(false);
		assertEquals("foo", action.transformByLine("foo"));
		assertEquals("!@#$%^&*)(*&|+!!!!!foo!!!!", action.transformByLine("!@#$%^&*)(*&|+!!!!!FOO!!!!"));
		assertEquals("public", action.transformByLine("PUBLIC"));

		assertEquals("testFlexibleQuery", action.transformByLine("testFLEXIBLE_QUERY"));
        assertEquals("testFlexibleQueryProductsForWorkflowAttachment",
				action.transformByLine("testFlexibleQuery_PRODUCTS_FOR_WORKFLOW_ATTACHMENT"));

		assertEquals("thisIsAText", action.transformByLine("This is a text"));

        //this is ugly but nothing can be done about that.
		assertEquals("whOAhATeSt", action.transformByLine("WhOAh a TeSt"));
		assertEquals("whOAhATeSt", action.transformByLine("WhOAh_a_TeSt"));
		assertEquals("whOAhATeSt", action.transformByLine("WhOAh a_TeSt"));
		assertEquals("'closeBsAlert'", action.transformByLine("'Close Bs Alert'"));
		assertEquals("\"closeBsAlert\"", action.transformByLine("\"Close Bs Alert\""));
    }
}