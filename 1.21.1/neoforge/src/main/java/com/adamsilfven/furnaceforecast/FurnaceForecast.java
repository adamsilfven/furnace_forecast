package com.adamsilfven.furnaceforecast;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod("furnace_forecast")
public final class FurnaceForecast {
    public FurnaceForecast() {
        // Never load a Minecraft client class on a dedicated server.
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.addListener(ClientTooltip::onScreenRender);
        }
    }
}
