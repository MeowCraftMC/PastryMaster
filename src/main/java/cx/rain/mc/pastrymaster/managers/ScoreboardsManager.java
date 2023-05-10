package cx.rain.mc.pastrymaster.managers;

import cx.rain.mc.pastrymaster.Constants;
import cx.rain.mc.pastrymaster.PastryMaster;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

public class ScoreboardsManager {
    private final ConfigManager configManager;

    /**
     * 面点大师排行榜
     */
    private Scoreboard pastryMasterBoard;

    /**
     * 最受欢迎榜
     */
    private Scoreboard mostPopularBoard;

    private Objective pastryMasterObjective;
    private Objective mostPopularObjective;

    public ScoreboardsManager(PastryMaster plugin) {
        this.configManager = plugin.getConfigManager();
    }

    public void init() {
        // Setup scoreboard
        var manager = Bukkit.getScoreboardManager();
        assert manager != null;
        pastryMasterBoard = manager.getNewScoreboard();
        mostPopularBoard = manager.getNewScoreboard();
        pastryMasterObjective = pastryMasterBoard.registerNewObjective("board", "dummy", configManager.getTranslated(Constants.MESSAGE_SCOREBOARD_MASTER));
        mostPopularObjective = mostPopularBoard.registerNewObjective("board", "dummy", configManager.getTranslated(Constants.MESSAGE_SCOREBOARD_POPULAR));
        pastryMasterObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        mostPopularObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        update();
    }

    public Scoreboard getMostPopularBoard() {
        return mostPopularBoard;
    }

    public Scoreboard getPastryMasterBoard() {
        return pastryMasterBoard;
    }

    public void update() {
        for (var entry : configManager.getPastryMasterData().entrySet()) {
            Score score = pastryMasterObjective.getScore(entry.getKey());
            score.setScore(entry.getValue());
        }

        for (var entry : configManager.getPopularData().entrySet()) {
            Score score = mostPopularObjective.getScore(entry.getKey());
            score.setScore(entry.getValue());
        }
    }
}
