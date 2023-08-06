package cx.rain.mc.pastrymaster.listener;

import cx.rain.mc.pastrymaster.Constants;
import cx.rain.mc.pastrymaster.PastryMaster;
import cx.rain.mc.pastrymaster.managers.ConfigManager;
import cx.rain.mc.pastrymaster.data.persistence.PastryContainerType;
import cx.rain.mc.pastrymaster.data.persistence.PastryData;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.logging.Logger;

public class ListenerPlayerKnead implements Listener {
    private final PastryMaster plugin;
    private final ConfigManager configManager;
    private final Logger logger;

    public ListenerPlayerKnead(PastryMaster plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.logger = plugin.getLogger();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (entity instanceof Player targetPlayer && event.getHand() == EquipmentSlot.HAND) {
            Player player = event.getPlayer();

            var playerData = player.getPersistentDataContainer();
            var playerPastry = playerData.get(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE);
            if (playerPastry == null) {
                playerPastry = new PastryData();
                playerData.set(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE, playerPastry);
            }

            var targetData = targetPlayer.getPersistentDataContainer();
            var targetPastry = targetData.get(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE);
            if (targetPastry == null) {
                targetPastry = new PastryData();
                targetData.set(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE, targetPastry);
            }

            long now = System.currentTimeMillis();
            if (now - playerPastry.lastKneaded < 30 * 1000) {
                long cdTime = 30 - (now - playerPastry.lastKneaded) / 1000;
                player.sendMessage(configManager.getTranslated(Constants.MESSAGE_COUNTDOWN, cdTime));
                return;
            }
            playerPastry.lastKneaded = now;
            playerData.set(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE, playerPastry);

            logger.info(player.getName() + " Kneaded " + targetPlayer.getName());

            var playerUuid = player.getUniqueId();
            if (!targetPastry.favorability.containsKey(playerUuid)) {
                targetPastry.favorability.put(playerUuid, 0);
            }
            targetPastry.favorability.put(playerUuid, targetPastry.favorability.get(playerUuid) + 1);
            targetData.set(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE, targetPastry);

            player.sendMessage(configManager.getTranslated(Constants.MESSAGE_KNEAD_PLAYER, targetPlayer.getName()));
            targetPlayer.sendMessage(configManager.getTranslated(Constants.MESSAGE_KNEAD_BY_PLAYER, player.getName()));

            configManager.increaseKneadData(player.getName(), targetPlayer.getName());
            configManager.save();
            plugin.getScoreboardsManager().update();
        }
    }
}
