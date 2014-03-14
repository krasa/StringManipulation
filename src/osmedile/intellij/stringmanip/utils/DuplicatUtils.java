package osmedile.intellij.stringmanip.utils;


/**
 * @author Olivier Smedile
 * @version $Id: DuplicatUtils.java 62 2008-04-20 11:11:54Z osmedile $
 */
public class DuplicatUtils {


    public static final String INT_ONLY_REGEX = "[-+]?[0-9]+[lLdDfF]?";

    public static final String DECIMAL_ONLY_REGEX =
            "([-+]?[0-9]+\\.[0-9]+[dDfF]?)?([-+]?[0-9]+\\.[dDfF]?)?([-+]?\\.[0-9]+[dDfF]?)?";
    public static final String DECIMAL_ONLY_REGEX2 = "[-+]?(\\d+\\.?\\d*||\\d*\\.?\\d+)[dDfF]?";
    public static final String DECIMAL_ONLY_REGEX3 = "([-+]?[0-9]+\\.[0-9]+[dDfF]?)?([-+]?\\.[0-9]+[dDfF]?)?";

    //public static final String DECIMAL2_ONLY_REGEX = "[-+]?[0-9]*\\.?[0-9]+[dDfF]?";

    /**
     * Regex that only match hexadecimal.
     */
    public static final String HEXA_ONLY_REGEX = "[-+]?0[xX][0-9a-fA-F]+";


    /**
     * This regex doesn't match all decimal regex that don't use a scientific notation.
     */
    public static final String SCI_ONLY_REGEX = "[-+]?[0-9]*\\.?[0-9]+[eE][-+]?[0-9]+";

    public static final String NUMBER_REGEX = "(" + SCI_ONLY_REGEX + ")|(" + HEXA_ONLY_REGEX + ")|" +
            DECIMAL_ONLY_REGEX + "(" + INT_ONLY_REGEX + ")?";

    public static final String SIMPLE_NUMBER_REGEX = DECIMAL_ONLY_REGEX3 + "(" + INT_ONLY_REGEX + ")?";

    //"([-+]?(0[xX][0-9a-fA-F]))?([-+]?((\\b[0-9]+)?\\.)?\\b[0-9]+([eE][-+]?[0-9]+)?\\b)?((([0-9]+)?\\.)?[0-9]+)?"


    /**
     * Increment the number found in the specified string.
     * The string is parsed using NumberUtils.createNumber() and NumberUtils.createDouble()
     *
     * @param str
     *
     * @return
     */
    public static String simpleInc(String str) {
        return simpleAddition(str, 1);
    }

    /**
     * Add the specified number to the the number found in the specified string.
     * The string is parsed using NumberUtils.createNumber() and NumberUtils.createDouble()
     *
     * @param str
     * @param add
     *
     * @return
     */
    public static String simpleAddition(final String str, final int add) {
        Number number = null;
        try {
            number = NumberUtils.createNumber(str);
        } catch (NumberFormatException e) {
            //Not a number, ignore it, will try to create a double

        } catch (IndexOutOfBoundsException e) {
            //Bug in createNumber, ignore it, will try to create a double
        }


        if (number == null) {
            try {
                number = NumberUtils.createDouble(str);
            } catch (Exception e1) {
                //nothing to do, really not a number or too complex for NumberUtils
            }
        }

        Number n;
        if (number != null) {
            if (number instanceof Float) {
                n = number.floatValue() + add;
            } else if (number instanceof Double) {
                n = number.doubleValue() + add;
            } else {
                n = number.intValue() + add;
            }
            return n.toString();
        } else {
            return str;

        }

    }

    public static String simpleDec(String str) {
        return simpleAddition(str, -1);
    }
}
