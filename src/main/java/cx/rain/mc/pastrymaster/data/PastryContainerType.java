package cx.rain.mc.pastrymaster.data;

import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class PastryContainerType implements PersistentDataType<PersistentDataContainer, PastryData> {
    public static final PastryContainerType INSTANCE = new PastryContainerType();

//    public static final NamespacedKey NAMESPACED_KEY_KNEADED_COUNT = new NamespacedKey(PastryMaster.getInstance(), "kneaded_count");
    public static final NamespacedKey NAMESPACED_KEY_LAST_KNEADED = new NamespacedKey(PastryMaster.getInstance(), "last_kneaded");
    public static final NamespacedKey NAMESPACED_KEY_FAVORABILITY = new NamespacedKey(PastryMaster.getInstance(), "favorability");
    public static final NamespacedKey NAMESPACED_KEY_FAVORABILITY_UUID = new NamespacedKey(PastryMaster.getInstance(), "uuid");
    public static final NamespacedKey NAMESPACED_KEY_FAVORABILITY_VALUE = new NamespacedKey(PastryMaster.getInstance(), "value");

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<PastryData> getComplexType() {
        return PastryData.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(PastryData complex, PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
//        container.set(NAMESPACED_KEY_KNEADED_COUNT, INTEGER, complex.kneaded);
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
    public PastryData fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
        var data = new PastryData();
//        data.kneaded = primitive.get(NAMESPACED_KEY_KNEADED_COUNT, INTEGER);
        data.lastKneaded = primitive.get(NAMESPACED_KEY_LAST_KNEADED, LONG);
        for (var c : primitive.get(NAMESPACED_KEY_FAVORABILITY, TAG_CONTAINER_ARRAY)) {
            var uuid = UUID.fromString(c.get(NAMESPACED_KEY_FAVORABILITY_UUID, STRING));
            var value = c.get(NAMESPACED_KEY_FAVORABILITY_VALUE, INTEGER);
            data.favorability.put(uuid, value);
        }
        return data;
    }
}
