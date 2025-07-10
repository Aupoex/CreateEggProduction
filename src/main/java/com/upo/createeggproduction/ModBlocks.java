package com.upo.createeggproduction;

import com.simibubi.create.foundation.data.SharedProperties;
import com.upo.createeggproduction.content.blocks.EggCollectorBlock;
import com.upo.createeggproduction.content.blocks.EmptyEggCollectorBlock;
import com.upo.createeggproduction.content.blocks.IncubatorBlock;
import com.upo.createeggproduction.content.items.EmptyEggCollectorBlockItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MapColor;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.upo.createeggproduction.content.items.EggCollectorBlockItem;

public class ModBlocks {

    private static final CreateRegistrate REGISTRATE = CreateEggProduction.registrate();

    public static final BlockEntry<EggCollectorBlock> EGG_COLLECTOR_BLOCK = REGISTRATE
            .block("egg_collector_block", EggCollectorBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item(EggCollectorBlockItem::new).tab(ModCreativeTabs.MAIN_TAB.getKey())
            .build()
            .register();

    public static final BlockEntry<EmptyEggCollectorBlock> EMPTY_EGG_COLLECTOR_BLOCK = REGISTRATE
            .block("empty_egg_collector_block", EmptyEggCollectorBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.STONE).strength(1.0f, 5.0f))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item(EmptyEggCollectorBlockItem::new).tab(ModCreativeTabs.MAIN_TAB.getKey())
            .build()
            .register();

    public static final BlockEntry<IncubatorBlock> INCUBATOR_BLOCK = REGISTRATE
            .block("incubator", IncubatorBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(1.2f, 4.0f)
                    .lightLevel(state -> 3)
                    .noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item().tab(ModCreativeTabs.MAIN_TAB.getKey())
            .build()
            .register();

    public static void load() {
    }
}


