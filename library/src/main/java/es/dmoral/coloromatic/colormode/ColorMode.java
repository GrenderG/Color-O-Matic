package es.dmoral.coloromatic.colormode;

import es.dmoral.coloromatic.colormode.mode.ARGB;
import es.dmoral.coloromatic.colormode.mode.AbstractColorMode;
import es.dmoral.coloromatic.colormode.mode.HSV;
import es.dmoral.coloromatic.colormode.mode.RGB;

/**
 * Created by Daniel Morales on 04/04/2016.
 */
public enum ColorMode {
    RGB, HSV, ARGB;

    public AbstractColorMode getColorMode() {
        switch (this) {
            case RGB:
                return new RGB();
            case HSV:
                return new HSV();
            case ARGB:
                return new ARGB();
            default:
                return new RGB();
        }
    }
}
