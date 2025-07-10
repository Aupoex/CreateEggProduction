package com.upo.createeggproduction.content.block_entities;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;

public class EggCollectorBlockEntity extends KineticBlockEntity {


    private static final TagKey<Item> SEED_TAG = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "seeds"));
    private static final int INVENTORY_SIZE = 2;
    private static final int FLUID_CAPACITY = 1000;
    protected SmartInventory inventory;
    protected LazyOptional<IItemHandler> itemHandlerCapability;
    public SmartFluidTankBehaviour tank;
    private int progress = 0;
    private static final int REQUIRED_SEED_AMOUNT = 2;
    private static final int REQUIRED_WATER_AMOUNT = 100;
    private static final int EGG_OUTPUT_AMOUNT = 1;
    private static final int BASE_WORK_PER_EGG = 5120;

    private static final Logger LOGGER = LogUtils.getLogger();
    private int tickCounter = 0;

    public EggCollectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);


        inventory = new SmartInventory(INVENTORY_SIZE, this) {

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot == 0 && stack.is(SEED_TAG);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                if (slot == 1) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }


            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {

                if (slot == 0) {
                    return ItemStack.EMPTY;
                }

                return super.extractItem(slot, amount, simulate);
            }
        };

        itemHandlerCapability = LazyOptional.empty();
    }




    private void produceEgg() {

        ItemStack currentOutput = inventory.getStackInSlot(1);
        if (currentOutput.isEmpty()) {
            inventory.setStackInSlot(1, new ItemStack(Items.EGG, EGG_OUTPUT_AMOUNT));
        } else {
            currentOutput.grow(EGG_OUTPUT_AMOUNT);
        }
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.TYPE, this, 1, FLUID_CAPACITY, false)
                .whenFluidUpdates(this::setChanged);
        behaviours.add(tank);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (!itemHandlerCapability.isPresent()) {
                itemHandlerCapability = LazyOptional.of(() -> inventory);
            }
            return itemHandlerCapability.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return tank.getCapability().cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerCapability.invalidate();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Progress", progress);
        compound.put("Inventory", inventory.serializeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        progress = compound.getInt("Progress");
        inventory.deserializeNBT(compound.getCompound("Inventory"));
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) {
            return;
        }

        if (getSpeed() != 0) {

            if (progress > 0) {
                progress -= Math.abs(getSpeed());
                setChanged();

                if (progress <= 0) {
                    produceEgg();
                    progress = 0;
                }
            }
            else if (canProcess()) {
                consumeInputs();
                progress = BASE_WORK_PER_EGG;
                setChanged();
            }
        }


        tickCounter++;
        if (tickCounter >= 20) {
            tickCounter = 0;


            ItemStack seedSlot = inventory.getStackInSlot(0);
            ItemStack eggSlot = inventory.getStackInSlot(1);


        }
    }


    private boolean canProcess() {
        ItemStack seedSlot = inventory.getStackInSlot(0);
        FluidStack waterTank = tank.getPrimaryHandler().getFluidInTank(0);
        ItemStack outputSlot = inventory.getStackInSlot(1);

        if (seedSlot.getCount() < REQUIRED_SEED_AMOUNT) return false;
        if (!waterTank.getFluid().isSame(Fluids.WATER) || waterTank.getAmount() < REQUIRED_WATER_AMOUNT) return false;

        return outputSlot.isEmpty() || (outputSlot.getItem() == Items.EGG && outputSlot.getCount() + EGG_OUTPUT_AMOUNT <= outputSlot.getMaxStackSize());
    }

    private void consumeInputs() {

        ItemStack seedStack = inventory.getStackInSlot(0);

        seedStack.shrink(REQUIRED_SEED_AMOUNT);

        tank.getPrimaryHandler().drain(REQUIRED_WATER_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
    }
}