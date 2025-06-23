package xyz.devcmb.treeTumblers.timers;

import xyz.devcmb.treeTumblers.TreeTumblers;

import java.util.HashMap;
import java.util.Map;
public class TimerManager {
    private static final Map<String, Timer> timers = new HashMap<>();
    public static boolean paused = false;
    public static Timer activeTimer = null;

    public static void registerTimer(String name, Timer timer){
        timers.put(name, timer);
    }

    public static void registerAllTimers(){
        // TODO: do this
    }

    public static Timer runTimer(String name) {
        Timer timer = timers.get(name);

        if (timer == null) {
            TreeTumblers.LOGGER.warning("Failed to find a timer for " + name);
            return null;
        }

        activeTimer = timer;

        timer.run();
        return timer;
    }

    public static void clearTimers(){
        activeTimer = null;
    }

    public static void endActiveTimers(){
        activeTimer.end();
        activeTimer = null;
    }
}
