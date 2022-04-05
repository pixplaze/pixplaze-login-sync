package com.pixplaze.command;

import com.google.common.hash.Hashing;
import com.pixplaze.plugin.PixplazeLoginSync;
import com.pixplaze.sync.database.sql.LoginSyncHandler;
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
            plugin.getLogger().info("Вы не можете это сделать в консоли!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage("Неверные аргументы!");
            return false;
        }

        if (loginSyncHandler.getRegisteredPlayersNames().contains(player.getName())) {
            player.sendMessage("Вы уже зарегистрированы!");
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
            player.sendMessage("Вы успешно зарегистрировались!");
            loginSyncHandler.setPlayerLogined(player);
        } else {
            player.sendMessage("Ошибка при регистрации!");
        }
        return true;
    }
}
