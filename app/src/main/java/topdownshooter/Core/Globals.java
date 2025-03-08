package topdownshooter.Core;

public interface Globals {
    public static final String CONFIGURATION_FILE = "config.ini";
    public static final int GAME_TICK_MS = 15;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int WAVE_SUSPEND_DURATION_MS = 5000;

    static int Time2GameTick(int durationMs) {
        return durationMs / GAME_TICK_MS;
    }

    static int GameTick2Time(int tick) {
        return tick * GAME_TICK_MS;
    }

}
