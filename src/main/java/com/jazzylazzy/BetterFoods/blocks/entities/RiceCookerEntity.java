package com.jazzylazzy.BetterFoods.blocks.entities;

import com.jazzylazzy.BetterFoods.blocks.RiceCookerBlock;
import com.jazzylazzy.BetterFoods.recipes.CookedRiceRecipe;
import com.jazzylazzy.BetterFoods.screen.RiceCookerScreenHandler;
import com.jazzylazzy.BetterFoods.util.MbDropletConverterKt;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RiceCookerEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private int water = 0;
    private int maxWater = 1000;

    public RiceCookerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RICE_COOKER, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> RiceCookerEntity.this.progress;
                    case 1 -> RiceCookerEntity.this.maxProgress;
                    case 2 -> RiceCookerEntity.this.water;
                    case 3 -> RiceCookerEntity.this.maxWater;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RiceCookerEntity.this.progress = value;
                    case 1 -> RiceCookerEntity.this.maxProgress = value;
                    case 2 -> RiceCookerEntity.this.water = value;
                    case 3 -> RiceCookerEntity.this.maxWater = value;
                }
            }

            public int size() {
                return 2;
            }
        };
    }

    public SingleVariantStorage<FluidVariant> waterStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return MbDropletConverterKt.convertDropletsToMb(FluidConstants.BUCKET);
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if(!world.isClient()) {
                sendFluidPacket();
            }
        }
    };

    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.waterStorage.variant = fluidVariant;
        this.waterStorage.amount = fluidLevel;
    }

    private void sendFluidPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        waterStorage.variant.toPacket(data);
        data.writeLong(waterStorage.amount);
        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.FLUID_SYNC, data);
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("Rice Cooker");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RiceCookerScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("rice_cooker.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("rice_cooker.progress");
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, RiceCookerEntity entity) {
        if(world.isClient()) {
            return;
        }

        Integer waterLevel = state.get(RiceCookerBlock.WATER_LEVEL);

        if(hasRecipe(entity)) {
            entity.progress++;
            markDirty(world, blockPos, state);
            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }

    private static void craftItem(RiceCookerEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<CookedRiceRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(CookedRiceRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if(hasRecipe(entity)) {
            entity.removeStack(0, 1);

            entity.setStack(2, new ItemStack(recipe.get().getOutput().getItem(),
                    entity.getStack(2).getCount() + 1));

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(RiceCookerEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<CookedRiceRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(CookedRiceRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item item) {
        return inventory.getStack(2).getItem() == item || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return  true;
    }

    private void resetProgress(){
        this.progress = 0;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
}
