package com.pixplaze.commands;

import com.google.common.hash.Hashing;
import com.pixplaze.plugin.PixplazeLoginSync;
import com.pixplaze.sync.database.sql.LoginSyncHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LoginCmdExecutor implements CommandExecutor {

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

        if (args.length != 1) {
            player.sendMessage("Неверные аргументы!");
            return false;
        }

        if (!loginSyncHandler.getRegisteredPlayersNames().contains(player.getName())) {
            player.sendMessage("Вы ещё не зарегистрированы!");
            return true;
        }

        String password = args[0];
        String passwordHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

        if (loginSyncHandler.authPlayer(player, passwordHash)) {
            loginSyncHandler.setPlayerLogined(player);
        } else {
            player.sendMessage("Неверный пароль!");
        }
        return true;
    }
}
