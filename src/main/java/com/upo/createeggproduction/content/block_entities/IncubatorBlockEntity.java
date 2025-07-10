package com.upo.createeggproduction.content.block_entities;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class IncubatorBlockEntity extends BlockEntity {


    public static final int INVENTORY_SLOTS = 16;
    public static final int HATCH_TIME_TICKS = 300;
    public static final TagKey<Item> EGG_TAG = ItemTags.create(new ResourceLocation("forge", "eggs"));

    protected final ItemStackHandler itemHandler;
    private LazyOptional<IItemHandler> lazyItemHandler;
    protected final List<Integer> hatchingTimers;



    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INCUBATOR_BE.get(), pos, state);


        this.itemHandler = new ItemStackHandler(INVENTORY_SLOTS) {

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.is(EGG_TAG);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                if (!this.isItemValid(slot, stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }


            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }



            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
        };


        this.lazyItemHandler = LazyOptional.empty();


        this.hatchingTimers = new ArrayList<>(INVENTORY_SLOTS);
        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            this.hatchingTimers.add(-1);
        }
    }




    public static void serverTick(Level level, BlockPos pos, BlockState state, IncubatorBlockEntity be) {
        if (level.isClientSide()) return;

        boolean needsUpdate = false;
        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            ItemStack stack = be.itemHandler.getStackInSlot(i);
            int timer = be.hatchingTimers.get(i);

            if (!stack.is(EGG_TAG)) {
                if (timer != -1) {
                    be.hatchingTimers.set(i, -1);
                    needsUpdate = true;
                }
                continue;
            }


            BlockState belowState = level.getBlockState(pos.below());
            HeatLevel heat = BlazeBurnerBlock.getHeatLevelOf(belowState);
            boolean canHatch = heat != HeatLevel.NONE;

            if (timer == -1) {
                if (canHatch) {
                    be.hatchingTimers.set(i, HATCH_TIME_TICKS);
                    needsUpdate = true;
                }
            } else if (timer > 0) {
                if (canHatch) {
                    timer--;
                    be.hatchingTimers.set(i, timer);
                    if (timer == 0) {
                        hatch(level, pos, state, be, i, heat);
                        needsUpdate = true;
                    }
                }
            }
        }
        if (needsUpdate) {
            be.setChanged();
        }
    }

    private static void hatch(Level level, BlockPos pos, BlockState state, IncubatorBlockEntity be, int slot, HeatLevel finalHeat) {
        BlockPos spawnPos = pos.relative(state.getValue(HorizontalDirectionalBlock.FACING));
        be.itemHandler.setStackInSlot(slot, ItemStack.EMPTY);

        if (finalHeat == HeatLevel.SEETHING) {
            spawnItemEntity(level, spawnPos, new ItemStack(Items.CHARCOAL));
            spawnItemEntity(level, spawnPos, new ItemStack(Items.BONE, level.random.nextInt(0, 2)));
            level.playSound(null, pos, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 0.5F, 1.5F);
        } else if (finalHeat.isAtLeast(HeatLevel.KINDLED)) {
            spawnItemEntity(level, spawnPos, new ItemStack(Items.COOKED_CHICKEN));
            spawnItemEntity(level, spawnPos, new ItemStack(Items.FEATHER, level.random.nextInt(1, 3)));
            level.playSound(null, pos, SoundEvents.CHICKEN_HURT, SoundSource.BLOCKS, 0.5F, 1.0F);
        } else {
            Chicken chicken = EntityType.CHICKEN.create(level);
            if (chicken != null) {
                chicken.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                level.addFreshEntity(chicken);
                level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        }
        be.hatchingTimers.set(slot, -1);
        be.setChanged();
    }

    private static void spawnItemEntity(Level level, BlockPos pos, ItemStack stack) {
        if (stack.isEmpty() || level == null) return;
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, stack.copy());
        itemEntity.setDeltaMovement(level.random.nextDouble() * 0.1 - 0.05, 0.1, level.random.nextDouble() * 0.1 - 0.05);
        level.addFreshEntity(itemEntity);
    }

    public IItemHandler getInventory() {
        return itemHandler;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (!this.lazyItemHandler.isPresent()) {
                this.lazyItemHandler = LazyOptional.of(() -> this.itemHandler);
            }
            return this.lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("Inventory"));
        int[] loadedTimers = tag.getIntArray("Timers");
        this.hatchingTimers.clear();
        IntStream.range(0, INVENTORY_SLOTS).forEach(i ->
                this.hatchingTimers.add(i < loadedTimers.length ? loadedTimers[i] : -1)
        );
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", itemHandler.serializeNBT());
        tag.put("Timers", new IntArrayTag(this.hatchingTimers));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.load(tag);
        }
    }
}