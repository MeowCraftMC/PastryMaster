package cx.rain.mc.pastrymaster;

import cx.rain.mc.pastrymaster.command.PastryMasterCommand;
import cx.rain.mc.pastrymaster.managers.ConfigManager;
import cx.rain.mc.pastrymaster.listener.ListenerPlayerJoin;
import cx.rain.mc.pastrymaster.listener.ListenerPlayerKnead;
import cx.rain.mc.pastrymaster.managers.ScoreboardsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public final class PastryMaster extends JavaPlugin {
    private static PastryMaster INSTANCE;

    private final ConfigManager configManager;
    private final ScoreboardsManager scoreboardsManager;

    public PastryMaster() {
        INSTANCE = this;

        configManager = new ConfigManager(this);
        scoreboardsManager = new ScoreboardsManager(this);
    }

    public static PastryMaster getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        configManager.load();
        scoreboardsManager.init();

        Bukkit.getPluginManager().registerEvents(new ListenerPlayerKnead(this), this);
        Bukkit.getPluginManager().registerEvents(new ListenerPlayerJoin(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("pastrymaster")).setExecutor(new PastryMasterCommand(INSTANCE));

        getLogger().info("Are you pastry master?"); //Indicating load finished

        try {
            Class.forName("cx.rain.mc.bukkit.loreanvil.LoreAnvil");
            Plugin plugin = Bukkit.getPluginManager().getPlugin("LoreAnvil");
            plugin.getLogger().info("Oh, I'm sad, there is a bug.");
            getLogger().info("Don't be sad, have a hug. Tee tee!");
            plugin.getLogger().info("Thx!");
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

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ScoreboardsManager getScoreboardsManager() {
        return scoreboardsManager;
    }
}
