package com.upo.createeggproduction.content.block_entities;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.ModBlocks;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.upo.createeggproduction.client.render.EggCollectorRenderer;

public class ModBlockEntities {

    private static final CreateRegistrate REGISTRATE = CreateEggProduction.registrate();
    public static final BlockEntityEntry<EggCollectorBlockEntity> EGG_COLLECTOR_BE = REGISTRATE
            .blockEntity("egg_collector_block_entity", EggCollectorBlockEntity::new)
            .validBlocks(ModBlocks.EGG_COLLECTOR_BLOCK)
            .renderer(() -> EggCollectorRenderer::new)
            //.visual(EggCollectorVisual::new)
            .register();
    public static void registerAll() {
    }
}

