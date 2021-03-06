package cx.rain.mc.pastrymaster;

import cx.rain.mc.pastrymaster.command.PastryMasterCommand;
import cx.rain.mc.pastrymaster.config.ConfigManager;
import cx.rain.mc.pastrymaster.listener.ListenerPlayerJoin;
import cx.rain.mc.pastrymaster.listener.ListenerPlayerKnead;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.Set;

public final class PastryMaster extends JavaPlugin {
    private static PastryMaster INSTANCE;

    private ConfigManager configManager;
    private FileConfiguration config;
    /**
     * 面点大师排行榜
     */
    private Scoreboard pastryMasterBoard;
    /**
     * 最受欢迎榜
     */
    private Scoreboard mostPopularBoard;

    public PastryMaster() {
        INSTANCE = this;

        configManager = new ConfigManager(this);
    }

    public static PastryMaster getInstance() {
        return INSTANCE;
    }

    public Scoreboard getPastryMasterBoard() {
        return pastryMasterBoard;
    }

    public Scoreboard getMostPopularBoard() {
        return mostPopularBoard;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        configManager.load();
        config = getConfig();

        Bukkit.getPluginManager().registerEvents(new ListenerPlayerKnead(INSTANCE), this);
        Bukkit.getPluginManager().registerEvents(new ListenerPlayerJoin(INSTANCE), this);
        Bukkit.getPluginCommand("pastrymaster").setExecutor(new PastryMasterCommand(INSTANCE));

        setupScoreboard();

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

    private void setupScoreboard() {
        //Setup scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        pastryMasterBoard = manager.getNewScoreboard();
        mostPopularBoard = manager.getNewScoreboard();
        Objective pastryMasterObjective = pastryMasterBoard.registerNewObjective("board", "dummy", config.getString("messages.scoreboard_master"));
        Objective mostPopularObjective = mostPopularBoard.registerNewObjective("board", "dummy", config.getString("messages.scoreboard_popular"));
        pastryMasterObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        mostPopularObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        //load score from yaml
        YamlConfiguration data = getData();
        if (!data.contains("pastryMaster")) {
            data.createSection("pastryMaster");
        }
        if (!data.contains("mostPopular")) {
            data.createSection("mostPopular");
        }
        if (!data.contains("playerScoreboard")) {
            data.createSection("playerScoreboard");
        }
        if (!data.contains("lastKneadTime")) {
            data.createSection("lastKneadTime");
        }

        ConfigurationSection section = data.getConfigurationSection("pastryMaster");
        Set<String> keys = section.getKeys(false);
        for (String key : keys) {
            Score score = pastryMasterObjective.getScore(key);
            score.setScore(section.getInt(key));
        }
        section = data.getConfigurationSection("mostPopular");
        keys = section.getKeys(false);
        for (String key : keys) {
            Score score = mostPopularObjective.getScore(key);
            score.setScore(section.getInt(key));
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

    public void saveData() {
        configManager.save();
    }
}
