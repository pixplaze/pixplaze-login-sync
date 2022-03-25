package com.pixplaze.plugin;

import com.pixplaze.PlayerListener;
import com.pixplaze.commands.CommandHandler;
import com.pixplaze.sync.database.sql.ConnectionManager;
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
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("login").setExecutor(CommandHandler.getInstance());
        getCommand("logout").setExecutor(CommandHandler.getInstance());
        getCommand("register").setExecutor(CommandHandler.getInstance());
        ConnectionManager.getInstance().connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
