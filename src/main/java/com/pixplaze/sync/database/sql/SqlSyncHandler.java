package com.pixplaze.sync.database.sql;

import com.pixplaze.sync.ISyncHandler;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SqlSyncHandler implements ISyncHandler {
    private static final ConnectionManager connectionManager = ConnectionManager.getInstance();

    private static SqlSyncHandler instance;

    private final ArrayList<Player> unloginedPlayers = new ArrayList<>();
    private final ArrayList<Player> loginedPlayers = new ArrayList<>();

    public static SqlSyncHandler getInstance() {
        if (instance == null) {
            instance = new SqlSyncHandler();
        }
        return instance;
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
            String query = String.format("INSERT INTO player (name, pass_hash, reg_date) VALUES ('%s','%s', now())",
                    player.getName(), passwordHash);
            connectionManager.executeUpdate(query);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean authPlayer(Player player, String passwordHash) {
        return getPlayerPassHash(player).equals(passwordHash);
    }

    @Override
    public boolean isPlayerLogined(Player player) {
        return loginedPlayers.contains(player);
    }

    @Override
    public List<Player> getLoginedPlayers() {
        return new ArrayList<>(loginedPlayers);
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
        unloginedPlayers.remove(player);
        loginedPlayers.add(player);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        return true;
    }

    @Override
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

        String query = String.format("SELECT * FROM player WHERE name ='%s'", player.getName());
        ResultSet rs = connectionManager.executeSelect(query);

        try {
            rs.next();
            passHash = rs.getString("pass_hash");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return passHash;
    }
}
