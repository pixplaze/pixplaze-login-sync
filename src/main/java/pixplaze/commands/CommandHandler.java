package pixplaze.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pixplaze.pixplazeloginsync.PixplazeLoginSync;
import pixplaze.sync.LoginSyncHandler;

public class CommandHandler implements CommandExecutor {

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
            PixplazeLoginSync.getInstance().getLogger().info("Вы не можете это сделать в консоли!");
            return true;
        }
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("login")) {
            LoginSyncHandler.getInstance().setPlayerLogined(player);
        }

        if (command.getName().equalsIgnoreCase("logout")) {
            LoginSyncHandler.getInstance().setPlayerUnlogined(player);
        }

        return false;
    }
}
