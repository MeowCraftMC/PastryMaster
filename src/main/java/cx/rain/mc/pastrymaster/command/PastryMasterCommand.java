package cx.rain.mc.pastrymaster.command;

import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PastryMasterCommand implements TabExecutor {
    private final String[] subCommands = {"master", "welcome"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //处理命令
        if (sender instanceof Player) {
            if (args.length == 0)
                sender.sendMessage("参数不够");
            else if (args[0].equals("master"))
                ((Player) sender).setScoreboard(PastryMaster.getInstance().getPastryMasterBoard());
            else if (args[0].equals("welcome"))
                ((Player) sender).setScoreboard(PastryMaster.getInstance().getMostPopularBoard());
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
