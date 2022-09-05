package com.jazzylazzy.BetterFoods.CropBlocks;

import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import com.jazzylazzy.BetterFoods.items.ModItems;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RiceCropBlock extends CropBlock implements Waterloggable{

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty AGE = IntProperty.of("age", 0, 7);

    public RiceCropBlock(Settings settings){
        super(settings);
        this.setDefaultState(((BlockState)this.getDefaultState().with(WATERLOGGED, false)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(AGE);
        stateManager.add(WATERLOGGED);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected ItemConvertible getSeedsItem(){
        return ModItems.HUSKED_RICE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState()
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state){
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        System.out.println(state.get(Properties.WATERLOGGED));
        return state.get(Properties.WATERLOGGED);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.applyGrowth(world, pos, state);
    }

    @Override
    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int h = this.getGrowthAmount(world);
        if (!world.getBiome(pos).isIn(BiomeTags.IS_JUNGLE)){
            h = h - 1;
        }else{
            h = h + 1;
        }
        if (h < 0){
            h = 0;
        }
        int i = this.getAge(state) + h;
        System.out.println(i);
        int j = this.getMaxAge();
        if (i > j) {
            CropBlock tallRiceCrop = (CropBlock)ModBlocks.TALL_RICE_CROP_BLOCK;
            if (world.getBlockState(pos.up()).isOf(tallRiceCrop)){
                int tallCropAge = world.getBlockState(pos.up()).get(AGE);
                int g = tallCropAge + h;
                int f = tallRiceCrop.getMaxAge();
                if (g > f){
                    g = f;
                }
                world.setBlockState(pos.up(), tallRiceCrop.withAge(g), 2);
                System.out.println(world.getBlockState(pos.up()).get(AGE));
            }else{
                world.setBlockState(pos.up(), tallRiceCrop.withAge(i - j), 2);
                System.out.println(tallRiceCrop.getAgeProperty());
                System.out.println(world.getBlockState(pos.up()).get(AGE));
            }
            i = j;
        }
        world.setBlockState(pos, this.withAge(i), 2);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(ModBlocks.FLOODED_FARMLAND)
                && floor.get(Properties.WATERLOGGED);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        Identifier identifier = this.getLootTableId();
        System.out.println(this.getLootTableId());
        System.out.println(identifier == LootTables.EMPTY);
        if (identifier == LootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            System.out.println("not empty");
            LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
            ServerWorld serverWorld = lootContext.getWorld();
            LootTable lootTable = serverWorld.getServer().getLootManager().getTable(identifier);
            return lootTable.generateLoot(lootContext);
        }
    }
}
