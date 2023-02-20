import java.util.ArrayList;
import java.util.List;

public class Score {
    // score counter
    private static int score = 0;
    public static void inc() {
        Score.score += 1;
        for (int i=0; i<listeners.size(); i++) {
            ScoreListener sl = listeners.get(i);
            sl.actionPerformed();
        }
    }
    public static int get() {
        return score;
    }

    // event listener
    private static final List<ScoreListener> listeners = new ArrayList<ScoreListener>();
    public static void addScoreListener(ScoreListener toAdd) {
        listeners.add(toAdd);
    }
}
