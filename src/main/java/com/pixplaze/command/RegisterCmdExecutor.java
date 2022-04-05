package com.pixplaze.command;

import com.google.common.hash.Hashing;
import com.pixplaze.plugin.PixplazeLoginSync;
import com.pixplaze.sync.database.sql.LoginSyncHandler;
import com.pixplaze.util.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class RegisterCmdExecutor implements CommandExecutor {

    private static final LoginSyncHandler loginSyncHandler = LoginSyncHandler.getInstance();
    private static final PixplazeLoginSync plugin = PixplazeLoginSync.getInstance();

    private static final int minPassLength = 4;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("You cannot use this command in the console!");
            return true;
        }

        Player player = (Player) sender;
        String locale = player.getLocale();

        if (args.length != 2) {
            player.sendMessage(Common.getMessage(locale, "wrong-args"));
            return false;
        }

        if (loginSyncHandler.getRegisteredPlayersNames().contains(player.getName())) {
            player.sendMessage(Common.getMessage(locale, "already-registered"));
            return true;
        }

        String password = args[0];
        String passwordRepeat = args[1];

        if (password.length() < minPassLength || !password.equals(passwordRepeat)) {
            player.sendMessage("Длина пароля меньше " + minPassLength + " или пароли не совпадают!");
            return true;
        }

        String passwordHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        boolean result = loginSyncHandler.registerPlayer(player, passwordHash);

        if (result) {
            player.sendMessage(Common.getMessage(locale, "success-registration"));
            loginSyncHandler.setPlayerLogined(player);
        } else {
            player.sendMessage(Common.getMessage(locale, "unknown-registration-error"));
        }
        return true;
    }
}
