package com.adamsilfven.furnaceforecast;

public final class ForecastMathTest {
    private static int passed = 0;
    public static void main(String[] args) {
        // 23 iron + 1 coal = 8 complete, another two coal required.
        var sample = ForecastMath.calculate(23, 200, 0, 0, 1, 1600);
        check(sample.itemsCovered() == 8, "23 iron, 1 coal: covered 8");
        check(sample.extraFuelItems() == 2, "23 iron, 1 coal: add 2");
        check(!sample.enoughFuel(), "23 iron, 1 coal: not enough");
        check(sample.ticksToFinish() == 4600, "23 iron total ticks");
        check(ForecastMath.formatTime(4600).equals("3m 50s"), "correct duration");
        var lit = ForecastMath.calculate(10, 200, 0, 400, 1, 1600);
        check(lit.enoughFuel(), "burning fuel and stacked fuel combine");
        check(lit.itemsCovered() == 10, "all 10 supported");
        var partial = ForecastMath.calculate(2, 200, 170, 230, 0, 0);
        check(partial.itemsCovered() == 2, "partial smelt counts toward coverage");
        check(partial.enoughFuel(), "partial smelt needs 230 ticks");
        var notEnoughPartial = ForecastMath.calculate(2, 200, 170, 229, 0, 0);
        check(notEnoughPartial.itemsCovered() == 1, "one tick short");
        check(!notEnoughPartial.enoughFuel(), "not enough when 1 tick short");
        var empty = ForecastMath.calculate(0, 200, 0, 0, 0, 0);
        check(empty.enoughFuel(), "empty queue enough");
        var zeroFuel = ForecastMath.calculate(6, 100, 0, 0, 0, 0);
        check(zeroFuel.itemsCovered() == 0, "smoker no fuel");
        var full = ForecastMath.calculate(64, 200, 0, 0, 1, 20000);
        check(full.enoughFuel(), "lava bucket enough for 64");
        check(full.itemsCovered() == 64, "lava capacity capped to batch");
        var exact = ForecastMath.calculate(8, 200, 0, 0, 1, 1600);
        check(exact.extraFuelItems() == 0 && exact.enoughFuel(), "exact 8 items fuel");
        check(ForecastMath.formatTime(1).equals("0m 1s"), "round up seconds");
        try { ForecastMath.calculate(-1, 200, 0, 0, 0, 1600); throw new AssertionError("invalid input accepted"); }
        catch (IllegalArgumentException ok) { passed++; }
        System.out.println("PASS: " + passed + " furnace arithmetic assertions");
    }
    private static void check(boolean ok, String description) {
        if (!ok) throw new AssertionError(description);
        passed++;
    }
}
