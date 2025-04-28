package com.upo.createeggproduction.content.block_entities;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import java.util.List;


public class EggCollectorBlockEntity extends KineticBlockEntity {

    private static final TagKey<Item> SEED_TAG = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "seeds"));
    protected ItemStackHandler itemHandler = createItemHandler();
    public SmartFluidTankBehaviour tankBehaviour;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int INVENTORY_SIZE = 2;
    private static final int FLUID_CAPACITY = 1000;
    private static final int REQUIRED_SEED_AMOUNT = 2;
    private static final int REQUIRED_WATER_AMOUNT = 100; // mB
    private static final int EGG_OUTPUT_AMOUNT = 1;
    private static final int BASE_WORK_PER_EGG = 5120;
    private int progress = 0;

    public EggCollectorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @NotNull
    private ItemStackHandler createItemHandler() {

        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot == 0) {
                    return stack.is(SEED_TAG);
                }
                return false;
            }
            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot == 1) {
                    ItemStack stackInSlot = getStackInSlot(slot);
                    if (stackInSlot.getItem() != Items.EGG) {
                        return ItemStack.EMPTY;
                    }
                }
                return super.extractItem(slot, amount, simulate);
            }
            @Override
            @NotNull
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (slot == 1) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        tankBehaviour = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.TYPE, this, 1, FLUID_CAPACITY, false)
                .whenFluidUpdates(() -> {
                    setChanged();
                });
        behaviours.add(tankBehaviour);
    }
    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) {
            return;
        }
        float currentSpeed = Math.abs(getSpeed());
        if (currentSpeed == 0 || progress == 0) {
            if (currentSpeed == 0) {
                if (progress != 0) {
                    progress = 0;
                    setChanged();
                }
                return;
            }
            if (canProcess()) {

                startProcessing();
            } else {
                return;
            }
        }
        if (progress >= 0 && currentSpeed > 0) {
            progress += currentSpeed;
            setChanged();
            if (progress >= BASE_WORK_PER_EGG) {

                produceEgg();
                progress = 0;
            }
        } else if (progress != 0) {
            progress = 0;
            setChanged();
        }
    }
    private boolean canProcess() {
        ItemStack seedSlot = itemHandler.getStackInSlot(0);
        if (!seedSlot.is(SEED_TAG) || seedSlot.getCount() < REQUIRED_SEED_AMOUNT) {
            return false;
        }
        if (tankBehaviour == null) return false;
        IFluidHandler fluidHandler = tankBehaviour.getPrimaryHandler();
        if (tankBehaviour.getPrimaryHandler().getFluidAmount() < REQUIRED_WATER_AMOUNT || !tankBehaviour.getPrimaryHandler().getFluid().getFluid().isSame(Fluids.WATER)) {

            if (!tankBehaviour.getPrimaryHandler().getFluid().getFluid().isSame(Fluids.WATER)) {
            }
            return false;
        }
        ItemStack outputSlotStack = itemHandler.getStackInSlot(1);
        int currentOutputCount = outputSlotStack.getCount();
        int maxOutputStackSize = itemHandler.getSlotLimit(1);

        if (outputSlotStack.isEmpty()) {
        } else {
            if (outputSlotStack.getItem() != Items.EGG) {
                return false;
            }
            if (currentOutputCount + EGG_OUTPUT_AMOUNT > maxOutputStackSize) {
                return false;
            }
        }
        return true;
    }

    private void startProcessing() {
        ItemStack stackBefore = itemHandler.getStackInSlot(0).copy();
        ItemStack extracted = itemHandler.extractItem(0, REQUIRED_SEED_AMOUNT, false);
        ItemStack stackAfter = itemHandler.getStackInSlot(0);
        if (tankBehaviour != null) {
            tankBehaviour.getPrimaryHandler().drain(new FluidStack(Fluids.WATER, REQUIRED_WATER_AMOUNT), IFluidHandler.FluidAction.EXECUTE);
            LOGGER.debug("Consumed {} mB water.", REQUIRED_WATER_AMOUNT);
        }
        setChanged();
    }
    private void produceEgg() {
        ItemStack outputSlotStack = itemHandler.getStackInSlot(1);
        ItemStack eggToProduce = new ItemStack(Items.EGG, EGG_OUTPUT_AMOUNT);
        boolean success = false;
        if (outputSlotStack.isEmpty()) {
            itemHandler.setStackInSlot(1, eggToProduce);
            success = true;
        } else if (outputSlotStack.getItem() == Items.EGG) {
            int currentAmount = outputSlotStack.getCount();
            int maxStackSize = itemHandler.getSlotLimit(1);
            int canAdd = maxStackSize - currentAmount;

            if (canAdd >= EGG_OUTPUT_AMOUNT) {
                outputSlotStack.grow(EGG_OUTPUT_AMOUNT);
                success = true;
            } else {
                LOGGER.warn("produceEgg: Tried to produce egg, but slot 1 (containing eggs) is too full ({} / {}). This shouldn't happen if canProcess is correct.", currentAmount, maxStackSize);
            }
        } else {
            LOGGER.warn("produceEgg: Tried to produce egg, but slot 1 contains unexpected item: {}", outputSlotStack.getItem().getDescriptionId());
        }
        if (success) {
            if (level != null && !level.isClientSide) {
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
            }
            setChanged();
        }
    }
    @Nullable
    public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        if (side == Direction.DOWN) {
            return null;
        }
        return itemHandler;
    }
    @Nullable
    public IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
        if (side == Direction.DOWN) {
            return null;
        }
        return tankBehaviour != null ? tankBehaviour.getCapability() : null;
    }
    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("Progress", progress); // 保存计时器
        if (!clientPacket) {
            compound.put("Inventory", itemHandler.serializeNBT(registries));
        }
        super.write(compound, registries, clientPacket);
    }
    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        progress = compound.getInt("Progress");
        if (!clientPacket && compound.contains("Inventory")) {
            itemHandler.deserializeNBT(registries, compound.getCompound("Inventory"));
        }
        super.read(compound, registries, clientPacket);
    }
    public void invalidateCaps() {
    }
    public void reviveCaps() {
    }
    @Override
    public void destroy() {
        super.destroy();
        invalidateCaps();
    }
}
