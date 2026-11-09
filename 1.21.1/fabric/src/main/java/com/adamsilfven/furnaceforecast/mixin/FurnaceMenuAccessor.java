package com.adamsilfven.furnaceforecast.mixin;

import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Read the synchronized furnace progress; no world/server access needed. */
@Mixin(AbstractFurnaceMenu.class)
public interface FurnaceMenuAccessor {
    @Accessor("data")
    ContainerData furnaceforecast$getData();
}
