package com.pixplaze.plugin;

import com.pixplaze.PlayerListener;
import com.pixplaze.command.*;
import com.pixplaze.keyword.Dictionary;
import com.pixplaze.keyword.plugin.PixplazeKeywordLib;
import com.pixplaze.sync.database.sql.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.beans.ExceptionListener;
import java.io.File;
import java.util.Locale;
import java.util.Objects;

public final class PixplazeLoginSync extends JavaPlugin {

    private Dictionary dictionary;
    private final FileConfiguration config = getConfig();

    private static PixplazeLoginSync instance;

    public static PixplazeLoginSync getInstance() {
        return instance;
    }

    public PixplazeLoginSync() {
        instance = this;
    }

    @Override
    public void onEnable() {
        copyDefaultFiles();
        initDictionary();
        setLoggerFilter();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        registerCommands();
        ConnectionManager.getInstance().connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void copyDefaultFiles() {
        this.saveDefaultConfig();
        saveResource("lang/ru_RU.yml", false);
    }

    private void initDictionary() {
        String defaultLocale = config.getString("default-locale");
        dictionary = PixplazeKeywordLib.fromLanguageDirectory(Locale.forLanguageTag(defaultLocale),
                this.getDataFolder() + "/lang");
    }

    private void setLoggerFilter() {
        ((LoggerContext) LogManager.
                getContext(false)).getConfiguration().
                getLoggerConfig(LogManager.ROOT_LOGGER_NAME).
                addFilter(new PasswordFilter());
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("login")).setExecutor(new LoginCmdExecutor());
        Objects.requireNonNull(getCommand("logout")).setExecutor(new LogoutCmdExecutor());
        Objects.requireNonNull(getCommand("register")).setExecutor(new RegisterCmdExecutor());
    }

    public Dictionary getDictionary() {
        return dictionary;
    }
}
