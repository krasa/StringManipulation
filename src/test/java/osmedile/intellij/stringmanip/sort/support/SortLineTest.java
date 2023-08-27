package osmedile.intellij.stringmanip.sort.support;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import osmedile.intellij.stringmanip.align.ColumnAligner;

import static org.junit.Assert.assertEquals;
import static osmedile.intellij.stringmanip.sort.support.SortSettings.allFeaturesDisabled;

public class SortLineTest {
	private SortSettings enabledFeatures = new SortSettings(Sort.LINE_LENGTH_LONG_SHORT).preserveTrailingSpecialCharacters(true);
	private SortSettings disabledFeatures = allFeaturesDisabled(Sort.CASE_INSENSITIVE_A_Z);
	private SortSettings noTrailingChars = new SortSettings(Sort.LINE_LENGTH_LONG_SHORT).preserveTrailingSpecialCharacters(true).trailingChars("");
	private SortSettings onlyWhitespacesTrailingChars = new SortSettings(Sort.LINE_LENGTH_LONG_SHORT).preserveTrailingSpecialCharacters(true).trailingChars(" \t");

	@Test
	public void getTextForComparison() throws Exception {
		//@formatter:off
		assertThatWith(disabledFeatures).line("foo"                  ).isComparedAs("foo"      ) ;
		assertThatWith(disabledFeatures).line(" foo "                ).isComparedAs(" foo "    ) ;
		assertThatWith(disabledFeatures).line("\tfoo"                ).isComparedAs("\tfoo"    ) ;
		assertThatWith(disabledFeatures).line("\tfoo\n"              ).isComparedAs("\tfoo\n"  ) ;
		assertThatWith(disabledFeatures).line("\tfoo;"               ).isComparedAs("\tfoo;"   ) ;
		assertThatWith(disabledFeatures).line("\tfoo,"               ).isComparedAs("\tfoo,"   ) ;
		assertThatWith(disabledFeatures).line("\tfoo,;"              ).isComparedAs("\tfoo,;"  ) ;
		assertThatWith(disabledFeatures).line("\tfoo;,"              ).isComparedAs("\tfoo;,"  ) ;
		assertThatWith(disabledFeatures).line("\tfoo;,."             ).isComparedAs("\tfoo;,." ) ;
		assertThatWith(disabledFeatures).line("\tfoo.,;"             ).isComparedAs("\tfoo.,;" ) ;
		
		
		assertThatWith(enabledFeatures).line ("foo"                  ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line (" foo "                ).isComparedAs("foo "      ) ;
		assertThatWith(enabledFeatures).line ("\tfoo"                ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\t  foo;"             ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,"             ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,;"            ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,"       ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,."      ).isComparedAs("foo;,."   ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,."     ).isComparedAs("foo ;,."  ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,     " ).isComparedAs("foo "     ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo.,;"      ).isComparedAs("foo."     ) ;
		//@formatter:on

		enabledFeatures.setIgnoreLeadingCharactersEnabled(true);
		enabledFeatures.setIgnoreLeadingCharacters("#\\s*");
		assertThatWith(enabledFeatures).line("# jupyterlab-drawio = \">=0.9.0\"").isComparedAs("jupyterlab-drawio = \">=0.9.0\"");

		enabledFeatures.setIgnoreLeadingCharacters("#");
		assertThatWith(enabledFeatures).line(" # jupyterlab-drawio = \">=0.9.0\"").isComparedAs("jupyterlab-drawio = \">=0.9.0\"");
		assertThatWith(enabledFeatures).line("#foo#foo").isComparedAs("foo#foo");
		assertThatWith(enabledFeatures).line("foo#foo").isComparedAs("foo#foo");

		enabledFeatures.setIgnoreLeadingCharacters("\\s*;\\s*");

	}

	@Test
	public void transformTo() throws Exception {
		assertThatWith(noTrailingChars).line("     \t  foo   ").trasformedTo("bar  ").produces("     \t  bar  ");
		assertThatWith(noTrailingChars).line("").trasformedTo("bar ").produces("bar ");
		assertThatWith(noTrailingChars).line("foo").trasformedTo(" ").produces("");

		assertThatWith(onlyWhitespacesTrailingChars).line("     \t  foo   ;").trasformedTo("bar  ").produces("     \t  bar");
		assertThatWith(onlyWhitespacesTrailingChars).line("").trasformedTo("bar ").produces("bar");
		assertThatWith(onlyWhitespacesTrailingChars).line("foo").trasformedTo(" ").produces("");
	}

	@Test
	public void trailingDisabled() throws Exception {
//		@formatter:off
	String [] s =new String[]{	
			"(foo)      | [bar]     | {bar}     | ",
			"(foo )     | [bar]     | {bar}     | ",
			"(foo)      | [bar ]    | {bar }    | ",
			"(foo )     | [bar ]    | {bar }    | ",
			"(foo,)     | [bar]     | {bar}     | ",
			"(foo,)     | [bar ]    | {bar }    | ",
			"(foo, )    | [bar]     | {bar}     | ",
			"(foo, )    | [bar ]    | {bar }    | ",
			"(foo ,)    | [bar]     | {bar}     | ",
			"(foo ,)    | [bar ]    | {bar }    | ",
			"(foo , )   | [bar]     | {bar}     | ",
			"(foo , )   | [bar ]    | {bar }    | ",
			"(foo,)     | [bar;]    | {bar;}    | ",
			"(foo,)     | [bar ;]   | {bar ;}   | ",
			"(foo,)     | [bar; ]   | {bar; }   | ",
			"(foo,)     | [bar ; ]  | {bar ; }  | ",
			"(foo, )    | [bar;]    | {bar;}    | ",
			"(foo, )    | [bar ;]   | {bar ;}   | ",
			"(foo, )    | [bar; ]   | {bar; }   | ",
			"(foo, )    | [bar ; ]  | {bar ; }  | ",
			"(foo ,)    | [bar;]    | {bar;}    | ",
			"(foo ,)    | [bar ;]   | {bar ;}   | ",
			"(foo ,)    | [bar; ]   | {bar; }   | ",
			"(foo ,)    | [bar ; ]  | {bar ; }  | ",
			"(foo , )   | [bar;]    | {bar;}    | ",
			"(foo , )   | [bar ;]   | {bar ;}   | ",
			"(foo , )   | [bar; ]   | {bar; }   | ",
			"(foo , )   | [bar ; ]  | {bar ; }  | ",
			"(foo , )   | [bar ; X] | {bar ; X} | ",
			"(foo , , ) | [bar ; ]  | {bar ; }  | ",
	} ;
		//@formatter:on
		test(s, disabledFeatures);
		test(s, noTrailingChars);

	}

	@Test
	public void trailing() throws Exception {
//		@formatter:off
	String [] s =new String[]{	
			"(foo)      | [bar]     | {bar}       | ",
			"(foo )     | [bar]     | {bar}       | ",
			"(foo)      | [bar ]    | {bar }      | ",
			"(foo )     | [bar ]    | {bar }      | ",
			"(foo,)     | [bar]     | {bar,}      | ",
			"(foo,)     | [bar ]    | {bar ,}     | ",
			"(foo, )    | [bar]     | {bar, }     | ",
			"(foo, )    | [bar ]    | {bar , }    | ",
			"(foo ,)    | [bar]     | {bar,}      | ",
			"(foo ,)    | [bar ]    | {bar ,}     | ",
			"(foo , )   | [bar]     | {bar, }     | ",
			"(foo , )   | [bar ]    | {bar , }    | ",
			"(foo,)     | [bar;]    | {bar,}      | ",
			"(foo,)     | [bar ;]   | {bar ,}     | ",
			"(foo,)     | [bar; ]   | {bar,}      | ",
			"(foo,)     | [bar ; ]  | {bar ,}     | ",
			"(foo, )    | [bar;]    | {bar, }     | ",
			"(foo, )    | [bar ;]   | {bar , }    | ",
			"(foo, )    | [bar; ]   | {bar, }     | ",
			"(foo, )    | [bar ; ]  | {bar , }    | ",
			"(foo ,)    | [bar;]    | {bar,}      | ",
			"(foo ,)    | [bar ;]   | {bar ,}     | ",
			"(foo ,)    | [bar; ]   | {bar,}      | ",
			"(foo ,)    | [bar ; ]  | {bar ,}     | ",
			"(foo , )   | [bar;]    | {bar, }     | ",
			"(foo , )   | [bar ;]   | {bar , }    | ",
			"(foo , )   | [bar; ]   | {bar, }     | ",
			"(foo , )   | [bar ; ]  | {bar , }    | ",
			"(foo , )   | [bar ; X] | {bar ; X, } | ",
			"(foo , , ) | [bar]     | {bar, , }   | ",
			"(foo , , ) | [bar ]    | {bar , , }  | ",
			"(foo , , ) | [bar;]    | {bar, , }   | ",
			"(foo , , ) | [bar; ]   | {bar, , }   | ",
			"(foo , , ) | [bar ;]   | {bar , , }  | ",
			"(foo , , ) | [bar ; ]  | {bar , , }  | ",
	} ;
		//@formatter:on
		test(s, new SortSettings(null).preserveTrailingSpecialCharacters(true).trailingChars(",;"));
	}

	@Test
	public void withWhitespaceTrailingChar() throws Exception {
//		@formatter:off
	String [] s =new String[]{	

			"(foo)      | [bar]     | {bar}        | ",
			"(foo )     | [bar]     | {bar }       | ",
			"(foo)      | [bar ]    | {bar}        | ",
			"(foo )     | [bar ]    | {bar }       | ",
			"(foo,)     | [bar]     | {bar,}       | ",
			"(foo,)     | [bar ]    | {bar,}       | ",
			"(foo, )    | [bar]     | {bar, }      | ",
			"(foo, )    | [bar ]    | {bar, }      | ",
			"(foo ,)    | [bar]     | {bar ,}      | ",
			"(foo ,)    | [bar ]    | {bar ,}      | ",
			"(foo , )   | [bar]     | {bar , }     | ",
			"(foo , )   | [bar ]    | {bar , }     | ",
			"(foo,)     | [bar;]    | {bar,}       | ",
			"(foo,)     | [bar ;]   | {bar,}       | ",
			"(foo,)     | [bar; ]   | {bar,}       | ",
			"(foo,)     | [bar ; ]  | {bar,}       | ",
			"(foo, )    | [bar;]    | {bar, }      | ",
			"(foo, )    | [bar ;]   | {bar, }      | ",
			"(foo, )    | [bar; ]   | {bar, }      | ",
			"(foo, )    | [bar ; ]  | {bar, }      | ",
			"(foo ,)    | [bar;]    | {bar ,}      | ",
			"(foo ,)    | [bar ;]   | {bar ,}      | ",
			"(foo ,)    | [bar; ]   | {bar ,}      | ",
			"(foo ,)    | [bar ; ]  | {bar ,}      | ",
			"(foo , )   | [bar;]    | {bar , }     | ",
			"(foo , )   | [bar ;]   | {bar , }     | ",
			"(foo , )   | [bar; ]   | {bar , }     | ",
			"(foo , )   | [bar ; ]  | {bar , }     | ",
			"(foo , )   | [bar ; X] | {bar ; X , } | ",
			"(foo , , ) | [bar]     | {bar , , }   | ",
			"(foo , , ) | [bar ]    | {bar , , }   | ",
			"(foo , , ) | [bar;]    | {bar , , }   | ",
			"(foo , , ) | [bar; ]   | {bar , , }   | ",
			"(foo , , ) | [bar ;]   | {bar , , }   | ",
			"(foo , , ) | [bar ; ]  | {bar , , }   | ",
	} ;
		//@formatter:on
		test(s, new SortSettings(null).preserveTrailingSpecialCharacters(true).trailingChars(" ,;"));
	}

	private void printAlignedResult() {
		System.out.println("----ALIGNED OUTPUT---");
		System.out.println(new ColumnAligner("|").align(output.toString()));
		output.setLength(0);
		System.out.println("----ALIGNED OUTPUT END---");
	}

	protected void test(String[] s, SortSettings enabledFeatures) {
		for (String s1 : s) {
			String[] split = s1.split("\\|");
			String input = StringUtils.replaceChars(split[0].trim(), "()[]{}", "");
			String target = StringUtils.replaceChars(split[1].trim(), "()[]{}", "");
			String expected = StringUtils.replaceChars(split[2].trim(), "()[]{}", "");

			assertThatWith(enabledFeatures).line(input).trasformedTo(target).print(split[0], split[1]).produces(expected);
		}
		printAlignedResult();
	}

	static StringBuilder output = new StringBuilder();

	private static SetupStep assertThatWith(SortSettings sortSettings) {
		return new SetupStep(sortSettings);
	}

	private static class SetupStep {
		private final SortSettings sortSettings;

		public SetupStep(SortSettings sortSettings) {
			this.sortSettings = sortSettings;
		}

		public AssertStep line(String source) {
			return new AssertStep(new SortLine(source, sortSettings));
		}

		private class AssertStep {
			private final SortLine line;

			public AssertStep(SortLine line) {
				this.line = line;
			}

			public void isComparedAs(String to) {
				assertEquals(to, line.getTextForComparison());
			}

			public AssertTransformTo trasformedTo(String to) {
				return new AssertTransformTo(new SortLine(to, sortSettings));
			}

			private class AssertTransformTo {
				private final SortLine to;

				public AssertTransformTo(SortLine to) {
					this.to = to;
				}

				public void produces(String expectedResult) {
					String result = line.transformTo(to).getText();
					assertEquals("expected={" + expectedResult + "} actual={" + result + "}", expectedResult, result);
				}

				public AssertTransformTo print(String s, String s1) {
					String str = "\"" + s + " | " + s1 + " | {" + line.transformTo(to) + "}" + "      |\",";
					System.out.println(str);
					output.append(str).append('\n');
					return this;
				}
			}
		}

	}

}