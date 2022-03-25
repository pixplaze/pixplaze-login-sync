package com.pixplaze.commands;

import com.pixplaze.plugin.PixplazeLoginSync;
import com.pixplaze.sync.database.sql.LoginSyncHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LogoutCmdExecutor implements CommandExecutor {

    private static final LoginSyncHandler loginSyncHandler = LoginSyncHandler.getInstance();
    private static final PixplazeLoginSync plugin = PixplazeLoginSync.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            plugin.getLogger().info("Вы не можете это сделать в консоли!");
            return true;
        }

        Player player = (Player) sender;

        if (!loginSyncHandler.isPlayerLogined(player)) {
            player.sendMessage("Вы ещё не залогинились!");
            return true;
        }
        loginSyncHandler.setPlayerUnlogined(player);
        return true;
    }
}
