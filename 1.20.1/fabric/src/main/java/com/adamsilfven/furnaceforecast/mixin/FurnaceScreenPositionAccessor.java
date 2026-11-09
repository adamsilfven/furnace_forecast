package com.adamsilfven.furnaceforecast.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Read the actual top-left screen position, including recipe-book offsets. */
@Mixin(AbstractContainerScreen.class)
public interface FurnaceScreenPositionAccessor {
    @Accessor("leftPos")
    int furnaceforecast$getLeftPos();

    @Accessor("topPos")
    int furnaceforecast$getTopPos();
}
