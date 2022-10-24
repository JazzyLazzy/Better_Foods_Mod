package com.jazzylazzy.BetterFoods.blocks;

import com.jazzylazzy.BetterFoods.blocks.entities.ModBlockEntities;
import com.jazzylazzy.BetterFoods.blocks.entities.RiceCookerEntity;
import com.jazzylazzy.BetterFoods.util.WaterProperty;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RiceCookerBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final BooleanProperty LIT = Properties.LIT;
    public static final IntProperty WATER_LEVEL = IntProperty.of("water_level", 0, 10000);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
    private BlockEntity riceCookerEntity;

    public RiceCookerBlock(Settings settings) {
        super(settings);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(LIT);
    }

    /*Block Entity*/

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        riceCookerEntity = new RiceCookerEntity(pos, state);
        return riceCookerEntity;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RiceCookerEntity) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        System.out.println("used");
        if (!world.isClient) {
            System.out.println(player.getStackInHand(hand).getItem());
            if (player.getStackInHand(hand).getItem().equals(Items.WATER_BUCKET)){
                System.out.println("used with water");
                ItemStack stack = new ItemStack(Items.BUCKET);
                stack.setCount(1);
                player.setStackInHand(hand, stack);
            }
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.RICE_COOKER, RiceCookerEntity::tick);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
