package osmedile.intellij.stringmanip.align;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AlignToColumnsActionTest {

	private static final ColumnAligner COLUMN_ALIGNER = new ColumnAligner();

	@Test
	public void testSpace1() {
		String process = COLUMN_ALIGNER.reformat(" ", "foo      bar");
		assertThat(process, is("foo bar"));
	}

	@Test
	public void test() {
		String process = COLUMN_ALIGNER.reformat("|", "foo 1     | foo 2");
		assertThat(process, is("foo 1 | foo 2"));
	}

	@Test
	public void test2() {
		String process = COLUMN_ALIGNER.reformat("|", "foo 1     | foo 2|");
		assertThat(process, is("foo 1 | foo 2 |"));
	}

	@Test
	public void test3() {
		String process = COLUMN_ALIGNER.reformat("|", "|foo 1     | foo 2");
		assertThat(process, is("| foo 1 | foo 2"));
	}

	@Test
	public void test4() {
		String process = COLUMN_ALIGNER.reformat("|", "| foo 1 | foo 2\n| foo 1 | foo 2\n");
		assertThat(process, is("| foo 1 | foo 2\n| foo 1 | foo 2\n"));
	}

	@Test
	public void test5() {
		String process = COLUMN_ALIGNER.reformat("|", "| foo 1 | foo 2| foo 3\n| foo 1 | foo 2\n");
		assertThat(process, is("| foo 1 | foo 2 | foo 3\n| foo 1 | foo 2\n"));
	}

	@Test
	public void test6() {
		String process = COLUMN_ALIGNER.reformat("|", "| foo 1 | foo 2\n| foo 1 | foo 2 | foo 3\n");
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
				  "| foo 1       | foooooooooo 2 | foo 3  | foo 4\n"
				+ "| val 11      | val 12        | val 13 | val 14 | val 15           |\n"
				+ "| foooooooooo | val 22        | val 33 | val34  | fooooooooooooooo |";
		// @formatter:on

		String process = COLUMN_ALIGNER.reformat("|", notFormattedText);
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
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
				  "foo 1       | foooooooooo 2 | foo 3  | foo 4\n"
				+ "val 11      | val 12        | val 13 | val 14 | val 15           |\n"
				+ "foooooooooo | val 22        | val 33 | val34  | fooooooooooooooo |\n";
		// @formatter:on

		String process = COLUMN_ALIGNER.reformat("|", notFormattedText);
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
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
				"12 | val 13  | val 14        | val 15   |                  | foooooooooo | val   22 | val 33 | val34 | fooooooooooooooo |";
		// @formatter:on

		String process = COLUMN_ALIGNER.reformat("|", notFormattedText);
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}

	@Test
	public void test10() {
		//  @formatter:off
		String notFormattedText =
"   1  2  345  67 89 \n" +
"1 2 3  4  5 6  7 8  9     ";

		String expectedText = 
"1 2 345 67 89 \n" +
"1 2 3   4  5  6 7 8 9 ";
		// @formatter:on

		String process = COLUMN_ALIGNER.reformat(" ", notFormattedText);
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
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
"     ";

		String expectedText = 
"1 2 345 67 89 \n" +
"1 2 3   4  5  6 7 8 9 \n";
		// @formatter:on

		String process = COLUMN_ALIGNER.reformat(" ", notFormattedText);
		System.out.println("INPUT >>>>>>>>>>>");
		System.out.println(notFormattedText);
		System.out.println("RESULT >>>>>>>>>>>");
		System.out.println(process);

		assertThat(process, is(expectedText));
	}
}
