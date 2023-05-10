package cx.rain.mc.pastrymaster.data.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PastryData {
    public String scoreboardType = "";
    public long lastKneaded;
    public Map<UUID, Integer> favorability = new HashMap<>();
}
