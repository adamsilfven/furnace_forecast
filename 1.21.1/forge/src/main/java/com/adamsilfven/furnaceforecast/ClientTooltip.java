package com.adamsilfven.furnaceforecast;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ForgeHooks;

/** Nothing on the HUD. Hover the ordinary furnace flame to see a normal tooltip. */
public final class ClientTooltip {
    private ClientTooltip() { }

    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof AbstractFurnaceScreen<?> screen)) return;

        int x = screen.getGuiLeft() + 56;
        int y = screen.getGuiTop() + 36;
        if (event.getMouseX() < x || event.getMouseX() >= x + 14 ||
                event.getMouseY() < y || event.getMouseY() >= y + 14) return;

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

            // A client cannot promise accurate custom modded recipes if it has no matching recipe.
            int recipeTime = minecraft.level.getRecipeManager()
                    .getRecipeFor(recipeType, new SingleRecipeInput(input), minecraft.level)
                    .map(held -> held.value().getCookingTime()).orElse(0);
            if (recipeTime <= 0) {
                lines.add(gray("No available cooking recipe"));
            } else {
                int litTicks = Math.max(0, menu.data.get(0));
                int cookProgress = Math.max(0, menu.data.get(2));
                int activeCookTime = menu.data.get(3);
                int tickTime = activeCookTime > 0 && cookProgress > 0 ? activeCookTime : recipeTime;
                ItemStack fuel = menu.getSlot(1).getItem();
                int burnTicksPerFuel = fuel.isEmpty() ? 0 : Math.max(0, ForgeHooks.getBurnTime(fuel, recipeType));

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
        event.getGuiGraphics().renderComponentTooltip(minecraft.font, lines,
                event.getMouseX(), event.getMouseY());
    }

    private static Component gray(String text) {
        return Component.literal(text).withStyle(ChatFormatting.GRAY);
    }
}
