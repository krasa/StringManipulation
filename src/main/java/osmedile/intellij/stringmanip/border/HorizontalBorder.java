/*
 * iNamik Text Tables for Java
 *
 * Copyright (C) 2016 David Farrell (DavidPFarrell@yahoo.com)
 *
 * Licensed under The MIT License (MIT), see LICENSE.txt
 */
package osmedile.intellij.stringmanip.border;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;

import java.util.Collection;

public final class HorizontalBorder {

    public static final HorizontalBorder DOUBLE_LINE = of(Chars.DOUBLE_LINE);
    public static final HorizontalBorder SINGLE_LINE = of(Chars.SINGLE_LINE);

    private final Chars chars;

    public static HorizontalBorder of(Chars chars) {
        return new HorizontalBorder(chars);
    }

    public HorizontalBorder(Chars chars) {
        this.chars = chars;
    }

    public GridTable apply(GridTable grid) {
        final int gr = grid.numRows();
        final int gc = grid.numCols();

        GridTable b = GridTable.of((gr * 2) + 1, (gc * 2) + 1);

        // Copy source grid first to compute cell maximums
        //
        for (int r = 0; r < gr; r++) {
            for (int c = 0; c < gc; c++) {
                b.put((r * 2) + 1, (c * 2) + 1, grid.cell(r, c));
            }
        }

        //
        // Top Row
        //
        int r = 0;
        int c = 0;

        // Top-Left Corner
        //
        b.put(r, c, Cell.of(String.valueOf(chars.topLeft)));
        c++;
        // Top-Horizontals and Intersects
        //
        for (int h = 0; h < gc; h++) {
            // Horizontal-Fill
            //
            b.put(r, c, hline(grid.colWidth(h)));
            c++;
            // Top-Intersect
            //
            if (h < gc - 1) {
                b.put(r, c, Cell.of(String.valueOf(chars.topIntersect)));
                c++;
            }
        }
        // Top-Right Corner
        //
        b.put(r, c, Cell.of(String.valueOf(chars.topRight)));
        c++;

        //
        // Middle Rows
        //

        for (int v = 0; v < gr; v++) {
            //
            // Cell-Line
            //

            r++;
            c = 0;
            // Vertical-Fill
            //
//            b.put(r, c, vline(grid.rowHeight(v)));
            c++;
            for (int h = 0; h < gc; h++) {
                // Skip previously-populated data cell
                //
                c++;
                // Vertical-Fill
                //
                if (h < gc - 1) {
//                    b.put(r, c, vline(grid.rowHeight(v)));
                    c++;
                }
            }
            // Vertical-Fill
            //
//            b.put(r, c, vline(grid.rowHeight(v)));
            c++;

            if (v < gr - 1) {
                //
                // Border-Line
                //

                r++;
                c = 0;
                // Left Intersect
                //
                b.put(r, c, Cell.of(String.valueOf(chars.LeftIntersect)));
                c++;
                for (int h = 0; h < gc; h++) {
                    // Horizontal-Fill
                    //
                    b.put(r, c, hline(grid.colWidth(h)));
                    c++;
                    // Intersect
                    //
                    if (h < gc - 1) {
                        b.put(r, c, Cell.of(String.valueOf(chars.intersect)));
                        c++;
                    }
                }
                // Right-Intersect
                //
                b.put(r, c, Cell.of(String.valueOf(chars.RightIntersect)));
                c++;
            }
        }

        //
        // Bottom row
        //

        // Bottom-Left Corner
        //
        r++;
        c = 0;
        b.put(r, c, Cell.of(String.valueOf(chars.bottomLeft)));
        c++;
        // Bottom-Horizontals and Intersects
        //
        for (int h = 0; h < gc; h++) {
            // Horizontal-Fill
            //
            b.put(r, c, hline(grid.colWidth(h)));
            c++;
            // Bottom-Intersect
            //
            if (h < gc - 1) {
                b.put(r, c, Cell.of(String.valueOf(chars.bottomIntersect)));
                c++;
            }
        }
        // Bottom-Right Corner
        //
        b.put(r, c, Cell.of(String.valueOf(chars.bottomRight)));
        c++;

        return b;
    }

    private Collection<String> hline(int width) {
        StringBuilder sb = new StringBuilder(width);
        while (sb.length() < width) {
            sb.append(chars.horizontal);
        }
        return Cell.of(sb.toString());
    }

//    private Collection<String> vline(int height) {
//        List<String> newLines = new ArrayList<String>(height);
//        final String line = Character.toString(chars.vertical);
//        while (newLines.size() < height) {
//            newLines.add(line);
//        }
//        return newLines;
//    }

    /*
     * Chars
     */
    public static class Chars {
        public static final Chars DOUBLE_LINE = new Chars('╬', '═', '║', '╔', '╦', '╗', '╠', '╣', '╚', '╩', '╝');
        public static final Chars SINGLE_LINE = new Chars('┼', '─', '│', '┌', '┬', '┐', '├', '┤', '└', '┴', '┘');

        public final char intersect;
        public final char horizontal;
        public final char vertical;
        public final char topLeft;
        public final char topIntersect;
        public final char topRight;
        public final char LeftIntersect;
        public final char RightIntersect;
        public final char bottomLeft;
        public final char bottomIntersect;
        public final char bottomRight;

        // *
        public static Chars of(char intersect) {
            return new Chars(intersect, intersect, intersect, intersect, intersect, intersect, intersect, intersect, intersect, intersect, intersect);
        }

        // +-|
        public static Chars of(char intersect, char horizontal, char vertical) {
            return new Chars(intersect, horizontal, vertical, intersect, intersect, intersect, intersect, intersect, intersect, intersect, intersect);
        }

        public Chars(char i, char h, char v, char tl, char ti, char tr, char li, char ri, char bl, char bi, char br) {
            this.intersect = i;
            this.horizontal = h;
            this.vertical = v;
            this.topLeft = tl;
            this.topIntersect = ti;
            this.topRight = tr;
            this.LeftIntersect = li;
            this.RightIntersect = ri;
            this.bottomLeft = bl;
            this.bottomIntersect = bi;
            this.bottomRight = br;
        }
    }

}
