package com.upo.createeggproduction;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.upo.createeggproduction.content.block_entities.EggCollectorBlockEntity;
import com.upo.createeggproduction.content.block_entities.ModBlockEntities;
import com.upo.createeggproduction.events.ModEvents;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.upo.createeggproduction.client.render.IncubatorRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.upo.createeggproduction.content.block_entities.visuals.EggCollectorVisual;


@Mod(CreateEggProduction.MODID)
public class CreateEggProduction {
    public static final String MODID = "createeggproduction";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public CreateEggProduction() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeTabs.register(modEventBus);
        ModPartials.init();
        ModBlocks.load();
        ModBlockEntities.register(modEventBus);

        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(ModEvents.class);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderType translucentType = RenderType.translucent();
            //ItemBlockRenderTypes.setRenderLayer(ModBlocks.EGG_COLLECTOR_BLOCK.get(), translucentType);
            //ItemBlockRenderTypes.setRenderLayer(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK.get(), translucentType);
            //ItemBlockRenderTypes.setRenderLayer(ModBlocks.INCUBATOR_BLOCK.get(), translucentType);

            BlockEntityType<EggCollectorBlockEntity> beType = ModBlockEntities.EGG_COLLECTOR_BE.get();
            SimpleBlockEntityVisualizer.builder(beType)
                    .factory(EggCollectorVisual::new)
                    .skipVanillaRender(blockEntity -> true)
                    .apply();

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
            event.accept(ModBlocks.EGG_COLLECTOR_BLOCK.get());
            event.accept(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK.get());
            event.accept(ModBlocks.INCUBATOR_BLOCK.get());
        }
    }



    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}

