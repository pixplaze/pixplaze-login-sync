package com.pixplaze.command;

import com.google.common.hash.Hashing;
import com.pixplaze.plugin.PixplazeLoginSync;
import com.pixplaze.sync.database.sql.SqlSyncHandler;
import com.pixplaze.util.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class LoginCmdExecutor implements CommandExecutor {

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

        if (args.length != 1) {
            player.sendMessage(Common.getMessage(locale, "wrong-args"));
            return false;
        }

        if (!syncHandler.getRegisteredPlayersNames().contains(player.getName())) {
            player.sendMessage(Common.getMessage(locale, "not-registered-yet"));
            return true;
        }

        String password = args[0];
        String passwordHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

        if (syncHandler.authPlayer(player, passwordHash)) {
            syncHandler.setPlayerLogined(player);
            player.sendMessage(Common.getMessage(locale, "success-authorization"));
        } else {
            player.sendMessage(Common.getMessage(locale, "wrong-password"));
        }
        return true;
    }
}
