package pixplaze.pixplazeloginsync;

import org.bukkit.plugin.java.JavaPlugin;
import pixplaze.PlayerListener;
import pixplaze.commands.CommandHandler;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
