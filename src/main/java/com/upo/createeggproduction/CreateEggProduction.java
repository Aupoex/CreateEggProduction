package com.upo.createeggproduction;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.upo.createeggproduction.content.block_entities.EggCollectorBlockEntity;
import com.upo.createeggproduction.content.block_entities.ModBlockEntities;
import com.upo.createeggproduction.integration.ponder.PonderHelper;
import com.upo.createeggproduction.ponder.ModPonderPlugin;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import com.upo.createeggproduction.client.render.IncubatorRenderer;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import com.upo.createeggproduction.content.block_entities.visuals.EggCollectorVisual;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(CreateEggProduction.MODID)
public class CreateEggProduction {
    public static final String MODID = "createeggproduction";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab(ModCreativeTabs.MAIN_TAB.getKey());

    public CreateEggProduction(IEventBus modEventBus) {
        ModCreativeTabs.register(modEventBus);
        ModPartials.init();
        ModBlocks.load();
        ModBlockEntities.load();
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(ModDataGen::gatherData);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::addCreative);
        PonderHelper.registerPlugin();
    }
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderType translucentType = RenderType.translucent();
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EGG_COLLECTOR_BLOCK.get(), translucentType);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK.get(), translucentType);
            PonderIndex.addPlugin(new ModPonderPlugin());
            //注册 Visual
            BlockEntityType<EggCollectorBlockEntity> beType = ModBlockEntities.EGG_COLLECTOR_BE.get();
            SimpleBlockEntityVisualizer.builder(beType)
                    .factory(EggCollectorVisual::new)
                    .skipVanillaRender(blockEntity -> true)
                    .apply();
            //孵化器渲染
            RenderType incubatorRenderType = RenderType.translucent();
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.INCUBATOR_BLOCK.get(), incubatorRenderType);
            BlockEntityRenderers.register(ModBlockEntities.INCUBATOR_BE.get(), IncubatorRenderer::new);
        });
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                ModStressValues.registerAll();
            } catch (Exception e) {
                LOGGER.error("Error occurred during stress value registration task submission!", e);
            }
        });
    }
    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ModCreativeTabs.MAIN_TAB.getKey()) {
            event.accept(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK_ITEM.get());
            event.accept(ModBlocks.INCUBATOR_BLOCK_ITEM.get());
        }
    }
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.EGG_COLLECTOR_BE.get(),
                (be, context) -> {
                    IItemHandler originalHandler = be.getItemHandlerCapability(context);
                    if (originalHandler == null) return null;
                    return new IItemHandler() {
                        @Override public int getSlots() { return originalHandler.getSlots(); }
                        @Override @NotNull public ItemStack getStackInSlot(int slot) { return originalHandler.getStackInSlot(slot); }
                        @Override @NotNull public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) { return originalHandler.insertItem(slot, stack, simulate); }
                        @Override public int getSlotLimit(int slot) { return originalHandler.getSlotLimit(slot); }
                        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return originalHandler.isItemValid(slot, stack); }
                        @Override
                        @NotNull
                        public ItemStack extractItem(int slot, int amount, boolean simulate) {
                            if (slot == 0) {
                                return ItemStack.EMPTY;
                            }
                            return originalHandler.extractItem(slot, amount, simulate);
                        }
                    };
                }
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.EGG_COLLECTOR_BE.get(),
                (be, context) -> {
                    return be.getFluidHandlerCapability(context);
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.INCUBATOR_BE.get(),
                (be, context) -> be.getItemHandlerCapability(context)
        );
    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}

