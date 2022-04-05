package com.pixplaze.plugin;

import com.pixplaze.PlayerListener;
import com.pixplaze.command.*;
import com.pixplaze.sync.database.sql.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PixplazeLoginSync extends JavaPlugin {

    private static PixplazeLoginSync instance;

    public static PixplazeLoginSync getInstance() {
        return instance;
    }

    public PixplazeLoginSync() {
        instance = this;
    }

    @Override
    public void onEnable() {
        ((LoggerContext) LogManager.
                getContext(false)).getConfiguration().
                getLoggerConfig(LogManager.ROOT_LOGGER_NAME).
                addFilter(new PasswordFilter());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        Objects.requireNonNull(getCommand("login")).setExecutor(new LoginCmdExecutor());
        Objects.requireNonNull(getCommand("logout")).setExecutor(new LogoutCmdExecutor());
        Objects.requireNonNull(getCommand("register")).setExecutor(new RegisterCmdExecutor());

        ConnectionManager.getInstance().connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
