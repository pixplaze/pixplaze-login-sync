package com.pixplaze.command;

import com.pixplaze.plugin.PixplazeLoginSync;
import com.pixplaze.sync.database.sql.SqlSyncHandler;
import com.pixplaze.util.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LogoutCmdExecutor implements CommandExecutor {

    private static final SqlSyncHandler syncHandler = SqlSyncHandler.getInstance();
    private static final PixplazeLoginSync plugin = PixplazeLoginSync.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("You cannot use this command in the console!");
            return true;
        }

        Player player = (Player) sender;
        String locale = player.getLocale();

        if (!syncHandler.isPlayerLogined(player)) {
            player.sendMessage(Common.getMessage(locale, "not-logged-yet"));
            return true;
        }
        syncHandler.setPlayerUnlogined(player);
        player.sendMessage(Common.getMessage(locale, "player-logout"));
        return true;
    }
}
