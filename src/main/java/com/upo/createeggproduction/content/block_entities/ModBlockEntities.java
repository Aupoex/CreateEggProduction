package com.upo.createeggproduction.content.block_entities;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.ModBlocks;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.upo.createeggproduction.client.render.EggCollectorRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


import static com.upo.createeggproduction.ModBlocks.INCUBATOR_BLOCK;


public class ModBlockEntities {

    private static final CreateRegistrate REGISTRATE = CreateEggProduction.registrate();

    public static final BlockEntityEntry<EggCollectorBlockEntity> EGG_COLLECTOR_BE = REGISTRATE
            .blockEntity("egg_collector_block_entity", EggCollectorBlockEntity::new)
            .validBlocks(ModBlocks.EGG_COLLECTOR_BLOCK)
            .renderer(() -> EggCollectorRenderer::new)
            .register();

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateEggProduction.MODID);


    public static final RegistryObject<BlockEntityType<IncubatorBlockEntity>> INCUBATOR_BE =
            BLOCK_ENTITIES.register("incubator_block_entity",
                    () -> BlockEntityType.Builder.of(
                            IncubatorBlockEntity::new,
                            INCUBATOR_BLOCK.get()
                    ).build(null)
            );


    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
    public static void load() {
    }
}