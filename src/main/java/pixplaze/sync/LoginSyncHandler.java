package pixplaze.sync;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pixplaze.pixplazeloginsync.PixplazeLoginSync;

import java.util.ArrayList;
import java.util.Set;

public class LoginSyncHandler implements ISyncHandler {
    private static LoginSyncHandler instance;

    private final ArrayList<Player> unloginedPlayers = new ArrayList<>();
    private final ArrayList<Player> loginedPlayers = new ArrayList<>();

    public static LoginSyncHandler getInstance() {
        if (instance == null) {
            instance = new LoginSyncHandler();
        }
        return instance;
    }

    public boolean hasPermission(Player player, String permissionName) {
        return false;
    }

    @Override
    public String getPlayerRole(String playerName) {
        return null;
    }

    @Override
    public Set<String> getPlayerPermissions(String playerName) {
        return null;
    }

    @Override
    public boolean registerPlayer(String playerName, String password) {
        return false;
    }

    @Override
    public boolean loginPlayer(String playerName, String password) {
        return false;
    }

    @Override
    public boolean isPlayerLogined(Player player) {
        if (loginedPlayers.contains(player)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setPlayerUnlogined(Player player) {
        if (isPlayerLogined(player)) {
            loginedPlayers.remove(player);
        }
        unloginedPlayers.add(player);
        player.sendMessage("Авторизируйтесь с помощью /login или зарегистрируйтесь с помощью /register");
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE));
        player.sendMessage("Вы разлогинились!");
        return true;
    }

    @Override
    public boolean setPlayerLogined(Player player) {
        if (unloginedPlayers.contains(player)) {
            unloginedPlayers.remove(player);
        }
        loginedPlayers.add(player);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.sendMessage("Вы залогинились!");
        return true;
    }
}
