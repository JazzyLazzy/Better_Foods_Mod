package com.jazzylazzy.BetterFoods.mixin;

import com.jazzylazzy.BetterFoods.BetterFoods;
import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(HoeItem.class)
public class HoeTillWaterMixin {
	@Inject(at = @At("RETURN"), method = "canTillFarmland", cancellable = true)
	private static void injectTillWater(ItemUsageContext context, CallbackInfoReturnable<Boolean> info) {
		if (context.getSide() != Direction.DOWN &&
				context.getWorld().getBlockState(context.getBlockPos().up()).isOf(Blocks.WATER)){
			info.setReturnValue(true);
		}
	}

	@Inject(at = @At("RETURN"), method = "createTillAction", cancellable = true)
	private static void injectWateredFarmland(BlockState result, CallbackInfoReturnable<Consumer<ItemUsageContext>> info){
		info.setReturnValue((itemUsageContext) -> {
			if (itemUsageContext.getWorld().getBlockState(itemUsageContext.getBlockPos().up()).isOf(Blocks.WATER)){
				itemUsageContext.getWorld().setBlockState(itemUsageContext.getBlockPos(), ModBlocks.FLOODED_FARMLAND.getDefaultState(), 11);
				itemUsageContext.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, itemUsageContext.getBlockPos(), GameEvent.Emitter.of(itemUsageContext.getPlayer(), ModBlocks.FLOODED_FARMLAND.getDefaultState()));
			}else{
				itemUsageContext.getWorld().setBlockState(itemUsageContext.getBlockPos(), result, 11);
				itemUsageContext.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, itemUsageContext.getBlockPos(), GameEvent.Emitter.of(itemUsageContext.getPlayer(), result));
			}
		});
	}
}