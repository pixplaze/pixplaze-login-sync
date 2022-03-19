package pixplaze.sync;

import org.bukkit.entity.Player;

import java.util.Set;

public interface ISyncHandler {

    String getPlayerRole(String playerName);

    Set<String> getPlayerPermissions(String playerName);

    boolean registerPlayer(String playerName, String password);

    boolean loginPlayer(String playerName, String password);

    boolean isPlayerLogined(Player player);

    boolean setPlayerUnlogined(Player player);

    boolean setPlayerLogined(Player player);
}