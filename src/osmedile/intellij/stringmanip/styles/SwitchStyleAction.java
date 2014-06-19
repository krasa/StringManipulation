package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import static org.apache.commons.lang.WordUtils.capitalize;

public class SwitchStyleAction extends AbstractStringManipAction {
    public static Style[][] transformation = new Style[][]{
            {Style.HYPHEN_LOWERCASE, Style.HYPHEN_UPPERCASE},
            {Style.HYPHEN_UPPERCASE, Style.UNDERSCORE_LOWERCASE},
            {Style.UNDERSCORE_LOWERCASE, Style.UNDERSCORE_UPPERCASE},
            {Style.UNDERSCORE_UPPERCASE, Style.DOT},
            {Style.DOT, Style.WORD_LOWERCASE},
            {Style.WORD_LOWERCASE, Style.WORD_CAPITALIZED},
            {Style.WORD_CAPITALIZED, Style.CAMEL_CASE},
            {Style.CAMEL_CASE, Style.HYPHEN_LOWERCASE},
    };

    public SwitchStyleAction() {
    }

    public SwitchStyleAction(boolean setupHandler) {
        super(setupHandler);
    }

    @Override
    public String transform(String s) {
        Style style = Style.from(s);
        for (Style[] styles : transformation) {
            if (styles[0] == style) {
                return styles[1].transform(styles[0], s);
            }
        }
        return s;
    }


}
