package osmedile.intellij.stringmanip.sort.support;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static osmedile.intellij.stringmanip.sort.support.SortSettings.allFeaturesDisabled;

public class LineTest {
	private SortSettings enabledFeatures = new SortSettings(Sort.LINE_LENGTH_LONG_SHORT);
	private SortSettings disabledFeatures = allFeaturesDisabled(Sort.CASE_INSENSITIVE_A_Z);

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
		assertThatWith(enabledFeatures).line (" foo "                ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\tfoo"                ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\tfoo\n"              ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\t  foo;"             ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,"             ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,;"            ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,"       ).isComparedAs("foo"      ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,."      ).isComparedAs("foo;,."   ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,."     ).isComparedAs("foo ;,."  ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,     " ).isComparedAs("foo "     ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo.,;"      ).isComparedAs("foo."     ) ;
		//@formatter:on
	}

	@Test
	public void transformTo() throws Exception {
//		@formatter:off
		assertThatWith(disabledFeatures).line("foo"                  ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line(" foo "                ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("\tfoo"                ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("\tfoo\n"              ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("\t  foo;"             ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("\t  foo,"             ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("\t  foo,;"            ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("     \t  foo;,"       ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("     \t  foo;,."      ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("     \t  foo ;,."     ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("     \t  foo ;,     " ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		assertThatWith(disabledFeatures).line("     \t  foo.,;"      ).trasformedTo( "bar"                 ).produces("bar"             ) ;
		
		
		assertThatWith(enabledFeatures).line ("foo    "                  ).trasformedTo( "bar"             ).produces("bar"             ) ;
		assertThatWith(enabledFeatures).line (" foo "                ).trasformedTo( "bar            "     ).produces(" bar"            ) ; //trailing whitespaces trimmed - feature ;)
		assertThatWith(enabledFeatures).line ("\tfoo"                ).trasformedTo( "bar"                 ).produces("\tbar"           ) ;
		assertThatWith(enabledFeatures).line ("\tfoo\n"              ).trasformedTo( "bar"                 ).produces("\tbar"           ) ;
		assertThatWith(enabledFeatures).line ("\t  foo;"             ).trasformedTo( "bar"                 ).produces("\t  bar;"        ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,"             ).trasformedTo( "bar"                 ).produces("\t  bar,"        ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,;"            ).trasformedTo( "bar"                 ).produces("\t  bar,;"       ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,"       ).trasformedTo( "bar"                 ).produces("     \t  bar;,"  ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,."      ).trasformedTo( "bar"                 ).produces("     \t  bar"    ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,."     ).trasformedTo( "bar"                 ).produces("     \t  bar"    ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,     " ).trasformedTo( "bar"                 ).produces("     \t  bar;,"  ) ;    
		assertThatWith(enabledFeatures).line ("     \t  foo.,;"      ).trasformedTo( "bar"                 ).produces("     \t  bar,;"  ) ;
		
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "bar"                 ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line (" foo "                ).trasformedTo( "bar "                ).produces(" bar"             ) ;
		assertThatWith(enabledFeatures).line ("\tfoo"                ).trasformedTo( "\tbar"               ).produces("\tbar"            ) ;
		assertThatWith(enabledFeatures).line ("\tfoo\n"              ).trasformedTo( "\tbar\n"             ).produces("\tbar"            ) ;
		assertThatWith(enabledFeatures).line ("\t  foo;"             ).trasformedTo( "\t  bar;"            ).produces("\t  bar;"         ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,"             ).trasformedTo( "\t  bar,"            ).produces("\t  bar,"         ) ;
		assertThatWith(enabledFeatures).line ("\t  foo,;"            ).trasformedTo( "\t  bar,;"           ).produces("\t  bar,;"        ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,"       ).trasformedTo( "     \t  bar;,"      ).produces("     \t  bar;,"   ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo;,."      ).trasformedTo( "     \t  bar,;|"     ).produces("     \t  bar,;|"  ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,."     ).trasformedTo( "     \t  bar ,;|"    ).produces("     \t  bar ,;|" ) ;
		assertThatWith(enabledFeatures).line ("     \t  foo ;,     " ).trasformedTo( "     \t  bar ;,  "   ).produces("     \t  bar ;,"  ) ;  
		assertThatWith(enabledFeatures).line ("     \t  foo.,;"      ).trasformedTo( "     \t  bar|,;"     ).produces("     \t  bar|,;"  ) ;
		
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "bar"                 ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( " bar "               ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "\tbar"               ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "\tbar\n"             ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "\t  bar;"            ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "\t  bar,"            ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "\t  bar,;"           ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "     \t  bar;,"      ).produces("bar"              ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "     \t  bar,;|"     ).produces("bar,;|"           ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "     \t  bar ,;|"    ).produces("bar ,;|"          ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "     \t  bar ;,  "   ).produces("bar "             ) ;
		assertThatWith(enabledFeatures).line ("foo"                  ).trasformedTo( "     \t  bar|,;"     ).produces("bar|"             ) ;
		//@formatter:on
	}

	private static SetupStep assertThatWith(SortSettings sortSettings) {
		return new SetupStep(sortSettings);
	}

	private static class SetupStep {
		private final SortSettings sortSettings;

		public SetupStep(SortSettings sortSettings) {
			this.sortSettings = sortSettings;
		}

		public AssertStep line(String foo) {
			return new AssertStep(new Line(foo, sortSettings));
		}

		private class AssertStep {
			private final Line line;

			public AssertStep(Line line) {
				this.line = line;
			}

			public void isComparedAs(String foo) {
				assertEquals(foo, line.getTextForComparison());
			}

			public AssertTransformTo trasformedTo(String bar) {
				return new AssertTransformTo(new Line(bar, sortSettings));
			}

			private class AssertTransformTo {
				private final Line to;

				public AssertTransformTo(Line to) {
					this.to = to;
				}

				public void produces(String expectedResult) {
					assertEquals(expectedResult, line.transformTo(to));
				}
			}
		}

	}

}