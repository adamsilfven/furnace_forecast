package com.adamsilfven.furnaceforecast;

import java.util.ArrayList;
import java.util.List;

import com.adamsilfven.furnaceforecast.mixin.FurnaceMenuAccessor;
import com.adamsilfven.furnaceforecast.mixin.FurnaceScreenPositionAccessor;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.SimpleContainer;

/** Vanilla-style tooltip, with the same layout and calculation as the 1.21.1 mod. */
public final class ClientTooltip {
    private ClientTooltip() { }

    public static void draw(AbstractFurnaceScreen<?> screen, GuiGraphics graphics, int mouseX, int mouseY) {
        FurnaceScreenPositionAccessor position = (FurnaceScreenPositionAccessor) screen;
        int x = position.furnaceforecast$getLeftPos() + 56;
        int y = position.furnaceforecast$getTopPos() + 36;
        if (mouseX < x || mouseX >= x + 14 || mouseY < y || mouseY >= y + 14) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;
        AbstractFurnaceMenu menu = screen.getMenu();
        ItemStack input = menu.getSlot(0).getItem();
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("Furnace Forecast").withStyle(ChatFormatting.WHITE));

        if (input.isEmpty()) {
            lines.add(gray("No items to cook"));
        } else {
            RecipeType<? extends AbstractCookingRecipe> recipeType = menu instanceof BlastFurnaceMenu
                    ? RecipeType.BLASTING : menu instanceof SmokerMenu ? RecipeType.SMOKING : RecipeType.SMELTING;
            int recipeTime = minecraft.level.getRecipeManager()
                    .getRecipeFor(recipeType, new SimpleContainer(input), minecraft.level)
                    .map(AbstractCookingRecipe::getCookingTime).orElse(0);
            if (recipeTime <= 0) {
                lines.add(gray("No available cooking recipe"));
            } else {
                ContainerData data = ((FurnaceMenuAccessor) menu).furnaceforecast$getData();
                int litTicks = Math.max(0, data.get(0));
                int cookProgress = Math.max(0, data.get(2));
                int activeCookTime = data.get(3);
                int tickTime = activeCookTime > 0 && cookProgress > 0 ? activeCookTime : recipeTime;
                ItemStack fuel = menu.getSlot(1).getItem();
                Integer registeredBurnTicks = fuel.isEmpty() ? null : FuelRegistry.INSTANCE.get(fuel.getItem());
                int burnTicksPerFuel = registeredBurnTicks == null ? 0 : Math.max(0, registeredBurnTicks);

                ForecastMath.Forecast f = ForecastMath.calculate(input.getCount(), tickTime,
                        cookProgress, litTicks, fuel.getCount(), burnTicksPerFuel);

                lines.add(gray("Batch: " + input.getCount() + " items"));
                lines.add(gray("Time: ~" + ForecastMath.formatTime(f.ticksToFinish())
                        + (f.enoughFuel() ? "" : " (after fueling)")));

                if (!fuel.isEmpty() && burnTicksPerFuel == 0) {
                    lines.add(gray("Fuel capacity unknown"));
                } else if (f.enoughFuel()) {
                    lines.add(Component.literal("Fuel: Enough (" + f.itemsCovered() + "/" + input.getCount() + ")")
                            .withStyle(ChatFormatting.GREEN));
                } else {
                    lines.add(Component.literal("Fuel: " + f.itemsCovered() + "/" + input.getCount() + " items")
                            .withStyle(ChatFormatting.RED));
                    if (burnTicksPerFuel > 0) {
                        String name = fuel.getHoverName().getString();
                        lines.add(Component.literal("Add " + f.extraFuelItems() + " " + name)
                                .withStyle(ChatFormatting.YELLOW));
                    } else {
                        lines.add(gray("Add fuel to finish"));
                    }
                }
            }
        }
        graphics.renderComponentTooltip(minecraft.font, lines, mouseX, mouseY);
    }

    private static Component gray(String text) {
        return Component.literal(text).withStyle(ChatFormatting.GRAY);
    }
}
