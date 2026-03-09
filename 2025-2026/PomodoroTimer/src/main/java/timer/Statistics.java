package timer;

import logging.ILogger;
import logging.Loggers;
import settings.AppSettings;

public class Statistics {
    private static final ILogger logger = Loggers.get(Statistics.class);
    private static volatile Statistics instance;

    // Переменные для статистики
    private int totalWorkTimeMinutes;              // Общее время работы (минуты)
    private int totalBreakTimeMinutes;              // Общее время отдыха (минуты)
    private int completedCycles;                      // Количество завершенных циклов

    private Statistics() {
        logger.info("Initializing %s", "Statistics");

        this.totalWorkTimeMinutes = 0;
        this.totalBreakTimeMinutes = 0;
        this.completedCycles = 0;
    }

    public static Statistics getInstance() {
        if (instance == null) {
            synchronized (Statistics.class) {
                if (instance == null) {
                    instance = new Statistics();
                }
            }
        }
        return instance;
    }

    public void addTotalWorkTime(int value) {
        totalWorkTimeMinutes += value;
    }
    public void addTotalBreakTime(int value) {
        totalBreakTimeMinutes += value;
    }
    public void addCompletedCycles(int value) {
        completedCycles += value;
    }

    public void setTotalWorkTime(int value) {
        totalWorkTimeMinutes = value;
    }
    public void setTotalBreakTime(int value) {
        totalBreakTimeMinutes = value;
    }
    public void setCompletedCycles(int value) {
        completedCycles = value;
    }

    public int getTotalWorkTime() {
        return totalWorkTimeMinutes;
    }
    public int getTotalBreakTime() {
        return totalBreakTimeMinutes;
    }
    public int getCompletedCycles() {
        return completedCycles;
    }
}
