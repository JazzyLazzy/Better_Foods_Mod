package com.jazzylazzy.BetterFoods;

import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import com.jazzylazzy.BetterFoods.screen.ModScreenHandlers;
import com.jazzylazzy.BetterFoods.screen.RiceCookerScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

public class BetterFoodsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RICE_CROP_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TALL_RICE_CROP_BLOCK, RenderLayer.getCutout());
        HandledScreens.register(ModScreenHandlers.RICE_COOKER_SCREEN_HANDLER, RiceCookerScreen::new);
    }
}
