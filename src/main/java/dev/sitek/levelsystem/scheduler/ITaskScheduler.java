package dev.sitek.levelsystem.scheduler;

public interface ITaskScheduler {

    void runAsync(Runnable runnable);

    void runLaterAsync(Runnable runnable, long delay);

    void runTimerAsync(Runnable runnable, long delay, long period);

}
