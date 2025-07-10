package com.upo.createeggproduction;

import com.upo.createeggproduction.client.render.IncubatorRenderer;
import com.upo.createeggproduction.content.block_entities.ModBlockEntities;
import com.upo.createeggproduction.ponder.ModPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = CreateEggProduction.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class ClientModEvents {

    private ClientModEvents() {}

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {

        event.registerBlockEntityRenderer(ModBlockEntities.INCUBATOR_BE.get(), IncubatorRenderer::new);
    }
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new ModPonderPlugin());
    }
}