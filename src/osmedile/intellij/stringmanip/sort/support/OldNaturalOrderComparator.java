/**
 * NaturalOrderComparator.java -- Perform improved 'natural order' comparisons of strings in Java.
 * Copyright (C) 2016 by Vojtech Krasa <vojta.krasa@gmail.com>
 * NaturalOrderComparator.java -- Perform 'natural order' comparisons of strings in Java.
 * Copyright (C) 2003 by Pierre-Luc Paour <natorder@paour.com>
 * Based on the C version by Martin Pool, of which this is more or less a straight conversion.
 * Copyright (C) 2000 by Martin Pool <mbp@humbug.org.au>
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */
package osmedile.intellij.stringmanip.sort.support;

import java.util.*;

public class OldNaturalOrderComparator implements Comparator {

	int compareRight(String a, String b) {
		int bias = 0;
		int ia = 0;
		int ib = 0;

		// The longest run of digits wins. That aside, the greatest
		// value wins, but we can't know that it will until we've scanned
		// both numbers to know that they have the same magnitude, so we
		// remember it in BIAS.
		for (; ; ia++, ib++) {
			char ca = charAt(a, ia);
			char cb = charAt(b, ib);

			if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
				return bias;
			} else if (!Character.isDigit(ca)) {
				return -1;
			} else if (!Character.isDigit(cb)) {
				return +1;
			} else if (ca < cb) {
				if (bias == 0) {
					bias = -1;
				}
			} else if (ca > cb) {
				if (bias == 0)
					bias = +1;
			} else if (ca == 0 && cb == 0) {
				return bias;
			}
		}
	}

	@Override
	public int compare(Object o1, Object o2) {
		String a = o1.toString();
		String b = o2.toString();

		int ia = 0, ib = 0;
		int nza = 0, nzb = 0;
		char pca, pcb;
		char ca, cb;
		int result;

		while (true) {
			// only count the number of zeroes leading the last number compared
			nza = nzb = 0;
			pca = pcb = '-';

			ca = charAt(a, ia);
			cb = charAt(b, ib);

			// skip over leading zeros
			while (ca == '0' && nextIsDigit(a, ia)) {
				if (ca == '0') {
					nza++;
				} else {
					// only count consecutive zeroes
					nza = 0;
				}

				pca = ca;
				ca = charAt(a, ++ia);
			}

			while (cb == '0' && nextIsDigit(b, ib)) {
				if (cb == '0') {
					nzb++;
				} else {
					// only count consecutive zeroes
					nzb = 0;
				}

				pcb = cb;
				cb = charAt(b, ++ib);
			}

			// process run of digits
			if (Character.isDigit(ca) && Character.isDigit(cb)) {
				result = compareRight(a.substring(ia), b.substring(ib));
				if (result != 0) {
					return result;
				}
//				//'2' < '02'
//				if (result == 0 && nza - nzb != 0) {
//					return nza - nzb;
//				}
			}

			if (ca == 0 && cb == 0) {
				// The strings compare the same. Perhaps the caller
				// will want to call strcmp to break the tie.
				return nza - nzb;
			}

			if (ca < cb) {
				return -1;
			} else if (ca > cb) {
				return +1;
			}

			++ia;
			++ib;
		}
	}

	public boolean disabled_isWhitespace(char ca) {
		//do not ignore leading whitespaces 
		return false;
//		return Character.isSpaceChar(ca);     - originally
	}

	static char charAt(String s, int i) {
		if (i >= s.length()) {
			return 0;
		} else {
			return s.charAt(i);
		}
	}

	private boolean nextIsDigit(String s, int i) {
		if (i + 1 >= s.length()) {
			return false;
		} else {
			return Character.isDigit(s.charAt(i + 1));
		}
	}


	public static void main(String[] args) {
		String[] strings = new String[]{"1-2", "1-02", "1-20", "10-20", "fred", "jane", "pic01",
			"pic2", "pic02", "pic02a", "pic3", "pic4", "pic 4 else", "pic 5", "pic05", "pic 5",
			"pic 5 something", "pic 6", "pic   7", "pic100", "pic100a", "pic120", "pic121",
			"pic02000", "tom", "x2-g8", "x2-y7", "x2-y08", "x8-y8"};

		List orig = Arrays.asList(strings);

		System.out.println("Original: " + orig);

		List scrambled = Arrays.asList(strings);
		Collections.shuffle(scrambled);

//		System.out.println("Scrambled: " + scrambled);

		Collections.sort(scrambled, new OldNaturalOrderComparator());

		System.out.println("Sorted: " + scrambled);

		shuffle3000(scrambled);

		compareSymmetric();

		floatsWithCommas();
		numbersWithSigns();

	}

	private static void numbersWithSigns() {
		List<String> unSorted = Arrays.asList("+1", "+2", "+3", "+01", "+02", "+03", "0", "1", "2", "-0", "-1", "-2", "-3", "-01", "-02", "-03");

		System.out.println("Unsorted: " + unSorted);

		unSorted.sort(new osmedile.intellij.stringmanip.sort.support.Paour.NaturalOrderComparator());

		System.out.println("Sorted: " + unSorted);

	}

	static void shuffle3000(List<? extends Object> scrambled) {
		Collections.shuffle(scrambled, new Random(3000));
		Collections.sort(scrambled, new osmedile.intellij.stringmanip.sort.support.Paour.NaturalOrderComparator());

		System.out.println("Sorted: " + scrambled);
	}

	static void compareSymmetric() {
		osmedile.intellij.stringmanip.sort.support.Paour.NaturalOrderComparator naturalOrderComparator = new osmedile.intellij.stringmanip.sort.support.Paour.NaturalOrderComparator();

		int compare1 = naturalOrderComparator.compare("1-2", "1-02");
		int compare2 = naturalOrderComparator.compare("1-02", "1-2");

		System.out.println(compare1 + " == " + compare2);

		compare1 = naturalOrderComparator.compare("pic 5", "pic05");
		compare2 = naturalOrderComparator.compare("pic05", "pic 5");

		System.out.println(compare1 + " == " + compare2);
	}

	static void floatsWithCommas() {
		List<String> unSorted = Arrays.asList("0.9", "1.0c", "1.2", "1.3", "0.6", "1.1", "0.7", "0.3", "1.0b", "1.0", "0.8");

		System.out.println("Unsorted: " + unSorted);

		unSorted.sort(new osmedile.intellij.stringmanip.sort.support.Paour.NaturalOrderComparator());

		System.out.println("Sorted: " + unSorted);
	}
}