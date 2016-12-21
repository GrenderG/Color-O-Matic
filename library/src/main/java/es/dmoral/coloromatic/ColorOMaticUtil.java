package es.dmoral.coloromatic;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * Color-O-Matic
 * Copyright (C) 2016 - GrenderG
 *
 * This file is part of Color-O-Matic.
 *
 * Color-O-Matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Color-O-Matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Color-O-Matic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ColorOMaticUtil {

    public static String getFormattedColorString(int color, boolean showAlpha) {
        if (showAlpha)
            return String.format("#%08X", color);
        else
            return String.format("#%06X", 0xFFFFFF & color);
    }

    @ColorInt
    public static int getThemeAccentColor(@NonNull Context context) {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

}
