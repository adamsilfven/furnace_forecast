package com.adamsilfven.furnaceforecast;

/** Only arithmetic, deliberately independent of Minecraft/NeoForge. */
public final class ForecastMath {
    private ForecastMath() { }

    public record Forecast(long ticksToFinish, int itemsCovered, int extraFuelItems, boolean enoughFuel) { }

    public static Forecast calculate(int items, int ticksPerItem, int progressTicks,
                                     int litTicksRemaining, int queuedFuelItems, int ticksPerFuel) {
        if (items < 0 || ticksPerItem <= 0 || progressTicks < 0 || litTicksRemaining < 0 ||
                queuedFuelItems < 0 || ticksPerFuel < 0) {
            throw new IllegalArgumentException("Invalid furnace forecast inputs");
        }
        long progress = Math.min(progressTicks, ticksPerItem - 1);
        long neededTicks = Math.max(0L, (long) items * ticksPerItem - progress);
        long availableTicks = (long) litTicksRemaining + (long) queuedFuelItems * ticksPerFuel;
        // The partial progress of the first item counts toward total coverage.
        int coverage = (int) Math.min(items, (availableTicks + progress) / ticksPerItem);
        long shortfallTicks = Math.max(0L, neededTicks - availableTicks);
        int additional = ticksPerFuel == 0 ? 0 : (int) Math.min(Integer.MAX_VALUE,
                (shortfallTicks + ticksPerFuel - 1L) / ticksPerFuel);
        return new Forecast(neededTicks, coverage, additional, shortfallTicks == 0);
    }

    public static String formatTime(long ticks) {
        long seconds = (ticks + 19L) / 20L;
        return (seconds / 60) + "m " + (seconds % 60) + "s";
    }
}
