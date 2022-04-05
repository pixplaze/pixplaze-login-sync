package com.pixplaze.util;

import com.pixplaze.keyword.Dictionary;
import com.pixplaze.plugin.PixplazeLoginSync;

public class Common {

    private static final Dictionary dictionary = PixplazeLoginSync.getInstance().getDictionary();

    public static String getMessage(String locale, String key) {
        return dictionary.get(com.pixplaze.keyword.util.Common.escapeLocale(locale), key);
    }
}
