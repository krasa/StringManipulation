package osmedile.intellij.stringmanip.align;

import com.google.common.base.Joiner;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AlignToColumnsActionTest {

	protected String align(String text, String... separator) {
		ColumnAlignerModel model = new ColumnAlignerModel(separator);
		model.setKeepLeadingIndent(true);
		model.setRightAlignNumbers(false);
//		model.setTrimLines(true);
		return new ColumnAligner(model).align(text);
	}

	@Test
	public void testSpace1() {
		String process = align("foo      bar"
				, " ");
		assertThat(process, is("foo bar\n"));
	}

	@Test
	public void test() {
		String process = align("foo 1     | foo 2"
				, "|");
		assertThat(process, is("foo 1 | foo 2\n"));
	}


	@Test
	public void test2() {
		String process = align("foo 1     | foo 2|"
				, "|");
		assertThat(process, is("foo 1 | foo 2 |\n"));
	}

	@Test
	public void test3() {
		String process = align("|foo 1     | foo 2"
				, "|");
		assertThat(process, is("| foo 1 | foo 2\n"));
	}

	@Test
	public void test4() {
		String process = align("| foo 1 | foo 2\n| foo 1 | foo 2\n"
				, "|");
		assertThat(process, is("| foo 1 | foo 2\n| foo 1 | foo 2\n"));
	}


	@Test
	public void test5() {
		String process = align("| foo 1 | foo 2| foo 3\n| foo 1 | foo 2\n"
				, "|");
		assertThat(process, is("| foo 1 | foo 2 | foo 3\n| foo 1 | foo 2\n"));
	}

	@Test
	public void test6() {
		String process = align("| foo 1 | foo 2\n| foo 1 | foo 2 | foo 3\n"
				, "|");
		assertThat(process, is("| foo 1 | foo 2\n| foo 1 | foo 2 | foo 3\n"));
	}

	@Test
	public void test7() {
		// @formatter:off
		String notFormattedText =
			"   |foo 1|foooooooooo 2|   foo 3|foo 4 \n"
				+ "  |val 11   | val 12|val 13| val 14|val 15|\n"
				+ " |foooooooooo| val 22|val 33|val34|fooooooooooooooo                       |";

		String expectedText =
				"  | foo 1       | foooooooooo 2 | foo 3  | foo 4\n"
						+ "  | val 11      | val 12        | val 13 | val 14 | val 15           |\n"
						+ "  | foooooooooo | val 22        | val 33 | val34  | fooooooooooooooo |\n";
		// @formatter:on

		String process = align(notFormattedText
				, "|");
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}


	@Test
	public void test8() {
		//  @formatter:off
		String notFormattedText =
			"     foo 1|foooooooooo 2|   foo 3|foo 4 \n"
				+ "     val 11| val 12|val 13| val 14|val 15|\n"
				+ "     foooooooooo| val 22|val 33|val34|fooooooooooooooo                       |\n";

		String expectedText =
				"     foo 1       | foooooooooo 2 | foo 3  | foo 4\n"
						+ "     val 11      | val 12        | val 13 | val 14 | val 15           |\n"
						+ "     foooooooooo | val 22        | val 33 | val34  | fooooooooooooooo |\n";
		// @formatter:on

		String process = align(notFormattedText
				, "|");
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test9() {
		//  @formatter:off
		String notFormattedText =
				"| foo   1 | foooooooooo 2 | foo    3 | foo            4 | val 11      | val\n" +
						"12 | val 13  | val 14        | val 15   |                		| foooooooooo | val   22 | val 33 | val34 | fooooooooooooooo |";

		String expectedText =
				"   | foo   1 | foooooooooo 2 | foo    3 | foo            4 | val 11      | val\n" +
						"12 | val 13  | val 14        | val 15   |                  | foooooooooo | val   22 | val 33 | val34 | fooooooooooooooo |\n";
		// @formatter:on

		String process = align(notFormattedText, "|");
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test10() {
		//  @formatter:off
		String notFormattedText =
			"   1  2  345  67 89 \n" +
				"1 2 3  4  5 6  7 8  9     \n";

		String expectedText =
			"1 2 345 67 89 \n" +
				"1 2 3   4  5  6 7 8 9 \n";
		// @formatter:on

		String process = align(notFormattedText, " ");
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test10r() {
		//  @formatter:off
		String notFormattedText =
				"   1  2  345  67 89 \n" +
						"1 2 3  4  5 6  7 8  9     \n";

		String expectedText =
				"1 2 345 67 89 \n" +
						"1 2   3  4  5 6 7 8 9 \n";
		// @formatter:on

		ColumnAlignerModel model = new ColumnAlignerModel(" ");
		model.setKeepLeadingIndent(true);
		model.setRightAlignNumbers(true);
		String process = new ColumnAligner(model).align(notFormattedText);
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test10r2() {
		//  @formatter:off
		String notFormattedText =
				"   a  a  aaa  aa aa \n" +
						"a a a  a  a 6  7 8  9     \n";

		String expectedText =
				"a a aaa aa aa \n" +
						"a a   a  a  a 6 7 8 9 \n";
		// @formatter:on

		ColumnAlignerModel model = new ColumnAlignerModel(" ");
		model.setKeepLeadingIndent(true);
		model.setRightAlign(true);
		String process = new ColumnAligner(model).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test11() {
		//  @formatter:off
		String notFormattedText =
			"   1  2  345  67 89 \n" +
				"1 2 3  4  5 6  7 8  9 \n" +
				"    \n";

		String expectedText =
			"1 2 345 67 89 \n" +
				"1 2 3   4  5  6 7 8 9 \n\n";
		// @formatter:on

		String process = align(notFormattedText, " ");
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test12() {
		// @formatter:off
		String notFormattedText =
			"$table->addColumn('field_01', 'boolean', ['default' => false]);\n" +
				"$table->addColumn('field_02', 'boolean', ['default' => false]);\n" +
				"$table->addColumn('field_03', 'datetime', ['notnull' => false]);\n" +
				"$table->addColumn('field_long_name', 'datetime', ['notnull' => false]);";

		String expectedText =
			"$table->addColumn('field_01',        'boolean',  ['default' => false]);\n" +
				"$table->addColumn('field_02',        'boolean',  ['default' => false]);\n" +
				"$table->addColumn('field_03',        'datetime', ['notnull' => false]);\n" +
				"$table->addColumn('field_long_name', 'datetime', ['notnull' => false]);\n";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel(",");
		columnAlignerModel.setAlignBy(ColumnAlignerModel.Align.VALUES);
		columnAlignerModel.setSpaceBeforeSeparator(false);
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test13() {
		// @formatter:off
		String notFormattedText =
			"93 WORD_LOWERCASE -> CAMEL_CASE: foo350a -> foo350A\n" +
				"97 _UNKNOWN -> DOT: dark border350A -> dark.border350.a";

		String expectedText =
			"93 WORD_LOWERCASE -> CAMEL_CASE : foo350a         -> foo350A\n" +
				"97 _UNKNOWN       -> DOT        : dark border350A -> dark.border350.a\n";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel(":", "->");
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test13a() {
		// @formatter:off
		String notFormattedText =
				"93 WORD_LOWERCASE -> CAMEL_CASE: foo350a -> foo350A\n" +
						"97 _UNKNOWN -> DOT: dark border350A -> dark.border350.a\n";

		String expectedText =
				"93 WORD_LOWERCASE -> CAMEL_CASE : foo350a         -> foo350A\n" +
						"97 _UNKNOWN       -> DOT        : dark border350A -> dark.border350.a\n";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel("->", ":");
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test_104() {
		// @formatter:off
		String notFormattedText =
				"   let mut state_stack = Vec::new();\n" +
						"   let epsilon_links = Vec::new();\n" +
						"   let links = Vec::new();\n" +
						"   let name = Some(String::from(\"ROOT\"));\n" +
						"   let init_state = State{epsilon_links,links,name};\n";

		String expectedText =
				"   let mut state_stack = Vec::new();\n" +
						"   let epsilon_links   = Vec::new();\n" +
						"   let links           = Vec::new();\n" +
						"   let name            = Some(String::from(\"ROOT\"));\n" +
						"   let init_state      = State{epsilon_links,links,name};\n";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel("=");
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}


	@Test
	public void test13_sequentially_ugly() {
		// @formatter:off
		String notFormattedText =
				"93 WORD_LOWERCASE -> CAMEL_CASE: foo350a -> foo350A\n" +
						"97 _UNKNOWN -> DOT: dark border350A -> dark.border350.a";

		String expectedText =
			"93 WORD_LOWERCASE -> CAMEL_CASE : foo350a               -> foo350A\n" +
				"97 _UNKNOWN       -> DOT              : dark border350A -> dark.border350.a\n";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel(":", "->");
		columnAlignerModel.setSequentialProcessing(true);
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test14() {
		String process = align("|| foo 1 ||| foo 2||\n| foo 1 | foo 2 | foo 3\n"
				, "|");
		assertThat(process, is("|       | foo 1 |       | | foo 2 | |\n" +
				"| foo 1 | foo 2 | foo 3\n"));
	}

	@Test
	public void test15() {
		String process = align("     || foo 1 ||| foo 2||\n| foo 1 | foo 2 | foo 3\n"
				, "|");
		assertThat(process, is("|       | foo 1 |       | | foo 2 | |\n" +
				"| foo 1 | foo 2 | foo 3\n"));
	}


	@Test
	public void test16() {
		String process = align("foo  ->    bar   <-    foo\n" +
						" foo<-bar->foo \n",
				"->", "<-", "");
		assertThat(process, is(
				"foo -> bar <- foo\n" +
						"foo <- bar -> foo\n"
		));
	}

	@Test
	public void testSpace17() {
		String process = align("zm9vig-jhcg=="
				, ",", "");
		assertThat(process, is("zm9vig-jhcg==\n"));
	}

	@Test
	public void test18() {
		// @formatter:off
		String notFormattedText =
				"            <li>Convert diacritics (accents) to ASCII</li>\n" +
						"            <li>Convert non-ASCII to escaped Unicode</li>";

		String expectedText =
				"< li > Convert diacritics (accents) to      ASCII   < /li >\n" +
						"< li > Convert non-ASCII  to        escaped Unicode < /li >\n";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel("<", ">", " ");
		columnAlignerModel.setKeepLeadingIndent(false);
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test19() {
		// @formatter:off
		String notFormattedText =
"1|2---3 4-5---6---\n" +
"1|2123| ---3 4-445||\n" +
"1|25---3 4-5 |66";

		String expectedText =
"1 | 2    --- 3 4-5 --- 6 ---\n" +
"1 | 2123 |         --- 3    4-445 | |\n" +
"1 | 25   --- 3 4-5       |  66\n";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel("---", "|", " ");
		columnAlignerModel.setRightAlignNumbers(false);
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}
	@Test
	public void test20() {
		// @formatter:off
		String notFormattedText =
"foo bar\n" +
"foo bar" ;

String expectedText =
"foo bar\n" +
"foo bar\n" ;

		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel("");
		String process = new ColumnAligner(columnAlignerModel).align(notFormattedText);
		assertThat(process, is(expectedText));
	}

	@Test
	public void test21() {
		// @formatter:off

		List<String> lines = new ArrayList<>();
		lines.add("1|2|3|4|5|");
		lines.add("1-2-3-4-5-");


		String expectedText =
			"1 | 2 | 3 | 4 | 5 |\n" +
			"1 - 2 - 3 - 4 - 5 -";
		// @formatter:on


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel("|", "-");
		List<String> align = new ColumnAligner(columnAlignerModel).align(lines);
		String join = Joiner.on("\n").join(align);

		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(Joiner.on("\n").join(lines));
		System.out.println("EXPECTED >>>>>>>>>>>");
		System.out.println(expectedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(join);

		assertThat(join, is(expectedText));
	}
}