package osmedile.intellij.stringmanip.sort.support;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static osmedile.intellij.stringmanip.sort.support.SortSettings.allFeaturesDisabled;

public class LineTest {
	private SortSettings allFeaturesEnabled = new SortSettings(Sort.LINE_LENGTH_LONG_SHORT);
	private SortSettings allFeaturesDisabled = allFeaturesDisabled(Sort.CASE_INSENSITIVE_A_Z);

	@Test
	public void getTextForComparison() throws Exception {
		assertThatWith(allFeaturesDisabled).input("foo").isComparedAs("foo");
		assertThatWith(allFeaturesDisabled).input(" foo ").isComparedAs(" foo ");
		assertThatWith(allFeaturesDisabled).input("\tfoo").isComparedAs("\tfoo");
		assertThatWith(allFeaturesDisabled).input("\tfoo\n").isComparedAs("\tfoo\n");
		assertThatWith(allFeaturesDisabled).input("\tfoo;").isComparedAs("\tfoo;");
		assertThatWith(allFeaturesDisabled).input("\tfoo,").isComparedAs("\tfoo,");
		assertThatWith(allFeaturesDisabled).input("\tfoo,;").isComparedAs("\tfoo,;");
		assertThatWith(allFeaturesDisabled).input("\tfoo;,").isComparedAs("\tfoo;,");
		assertThatWith(allFeaturesDisabled).input("\tfoo;,.").isComparedAs("\tfoo;,.");
		assertThatWith(allFeaturesDisabled).input("\tfoo.,;").isComparedAs("\tfoo.,;");


		assertThatWith(allFeaturesEnabled).input("foo").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input(" foo ").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input("\tfoo").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input("\tfoo\n").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input("\t  foo;").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input("\t  foo,").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input("\t  foo,;").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input("     \t  foo;,").isComparedAs("foo");
		assertThatWith(allFeaturesEnabled).input("     \t  foo;,.").isComparedAs("foo;,.");
		assertThatWith(allFeaturesEnabled).input("     \t  foo ;,.").isComparedAs("foo ;,.");
		assertThatWith(allFeaturesEnabled).input("     \t  foo ;,     ").isComparedAs("foo ");
		assertThatWith(allFeaturesEnabled).input("     \t  foo.,;").isComparedAs("foo.");
	}


	private static SetupStep assertThatWith(SortSettings sortSettings) {
		return new SetupStep(sortSettings);
	}

	private static class SetupStep {
		private final SortSettings sortSettings;

		public SetupStep(SortSettings sortSettings) {
			this.sortSettings = sortSettings;
		}

		public AssertStep input(String foo) {
			return new AssertStep(new Line(foo, sortSettings));
		}

		private static class AssertStep {
			private final Line line;

			public AssertStep(Line line) {
				this.line = line;
			}

			public void isComparedAs(String foo) {
				assertEquals(foo, line.getTextForComparison());
			}
		}
	}


	@Test
	public void transformTo() throws Exception {
	}


}