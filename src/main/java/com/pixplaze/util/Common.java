package com.pixplaze.util;

import com.pixplaze.keyword.Dictionary;
import com.pixplaze.plugin.PixplazeLoginSync;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Common {

    private static final Dictionary dictionary = PixplazeLoginSync.getInstance().getDictionary();
    private static final PixplazeLoginSync plugin = PixplazeLoginSync.getInstance();

    /**
     * Данный метод представляет из себя оболочку для метода из библиотеки PixplazeKeywordLib.
     * Единственная причина, по которой он существует - он выглядит в коде более читаемо, чем оригинальный метод.
     */
    public static String getMessage(String locale, String key) {
        return dictionary.get(com.pixplaze.keyword.util.Common.escapeLocale(locale), key);
    }

    /**
    * Метод возвращает "внешние" команды, используемые для входа на сервер.
    * Если конкретнее, в их списке состоят команды /register, /login и их алиасы.
    */
    public static List<String> getExternalCommands() {
        List<String> externalCommands = new ArrayList<>();
        externalCommands.add("login");
        externalCommands.add("register");
        externalCommands.addAll(Objects.requireNonNull(plugin.getCommand("login")).getAliases());
        externalCommands.addAll(Objects.requireNonNull(plugin.getCommand("register")).getAliases());
        return new ArrayList<>(externalCommands);
    }

    /**
    * Метод возвращает true, если в сообщении msg есть команда из списка внешних команд.
    */
    public static boolean isExternalCommand(String msg) {
        for (String word : getExternalCommands()) {
            if (msg.toLowerCase(Locale.ROOT).contains("/" + word)) {
                return true;
            }
        }
        return false;
    }
}
