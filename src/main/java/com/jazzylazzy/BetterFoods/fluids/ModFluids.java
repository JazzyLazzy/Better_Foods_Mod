package com.jazzylazzy.BetterFoods.fluids;

import com.jazzylazzy.BetterFoods.BetterFoods;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModFluids {

    private static Fluid registerFluid(String name, Fluid fluid){
        return Registry.register(Registry.FLUID, new Identifier(BetterFoods.MOD_ID, name), fluid);
    }

    public static void registerModFluids(){
        BetterFoods.LOGGER.debug("registering fluids");
    }

}
