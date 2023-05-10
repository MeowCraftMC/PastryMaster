package cx.rain.mc.pastrymaster.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PastryData {
//    public int kneaded;
    public long lastKneaded;
    public Map<UUID, Integer> favorability = new HashMap<>();
}
