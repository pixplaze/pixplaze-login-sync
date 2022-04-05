package com.pixplaze.sync.database.sql;

import com.pixplaze.sync.ISyncHandler;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LoginSyncHandler implements ISyncHandler {
    private static final ConnectionManager connectionManager = ConnectionManager.getInstance();

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
    public String getPlayerRole(Player player) {
        return null;
    }

    @Override
    public Set<String> getPlayerPermissions(Player player) {
        return null;
    }

    @Override
    public boolean registerPlayer(Player player, String passwordHash) {
        try {
            String query = "INSERT INTO player (name, pass_hash, reg_date) VALUES ('" +
                    player.getName().toLowerCase(Locale.ROOT) + "', '" + passwordHash + "', now());";
            connectionManager.executeUpdate(query);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean authPlayer(Player player, String passwordHash) {
        if (getPlayerPassHash(player).equals(passwordHash)) {
            return true;
        } else {
            return false;
        }
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
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE));
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
        return true;
    }

    public List<String> getRegisteredPlayersNames() {
        List<String> names = new ArrayList<>();

        try {
            ResultSet rs = connectionManager.executeSelect("SELECT * FROM player");
            while (rs.next()) {
                String name = rs.getString("name");
                names.add(name);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return names;
    }

    private String getPlayerPassHash(Player player) {
        String passHash = "";

        ResultSet rs = connectionManager.executeSelect("SELECT * FROM player WHERE name ='" +
                player.getName() + "';");

        try {
            rs.next();
            passHash = rs.getString("pass_hash");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return passHash;
    }
}
