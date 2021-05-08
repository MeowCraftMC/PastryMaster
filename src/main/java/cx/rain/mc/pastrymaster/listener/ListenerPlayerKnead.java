package cx.rain.mc.pastrymaster.listener;

import cx.rain.mc.pastrymaster.PastryMaster;
import net.md_5.bungee.api.ChatColor;
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
    private PastryMaster plugin;

    public ListenerPlayerKnead(PastryMaster instance) {
        plugin = instance;
        config = plugin.getConfig();
        data = plugin.getData();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Player && event.getHand().name().equals("HAND")) {
            Player targetPlayer = (Player) entity;
            Player player = event.getPlayer();

            long now = System.currentTimeMillis();
            long lastKneadTime = data.getLong("lastKneadTime." + player.getName(), 0L);
            if (now - lastKneadTime < 30 * 1000) {
                long cdTime = 30 - (now - lastKneadTime) / 1000;
                player.sendMessage("§d你刚刚已经揉过别人了，请等待 " + cdTime + " 秒再来～");
                return;
            }
            data.set("lastKneadTime." + player.getName(), now);

            plugin.getLogger().info(player.getName() + " 揉了揉 " + targetPlayer.getName());

            TextComponent text = new TextComponent(String.format(
                    config.getString("messages.knead_player"), targetPlayer.getName()));
            text.setColor(ChatColor.AQUA);
            player.spigot().sendMessage(text);

            TextComponent targetText = new TextComponent(String.format(
                    config.getString("messages.knead_by_player"), player.getName()));
            targetText.setColor(ChatColor.GOLD);
            targetPlayer.spigot().sendMessage(targetText);

            int masterCount = data.getInt("pastryMaster." + player.getName(), 0);
            int popularCount = data.getInt("mostPopular." + targetPlayer.getName(), 0);
            plugin.getPastryMasterBoard().getObjective("board").getScore(player.getName()).setScore(++masterCount);
            plugin.getMostPopularBoard().getObjective("board").getScore(targetPlayer.getName()).setScore(++popularCount);
            data.set("pastryMaster." + player.getName(), masterCount);
            data.set("mostPopular." + targetPlayer.getName(), popularCount);
            plugin.saveData();
        }
    }
}
