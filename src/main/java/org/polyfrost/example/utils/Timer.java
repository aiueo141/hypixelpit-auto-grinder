package org.polyfrost.example.utils;

public class Timer {

    private long lastMS;

    private long getCurrentMilliseconds() {
        return System.nanoTime() / 1000000L;
    }

    public boolean passed(final double ms) {
        return getCurrentMilliseconds() - lastMS >= ms;
    }

    public void reset() {
        this.lastMS = this.getCurrentMilliseconds();
    }

    public long getTime() {
        return getCurrentMilliseconds() - this.lastMS;
    }
}
