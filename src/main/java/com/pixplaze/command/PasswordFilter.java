package com.pixplaze.command;

import com.pixplaze.plugin.PixplazeLoginSync;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class PasswordFilter extends AbstractFilter {

    private static final PixplazeLoginSync plugin = PixplazeLoginSync.getInstance();

    @Override
    public Result filter(LogEvent event) {
        return event == null ? Result.NEUTRAL : isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return isLoggable(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return isLoggable(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : isLoggable(msg.toString());
    }

    private Result isLoggable(String msg) {
        if (msg != null) {
            if (msg.contains("issued server command:")) {
                if (!checkForUnsafetyCommand(msg)) {
                    return Result.DENY;
                }
            }
        }
        return Result.NEUTRAL;
    }

    private boolean checkForUnsafetyCommand(String msg) {
        ArrayList<String> stopList = new ArrayList<>();
        stopList.add("login");
        stopList.add("register");
        stopList.addAll(Objects.requireNonNull(plugin.getCommand("login")).getAliases());
        stopList.addAll(Objects.requireNonNull(plugin.getCommand("register")).getAliases());

        for (String word : stopList) {
            if (msg.toLowerCase(Locale.ROOT).contains(word)) {
                return false;
            }
        }
        return true;
    }

}