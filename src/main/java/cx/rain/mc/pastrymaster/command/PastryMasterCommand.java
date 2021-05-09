package cx.rain.mc.pastrymaster.command;

import cx.rain.mc.pastrymaster.PastryMaster;
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
    private PastryMaster plugin;
    private FileConfiguration config;
    private YamlConfiguration data;
    private final String[] subCommands = {"master", "popular", "reset"};

    public PastryMasterCommand(PastryMaster instance) {
        plugin = instance;
        data = plugin.getData();
        config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //处理命令
        if (sender instanceof Player) {
            if (args.length == 0)
                sender.sendMessage(config.getString("messages.not_enough_args"));
            else if (args[0].equals("master")) {
                ((Player) sender).setScoreboard(PastryMaster.getInstance().getPastryMasterBoard());
                sender.sendMessage(config.getString("messages.scoreboard_switch_to_master"));
                data.set("playerScoreboard." + sender.getName(), "master");
                plugin.saveData();
            } else if (args[0].equals("popular")) {
                ((Player) sender).setScoreboard(PastryMaster.getInstance().getMostPopularBoard());
                sender.sendMessage(config.getString("messages.scoreboard_switch_to_popular"));
                data.set("playerScoreboard." + sender.getName(), "popular");
                plugin.saveData();
            } else if (args[0].equals("reset")) {
                sender.sendMessage(config.getString("messages.scoreboard_reset"));
                ((Player) sender).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            } else {
                sender.sendMessage(config.getString("messages.invalid_args"));
            }
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
