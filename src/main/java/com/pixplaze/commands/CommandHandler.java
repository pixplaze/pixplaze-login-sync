package com.pixplaze.commands;

import com.google.common.hash.Hashing;
import com.pixplaze.sync.database.sql.LoginSyncHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.pixplaze.plugin.PixplazeLoginSync;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class CommandHandler implements CommandExecutor {

    private static final int minPassLength = 4;
    private static final LoginSyncHandler loginSyncHandler = LoginSyncHandler.getInstance();
    private static final PixplazeLoginSync plugin = PixplazeLoginSync.getInstance();

    private static CommandHandler instance;

    public static CommandHandler getInstance() {
        if (instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            plugin.getLogger().info("Вы не можете это сделать в консоли!");
            return true;
        }

        Player player = (Player) sender;
        String commandName = command.getName().toLowerCase(Locale.ROOT);

        switch (commandName) {
            case ("register"):
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

            case ("login"):
                if (args.length != 1) {
                    player.sendMessage("Неверные аргументы!");
                    return false;
                }

                if (!loginSyncHandler.getRegisteredPlayersNames().contains(player.getName())) {
                    player.sendMessage("Вы ещё не зарегистрированы!");
                    return true;
                }

                password = args[0];
                passwordHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

                if (loginSyncHandler.authPlayer(player, passwordHash)) {
                    loginSyncHandler.setPlayerLogined(player);
                    return true;
                }

            case ("logout"):
                if (!loginSyncHandler.isPlayerLogined(player)) {
                    player.sendMessage("Вы ещё не залогинились!");
                    return true;
                }
                loginSyncHandler.setPlayerUnlogined(player);

            default:
                return false;
        }
    }
}
