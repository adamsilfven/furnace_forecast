package com.adamsilfven.furnaceforecast.mixin;

import com.adamsilfven.furnaceforecast.ClientTooltip;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Client-only hook; the vanilla screen still renders normally. */
@Mixin(AbstractFurnaceScreen.class)
public abstract class FurnaceScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void furnaceforecast$afterRender(GuiGraphics graphics, int mouseX, int mouseY,
                                              float partialTick, CallbackInfo ci) {
        ClientTooltip.draw((AbstractFurnaceScreen<?>)(Object)this, graphics, mouseX, mouseY);
    }
}
