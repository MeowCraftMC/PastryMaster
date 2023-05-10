package cx.rain.mc.pastrymaster.managers;

import cx.rain.mc.pastrymaster.Constants;
import cx.rain.mc.pastrymaster.PastryMaster;
import cx.rain.mc.pastrymaster.data.config.ScoreboardData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.scoreboard.Score;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ConfigManager {
    private final FileConfiguration config;
    private final File dataFile;
    private final YamlConfiguration data;

    private ScoreboardData scoreboardData = new ScoreboardData();

    static {
        ConfigurationSerialization.registerClass(ScoreboardData.class);
    }

    public ConfigManager(PastryMaster plugin) {
        config = plugin.getConfig();
        dataFile = new File(plugin.getDataFolder().getPath(), "data.yml");
        data = new YamlConfiguration();
    }

    public void load() {
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
                save();
            }

            data.load(dataFile);
            scoreboardData = data.getObject("data", ScoreboardData.class);
            if (scoreboardData == null) {
                scoreboardData = new ScoreboardData();
            }

        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            data.set("data", scoreboardData);
            data.save(dataFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, Integer> getPastryMasterData() {
        return scoreboardData.getPastryMaster();
    }

    public Map<String, Integer> getPopularData() {
        return scoreboardData.getPopular();
    }

    public String getTranslated(String key, Object... params) {
        var translate = config.getString(Constants.MESSAGE_ROOT + "." + key);
        if (translate == null) {
            translate = "";
        }
        return String.format(translate, params);
    }

    public void increaseKneadData(String player, String target) {
        var masterity = getPastryMasterData().get(player);
        if (masterity == null) {
            masterity = 0;
        }
        getPastryMasterData().put(player, masterity + 1);

        var popularity = getPopularData().get(target);
        if (popularity == null) {
            popularity = 0;
        }
        getPopularData().put(target, popularity + 1);
    }
}
