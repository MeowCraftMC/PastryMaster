package cx.rain.mc.pastrymaster.command;

import cx.rain.mc.pastrymaster.Constants;
import cx.rain.mc.pastrymaster.PastryMaster;
import cx.rain.mc.pastrymaster.data.persistence.PastryContainerType;
import cx.rain.mc.pastrymaster.data.persistence.PastryData;
import cx.rain.mc.pastrymaster.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PastryMasterCommand implements TabExecutor {
    private final ConfigManager configManager;

    private final String[] subCommands = {"master", "popular", "reset"};

    public PastryMasterCommand(PastryMaster plugin) {
        configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //处理命令
        if (sender instanceof Player player) {
            var data = player.getPersistentDataContainer();
            var pastry = data.get(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE);
            if (pastry == null) {
                pastry = new PastryData();
                data.set(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE, pastry);
            }

            if (args.length == 0) {
                sender.sendMessage(configManager.getTranslated(Constants.MESSAGE_NOT_ENOUGH_ARGS));
                pastry.scoreboardType = "";
            }
            else if (args[0].equals("master")) {
                player.setScoreboard(PastryMaster.getInstance().getScoreboardsManager().getPastryMasterBoard());
                sender.sendMessage(configManager.getTranslated(Constants.MESSAGE_SWITCH_TO_MASTER));
                pastry.scoreboardType = "master";
            } else if (args[0].equals("popular")) {
                player.setScoreboard(PastryMaster.getInstance().getScoreboardsManager().getMostPopularBoard());
                sender.sendMessage(configManager.getTranslated(Constants.MESSAGE_SWITCH_TO_POPULAR));
                pastry.scoreboardType = "popular";
            } else if (args[0].equals("reset")) {
                sender.sendMessage(configManager.getTranslated(Constants.MESSAGE_SCOREBOARD_RESET));
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            } else {
                sender.sendMessage(configManager.getTranslated(Constants.MESSAGE_INVALID_ARGS));
                pastry.scoreboardType = "";
            }

            data.set(PastryContainerType.NAMESPACED_KEY_DATA_TYPE, PastryContainerType.INSTANCE, pastry);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //处理命令补全
        if (args.length > 1) return new ArrayList<>();
        if (args.length == 0) return Arrays.asList(subCommands);
        return Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }
}
