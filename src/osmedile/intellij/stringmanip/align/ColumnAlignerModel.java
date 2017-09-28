package osmedile.intellij.stringmanip.align;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnAlignerModel {
    private List<String> separators = new ArrayList<String>();
    private boolean spaceBefore = true;
    private boolean spaceAfter = true;
    private boolean trimValues = true;
    private boolean trimLines = false;

    public ColumnAlignerModel() {
    }

    public ColumnAlignerModel(String separator) {
        this.separators = Arrays.asList(separator);
    }

    public List<String> getSeparators() {
        return separators;
    }

    public void setSeparators(List<String> separators) {
        this.separators = separators;
    }

    public boolean isSpaceBefore() {
        return spaceBefore;
    }

    public void setSpaceBefore(final boolean spaceBefore) {
        this.spaceBefore = spaceBefore;
    }

    public boolean isSpaceAfter() {
        return spaceAfter;
    }

    public void setSpaceAfter(final boolean spaceAfter) {
        this.spaceAfter = spaceAfter;
    }

    public boolean isTrimValues() {
        return trimValues;
    }

    public void setTrimValues(final boolean trimValues) {
        this.trimValues = trimValues;
    }

    public boolean isTrimLines() {
        return trimLines;
    }

    public void setTrimLines(final boolean trimLines) {
        this.trimLines = trimLines;
    }
}