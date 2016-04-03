package es.dmoral.coloromatic.colormode.mode;

import java.util.List;

import es.dmoral.coloromatic.colormode.Channel;


/**
 * Created by Pavel Sikun on 28.03.16.
 */
public interface AbstractColorMode {
    int evaluateColor(List<Channel> channels);
    List<Channel> getChannels();
}
