package es.dmoral.coloromatic;

import es.dmoral.coloromatic.colormode.ColorMode;

import java.util.HashMap;

/**
 * Created by Daniel Morales on 04/04/2016.
 */
public class ChromaUtil {

    public static String getFormattedColorString(int color, boolean showAlpha) {
        if (showAlpha)
            return String.format("#%08X", color);
        else
            return String.format("#%06X", 0xFFFFFF & color);
    }

    public static String getFormattedTextIndicator(IndicatorMode indicatorMode, ColorMode colorMode, HashMap<String, String> colorValuesMap) {
        if (indicatorMode == IndicatorMode.HEX) {
            if (colorMode == ColorMode.RGB)
                return "#" + lengthFixer(colorValuesMap.get("R")) + lengthFixer(colorValuesMap.get("G"))
                        + lengthFixer(colorValuesMap.get("B"));
            else if (colorMode == ColorMode.ARGB)
                return "#" + lengthFixer(colorValuesMap.get("A")) + lengthFixer(colorValuesMap.get("R"))
                        + lengthFixer(colorValuesMap.get("G")) + lengthFixer(colorValuesMap.get("B"));
            else // using HSV and HEX is not recommended
                return "#" + lengthFixer(colorValuesMap.get("H")) + lengthFixer(colorValuesMap.get("S"))
                        + lengthFixer(colorValuesMap.get("V"));
        } else {
            if (colorMode == ColorMode.RGB)
                return lengthFixer(colorValuesMap.get("R")) + ", " + lengthFixer(colorValuesMap.get("G")) + ", "
                        + lengthFixer(colorValuesMap.get("B"));
            else if (colorMode == ColorMode.ARGB)
                return lengthFixer(colorValuesMap.get("A")) + ", " + lengthFixer(colorValuesMap.get("R")) + ", "
                        + lengthFixer(colorValuesMap.get("G")) + ", " + lengthFixer(colorValuesMap.get("B"));
            else
                return lengthFixer(colorValuesMap.get("H")) + "º, " + lengthFixer(colorValuesMap.get("S")) + "%, "
                        + lengthFixer(colorValuesMap.get("V")) + "%";
        }
    }

    private static String lengthFixer(String stringToFix) {
        return (stringToFix != null ? stringToFix.length() : 0) < 2 ? "0" + stringToFix : stringToFix;
    }
}
