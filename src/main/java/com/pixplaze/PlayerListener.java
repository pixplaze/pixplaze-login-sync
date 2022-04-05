package com.pixplaze;

import com.pixplaze.util.Common;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import com.pixplaze.plugin.PixplazeLoginSync;
import com.pixplaze.sync.database.sql.LoginSyncHandler;

import java.util.List;

public class PlayerListener implements Listener {

    private final static LoginSyncHandler loginSyncHandler = LoginSyncHandler.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String locale = player.getLocale();

        loginSyncHandler.setPlayerUnlogined(player);

        List<String> registeredPlayersNames = loginSyncHandler.getRegisteredPlayersNames();
        if (!registeredPlayersNames.contains(player.getName())) {
            player.sendMessage(Common.getMessage(locale, "new-player-join-text"));
        } else {
            player.sendMessage(Common.getMessage(locale, "old-player-join-text"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        loginSyncHandler.setPlayerUnlogined(e.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!loginSyncHandler.isPlayerLogined(player)) {
             e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
            if (player != null && !loginSyncHandler.isPlayerLogined(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!loginSyncHandler.isPlayerLogined(player)) {
            player.sendMessage(Common.getMessage(player.getLocale(), "not-permitted-action"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (!loginSyncHandler.isPlayerLogined(player)) {
            player.sendMessage(Common.getMessage(player.getLocale(), "not-permitted-action"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
            if (player != null && !loginSyncHandler.isPlayerLogined(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
            if (player != null && !loginSyncHandler.isPlayerLogined(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        HumanEntity entity = e.getWhoClicked();
        Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
        if (player != null && !loginSyncHandler.isPlayerLogined(player)) {
            entity.closeInventory();
            player.sendMessage(Common.getMessage(player.getLocale(), "not-permitted-action"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!loginSyncHandler.isPlayerLogined(player)) {
            player.sendMessage(Common.getMessage(player.getLocale(), "not-permitted-action"));
            e.setCancelled(true);
        }
    }
}
