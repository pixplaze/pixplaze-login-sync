package com.pixplaze.sync;

import org.bukkit.entity.Player;

import java.util.Set;

public interface ISyncHandler {

    String getPlayerRole(Player player);

    Set<String> getPlayerPermissions(Player player);

    boolean registerPlayer(Player player, String passwordHash);

    boolean authPlayer(Player player, String passwordHash);

    boolean isPlayerLogined(Player player);

    boolean setPlayerUnlogined(Player player);

    boolean setPlayerLogined(Player player);
}