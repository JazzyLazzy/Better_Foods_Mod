package com.jazzylazzy.BetterFoods;

import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import com.jazzylazzy.BetterFoods.blocks.entities.ModBlockEntities;
import com.jazzylazzy.BetterFoods.fluids.ModFluids;
import com.jazzylazzy.BetterFoods.items.ModItems;
import com.jazzylazzy.BetterFoods.recipes.ModRecipes;
import com.jazzylazzy.BetterFoods.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterFoods implements ModInitializer {
	public static final String MOD_ID = "better_foods";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		System.out.println("hello minecraft world");
		//Register Everything
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModScreenHandlers.registerAllScreenHandlers();
		ModFluids.registerModFluids();
		ModBlockEntities.registerModBlockEntities();
		ModRecipes.registerRecipes();
	}
}
