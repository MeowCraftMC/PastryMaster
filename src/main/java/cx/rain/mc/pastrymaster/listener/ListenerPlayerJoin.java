package cx.rain.mc.pastrymaster.listener;

import cx.rain.mc.pastrymaster.PastryMaster;
import cx.rain.mc.pastrymaster.data.persistence.PastryContainerType;
import cx.rain.mc.pastrymaster.data.persistence.PastryData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        var data = player.getPersistentDataContainer();
        var pastry = data.get(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE);
        if (pastry == null) {
            pastry = new PastryData();
            data.set(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE, pastry);
        }

        if (pastry.scoreboardType.equals("master")) {
            player.setScoreboard(PastryMaster.getInstance().getScoreboardsManager().getPastryMasterBoard());
        } else if (pastry.scoreboardType.equals("popular")) {
            player.setScoreboard(PastryMaster.getInstance().getScoreboardsManager().getMostPopularBoard());
        }
    }
}
