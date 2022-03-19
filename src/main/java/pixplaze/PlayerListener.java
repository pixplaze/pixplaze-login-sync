package pixplaze;

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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import pixplaze.pixplazeloginsync.PixplazeLoginSync;
import pixplaze.sync.LoginSyncHandler;

public class PlayerListener implements Listener {

    private LoginSyncHandler syncer = LoginSyncHandler.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        syncer.setPlayerUnlogined(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        syncer.setPlayerUnlogined(e.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!syncer.isPlayerLogined(player)) {
             player.sendMessage("Вы не можете двигаться!");
             e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
            if (player != null && !syncer.isPlayerLogined(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!syncer.isPlayerLogined(player)) {
            player.sendMessage("Вы не можете взаимодействовать!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (!syncer.isPlayerLogined(player)) {
            player.sendMessage("Вы не можете ничего выкинуть!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
            if (player != null && !syncer.isPlayerLogined(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
            if (player != null && !syncer.isPlayerLogined(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        HumanEntity entity = e.getWhoClicked();
        Player player = PixplazeLoginSync.getInstance().getServer().getPlayer(entity.getName());
        if (player != null && !syncer.isPlayerLogined(player)) {
            entity.closeInventory();
            e.setCancelled(true);
            entity.sendMessage("Вы не можете использовать инвентарь!");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!syncer.isPlayerLogined(player)) {
            player.sendMessage("Вы не можете отправлять сообщения!");
            e.setCancelled(true);
        }
    }
}
