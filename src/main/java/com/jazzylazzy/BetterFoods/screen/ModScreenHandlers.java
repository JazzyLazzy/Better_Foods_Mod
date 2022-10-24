package com.jazzylazzy.BetterFoods.screen;

import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {

    public static ScreenHandlerType<RiceCookerScreenHandler> RICE_COOKER_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        RICE_COOKER_SCREEN_HANDLER = new ScreenHandlerType<>(RiceCookerScreenHandler::new);
    }

}
