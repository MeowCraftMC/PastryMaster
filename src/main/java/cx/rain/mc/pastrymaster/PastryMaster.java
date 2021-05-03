package cx.rain.mc.pastrymaster;

import cx.rain.mc.pastrymaster.config.ConfigManager;
import cx.rain.mc.pastrymaster.listener.ListenerPlayerKnead;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PastryMaster extends JavaPlugin {
    private static PastryMaster INSTANCE;

    private ConfigManager configManager;

    public PastryMaster() {
        INSTANCE = this;

        //configManager = new ConfigManager(this);
    }

    public static PastryMaster getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Are you pastry master?");


        saveDefaultConfig();
        //configManager.load();

        Bukkit.getPluginManager().registerEvents(new ListenerPlayerKnead(), this);

        try {
            Class.forName("cx.rain.mc.bukkit.loreanvil");
            Plugin plugin = Bukkit.getPluginManager().getPlugin("LoreAnvil");
            plugin.getLogger().info("Oh, I'm sad, there are no hug.");
            getLogger().info("Don't be sad, have a hug. Tee tee!");
            plugin.getLogger().info("Fantastic!");
        } catch (ClassNotFoundException ignored) {
            // Silence is gold.
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //configManager.save();
        saveConfig();

        getLogger().info("Goodbye, pastry master.");
    }

    public YamlConfiguration getData() {
        return configManager.getData();
    }
}
