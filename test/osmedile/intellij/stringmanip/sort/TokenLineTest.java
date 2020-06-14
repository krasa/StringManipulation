package osmedile.intellij.stringmanip.sort;

import org.junit.Test;
import osmedile.intellij.stringmanip.sort.tokens.SortTokensModel;
import osmedile.intellij.stringmanip.sort.tokens.TokenLine;

import static org.junit.Assert.*;

public class TokenLineTest {
	@Test
	public void t() {
		SortTokensModel model = new SortTokensModel(",");
		assertEquals("aaa, bbb, zzz", new TokenLine("zzz, aaa, bbb", model).getSortedText());
		assertEquals("aaa,bbb,zzz", new TokenLine("zzz,aaa,bbb", model).getSortedText());
		assertEquals(",aaa, bbb, zzz", new TokenLine(",zzz, aaa, bbb", model).getSortedText());
		assertEquals(",aaa, bbb, zzz,", new TokenLine(",zzz, aaa, bbb,", model).getSortedText());
		assertEquals(", aaa, bbb, zzz,", new TokenLine(", zzz, aaa, bbb,", model).getSortedText());
		assertEquals(", aaa, bbb, zzz, ",  new TokenLine(", zzz, aaa, bbb, ", model).getSortedText());
		assertEquals(" , aaa, bbb, zzz, ", new TokenLine(" , zzz, aaa, bbb, ", model).getSortedText());
		assertEquals(" , aaa , bbb , zzz , ", new TokenLine(" , zzz , aaa , bbb , ", model).getSortedText());
		assertEquals("  ,  aaa  ,  bbb  ,  zzz  ,  ", new TokenLine("  ,  zzz  ,  aaa  ,  bbb  ,  ", model).getSortedText());

//		model.getSortSettings().preserveLeadingSpaces(false);
//		assertEquals(" aaa, bbb,zzz", new TokenLine("zzz, aaa, bbb", model).getSortedText());
//		assertEquals("  ,  aaa  ,  bbb  ,  zzz  ,  ", new TokenLine("  ,  zzz  ,  aaa  ,  bbb  ,  ", model).getSortedText());
	}


	@Test
	public void t1() {
		SortTokensModel model = new SortTokensModel(" ");
		assertEquals("aaa bbb zzz", new TokenLine("zzz aaa bbb", model).getSortedText());
		assertEquals(  "aaa  bbb  zzz", new TokenLine("zzz  aaa  bbb", model).getSortedText());
		assertEquals(  "aaa  bbb  zzz  ", new TokenLine("zzz  aaa  bbb  ", model).getSortedText());
		assertEquals(  "  aaa  bbb  zzz  ", new TokenLine("  zzz  aaa  bbb  ", model).getSortedText());

//		model.getSortSettings().preserveLeadingSpaces(false);
//		assertEquals("aaa bbb zzz", new TokenLine("zzz aaa bbb", model).getSortedText());
	}
}