package com.pixplaze.plugin;

import com.pixplaze.PlayerListener;
import com.pixplaze.command.*;
import com.pixplaze.sync.database.sql.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.bukkit.plugin.java.JavaPlugin;

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

        getCommand("login").setExecutor(new LoginCmdExecutor());
        getCommand("logout").setExecutor(new LogoutCmdExecutor());
        getCommand("register").setExecutor(new RegisterCmdExecutor());

        ConnectionManager.getInstance().connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
