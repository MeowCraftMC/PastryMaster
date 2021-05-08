package cx.rain.mc.pastrymaster.config;

import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private PastryMaster plugin;
    private File dataFile;
    private YamlConfiguration data;

    public ConfigManager(PastryMaster pluginIn) {
        plugin = pluginIn;
        dataFile=new File(plugin.getDataFolder().getPath(), "data.yml");
        data= new YamlConfiguration();
    }

    public void load() {
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }

            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            data.save(dataFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public YamlConfiguration getData() {
        return data;
    }
}
