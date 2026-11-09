package com.adamsilfven.furnaceforecast;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.common.MinecraftForge;

@Mod("furnace_forecast")
public final class FurnaceForecast {
    public FurnaceForecast() {
        // Never load a Minecraft client class on a dedicated server.
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.addListener(ClientTooltip::onScreenRender);
        }
    }
}
