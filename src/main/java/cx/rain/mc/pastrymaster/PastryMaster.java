package cx.rain.mc.pastrymaster;

import cx.rain.mc.pastrymaster.command.PastryMasterCommand;
import cx.rain.mc.pastrymaster.config.ConfigManager;
import cx.rain.mc.pastrymaster.listener.ListenerPlayerKnead;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.Set;

public final class PastryMaster extends JavaPlugin {
    private static PastryMaster INSTANCE;

    private ConfigManager configManager;
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

    public Scoreboard getPastryMasterBoard(){
        return pastryMasterBoard;
    }

    public Scoreboard getMostPopularBoard(){
        return mostPopularBoard;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        configManager.load();

        Bukkit.getPluginManager().registerEvents(new ListenerPlayerKnead(), this);
        Bukkit.getPluginCommand("pastrymaster").setExecutor(new PastryMasterCommand());

        SetupScoreboard();

        getLogger().info("Are you pastry master?"); //Indicating load finished

        try {
            Class.forName("cx.rain.mc.bukkit.loreanvil.LoreAnvil");
            Plugin plugin = Bukkit.getPluginManager().getPlugin("LoreAnvil");
            plugin.getLogger().info("Oh, I'm sad, there are no hug.");
            getLogger().info("Don't be sad, have a hug. Tee tee!");
            plugin.getLogger().info("Fantastic!");
        } catch (ClassNotFoundException ignored) {
            // Silence is gold.
        }
    }

    private void SetupScoreboard() {
        //Setup scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        pastryMasterBoard = manager.getNewScoreboard();
        mostPopularBoard = manager.getNewScoreboard();
        Objective pastryMasterObjective = pastryMasterBoard.registerNewObjective("board", "dummy", "§3面点大师榜");
        Objective mostPopularObjective = mostPopularBoard.registerNewObjective("board", "dummy", "§6最受欢迎榜");
        pastryMasterObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        mostPopularObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        //load score from yaml
        YamlConfiguration data=getData();
        if(!data.contains("pastryMaster"))
            data.createSection("pastryMaster");
        if(!data.contains("mostPopular"))
            data.createSection("mostPopular");
        ConfigurationSection section = data.getConfigurationSection("pastryMaster");
        Set<String> keys = section.getKeys(false);
        for(String key:keys){
            Score score=pastryMasterObjective.getScore(key);
            score.setScore(section.getInt(key));
        }
        section = data.getConfigurationSection("mostPopular");
        keys = section.getKeys(false);
        for(String key:keys){
            Score score=mostPopularObjective.getScore(key);
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
}
