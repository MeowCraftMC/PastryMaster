package cx.rain.mc.pastrymaster.command;

import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PastryMasterCommand implements TabExecutor {
    private PastryMaster plugin;
    private YamlConfiguration data;
    private final String[] subCommands = {"master", "popular"};

    public PastryMasterCommand(PastryMaster instance) {
        plugin = instance;
        data = plugin.getData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //处理命令
        if (sender instanceof Player) {
            if (args.length == 0)
                sender.sendMessage("§c参数不够");
            else if (args[0].equals("master")) {
                ((Player) sender).setScoreboard(PastryMaster.getInstance().getPastryMasterBoard());
                sender.sendMessage("§3你的记分板已切换至 面点大师榜");
                data.set("playerScoreboard." + sender.getName(), "master");
                plugin.saveData();
            } else if (args[0].equals("popular")) {
                ((Player) sender).setScoreboard(PastryMaster.getInstance().getMostPopularBoard());
                sender.sendMessage("§6你的记分板已切换至 最受欢迎榜");
                data.set("playerScoreboard." + sender.getName(), "popular");
                plugin.saveData();
            } else
                sender.sendMessage("§c参数错误");
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
