package com.jazzylazzy.BetterFoods;

import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import com.jazzylazzy.BetterFoods.items.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterFoods implements ModInitializer {
	public static final String MOD_ID = "better_foods";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		System.out.println("hello minecraft world");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}
