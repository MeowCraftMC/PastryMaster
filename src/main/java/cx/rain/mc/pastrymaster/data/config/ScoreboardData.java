package cx.rain.mc.pastrymaster.data.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardData implements ConfigurationSerializable {

    // Todo: Use UUID instead of String to point to a player.
    public Map<String, Integer> popular = new HashMap<>();
    public Map<String, Integer> pastryMaster = new HashMap<>();

    public ScoreboardData() {
    }

    /**
     * For deserialize.
      * @param map Map to deserialize.
     */
    public ScoreboardData(Map<String, Object> map) {
        var popularObject = map.get("popular");
        if (popularObject instanceof Map<?, ?> m) {
            popular = (Map<String, Integer>) m;
        }

        var pastryMasterObject = map.get("pasterMaster");
        if (pastryMasterObject instanceof Map<?, ?> m) {
            pastryMaster = (Map<String, Integer>) m;
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put("popular", popular);
        map.put("pasterMaster", pastryMaster);
        return map;
    }

    public Map<String, Integer> getPastryMaster() {
        return pastryMaster;
    }

    public Map<String, Integer> getPopular() {
        return popular;
    }
}
