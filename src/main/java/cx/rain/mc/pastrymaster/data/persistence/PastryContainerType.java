package cx.rain.mc.pastrymaster.data.persistence;

import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PastryContainerType implements PersistentDataType<PersistentDataContainer, PastryData> {
    public static final PastryContainerType INSTANCE = new PastryContainerType();
    public static final NamespacedKey NAMESPACED_KEY_DATA_TYPE = new NamespacedKey(PastryMaster.getInstance(), "pastry");

    protected static final NamespacedKey NAMESPACED_KEY_SCOREBOARD_TYPE = new NamespacedKey(PastryMaster.getInstance(), "kneaded_count");
    protected static final NamespacedKey NAMESPACED_KEY_LAST_KNEADED = new NamespacedKey(PastryMaster.getInstance(), "last_kneaded");
    protected static final NamespacedKey NAMESPACED_KEY_FAVORABILITY = new NamespacedKey(PastryMaster.getInstance(), "favorability");
    protected static final NamespacedKey NAMESPACED_KEY_FAVORABILITY_UUID = new NamespacedKey(PastryMaster.getInstance(), "uuid");
    protected static final NamespacedKey NAMESPACED_KEY_FAVORABILITY_VALUE = new NamespacedKey(PastryMaster.getInstance(), "value");

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<PastryData> getComplexType() {
        return PastryData.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(PastryData complex, PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        container.set(NAMESPACED_KEY_SCOREBOARD_TYPE, STRING, complex.scoreboardType);
        container.set(NAMESPACED_KEY_LAST_KNEADED, LONG, complex.lastKneaded);

        var arr = new ArrayList<PersistentDataContainer>();
        for (var entry : complex.favorability.entrySet()) {
            var c = context.newPersistentDataContainer();
            c.set(NAMESPACED_KEY_FAVORABILITY_UUID, STRING, entry.getKey().toString());
            c.set(NAMESPACED_KEY_FAVORABILITY_VALUE, INTEGER, entry.getValue());
            arr.add(c);
        }
        container.set(NAMESPACED_KEY_FAVORABILITY, TAG_CONTAINER_ARRAY, arr.toArray(new PersistentDataContainer[0]));
        return container;
    }

    @Override
    public @NotNull PastryData fromPrimitive(PersistentDataContainer primitive,
                                             @NotNull PersistentDataAdapterContext context) {
        var data = new PastryData();
        data.scoreboardType = primitive.get(NAMESPACED_KEY_SCOREBOARD_TYPE, STRING);
        data.lastKneaded = primitive.getOrDefault(NAMESPACED_KEY_LAST_KNEADED, LONG, 0L);
        for (var c : Objects.requireNonNull(primitive.get(NAMESPACED_KEY_FAVORABILITY, TAG_CONTAINER_ARRAY))) {
            var uuid = UUID.fromString(Objects.requireNonNull(c.get(NAMESPACED_KEY_FAVORABILITY_UUID, STRING)));
            var value = c.get(NAMESPACED_KEY_FAVORABILITY_VALUE, INTEGER);
            data.favorability.put(uuid, value);
        }
        return data;
    }
}
