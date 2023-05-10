package cx.rain.mc.pastrymaster.data;

import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class WorldPastryContainerType implements PersistentDataType<PersistentDataContainer, WorldPastryData> {
    public static final WorldPastryContainerType INSTANCE = new WorldPastryContainerType();

    public static final NamespacedKey NAMESPACED_KEY_KNEAD_COUNT = new NamespacedKey(PastryMaster.getInstance(), "favorability");
    public static final NamespacedKey NAMESPACED_KEY_KNEAD_COUNT_UUID = new NamespacedKey(PastryMaster.getInstance(), "uuid");
    public static final NamespacedKey NAMESPACED_KEY_KNEAD_COUNT_VALUE = new NamespacedKey(PastryMaster.getInstance(), "value");

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<WorldPastryData> getComplexType() {
        return WorldPastryData.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(WorldPastryData complex, PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        var arr = new ArrayList<PersistentDataContainer>();
        for (var entry : complex.kneadCount.entrySet()) {
            var c = context.newPersistentDataContainer();
            c.set(NAMESPACED_KEY_KNEAD_COUNT_UUID, STRING, entry.getKey().toString());
            c.set(NAMESPACED_KEY_KNEAD_COUNT_VALUE, INTEGER, entry.getValue());
            arr.add(c);
        }
        container.set(NAMESPACED_KEY_KNEAD_COUNT, TAG_CONTAINER_ARRAY, arr.toArray(new PersistentDataContainer[0]));
        return container;
    }

    @Override
    public WorldPastryData fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
        var data = new WorldPastryData();
        for (var c : primitive.get(NAMESPACED_KEY_KNEAD_COUNT, TAG_CONTAINER_ARRAY)) {
            var uuid = UUID.fromString(c.get(NAMESPACED_KEY_KNEAD_COUNT_UUID, STRING));
            var value = c.get(NAMESPACED_KEY_KNEAD_COUNT_VALUE, INTEGER);
            data.kneadCount.put(uuid, value);
        }
        return data;
    }
}
