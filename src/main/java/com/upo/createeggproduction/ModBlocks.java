package com.upo.createeggproduction;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.SharedProperties;
import com.upo.createeggproduction.content.blocks.EggCollectorBlock;
import com.upo.createeggproduction.content.blocks.EmptyEggCollectorBlock;
import com.upo.createeggproduction.content.items.EmptyEggCollectorBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.upo.createeggproduction.content.items.EggCollectorBlockItem;

public class ModBlocks {

    private static final CreateRegistrate REGISTRATE = CreateEggProduction.registrate();
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, CreateEggProduction.MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, CreateEggProduction.MODID);

    public static final BlockEntry<EggCollectorBlock> EGG_COLLECTOR_BLOCK = REGISTRATE
            .block("egg_collector_block", EggCollectorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(props -> props.noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item(EggCollectorBlockItem::new)
            .build()
            .register();
    public static final DeferredHolder<Block, EmptyEggCollectorBlock> EMPTY_EGG_COLLECTOR_BLOCK =
            BLOCKS.register("empty_egg_collector_block",
                    () -> new EmptyEggCollectorBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(1.0f, 5.0f)
                            .noOcclusion()
                            .requiresCorrectToolForDrops()
                    )
            );
    public static final DeferredHolder<Item, EmptyEggCollectorBlockItem> EMPTY_EGG_COLLECTOR_BLOCK_ITEM =
            ITEMS.register("empty_egg_collector_block",
                    () -> new EmptyEggCollectorBlockItem(
                            EMPTY_EGG_COLLECTOR_BLOCK.get(),
                            new Item.Properties()
                    )
            );
    public static void registerAll(IEventBus modEventBus) {
        LOGGER.debug("Registering ModBlocks DeferredRegisters to event bus...");
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }

}

