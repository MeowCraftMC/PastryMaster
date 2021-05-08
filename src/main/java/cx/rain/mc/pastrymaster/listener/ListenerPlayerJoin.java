package cx.rain.mc.pastrymaster.listener;

import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerPlayerJoin implements Listener {
    private YamlConfiguration data;
    private PastryMaster plugin;

    public ListenerPlayerJoin(PastryMaster instance) {
        plugin = instance;
        data = plugin.getData();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerScoreboard = data.getString("playerScoreboard." + player.getName(), "");
        if (playerScoreboard.equals("master")) {
            player.setScoreboard(PastryMaster.getInstance().getPastryMasterBoard());
        } else if (playerScoreboard.equals("popular")) {
            player.setScoreboard(PastryMaster.getInstance().getMostPopularBoard());
        }
    }
}
