package com.jazzylazzy.BetterFoods.recipes;

import com.jazzylazzy.BetterFoods.BetterFoods;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        BetterFoods.LOGGER.debug("Registering recipes");
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(BetterFoods.MOD_ID, CookedRiceRecipe.Serializer.ID),
                CookedRiceRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(BetterFoods.MOD_ID, CookedRiceRecipe.Type.ID),
                CookedRiceRecipe.Type.INSTANCE);
    }
}
