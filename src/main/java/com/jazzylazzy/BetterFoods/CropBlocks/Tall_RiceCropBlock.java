package com.jazzylazzy.BetterFoods.CropBlocks;

import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class Tall_RiceCropBlock extends CropBlock implements Fertilizable {

    public Tall_RiceCropBlock(AbstractBlock.Settings settings){
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isOf(ModBlocks.RICE_CROP_BLOCK) && super.canPlaceAt(state, world, pos);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(ModBlocks.RICE_CROP_BLOCK);
    }
}
