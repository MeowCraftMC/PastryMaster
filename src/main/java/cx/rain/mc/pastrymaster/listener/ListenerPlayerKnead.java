package cx.rain.mc.pastrymaster.listener;

import cx.rain.mc.pastrymaster.PastryMaster;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ListenerPlayerKnead implements Listener {
    private FileConfiguration config;
    private YamlConfiguration data;

    public ListenerPlayerKnead() {
        config = PastryMaster.getInstance().getConfig();
        data = PastryMaster.getInstance().getData();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Player) {
            Player targetPlayer = (Player) entity;
            Player player = event.getPlayer();

            player.spigot().sendMessage(new TextComponent(
                    String.format(config.getString("messages.knead_player"),
                            targetPlayer.getName())));
            targetPlayer.spigot().sendMessage(new TextComponent(
                    String.format(config.getString("messages.knead_by_player"),
                            player.getName())));
        }
    }
}
